package damas;

import UI.VistaJuego;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;
import utilidades.Movimiento;

public class Cliente {

    private VistaJuego vista;
    private Controlador controlador;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    private String nombreJugador;
    
    private Thread oyenteEntrada;
    
    private Tablero tablero;
    private ControladorTableroJuego controladorPartida;
    
    private String miColor;
    
    public Cliente(Reglas reglamento) {
        vista = new VistaJuego("damas");
        
        controlador = new Controlador(reglamento, this);
        
        vista.addControlador(controlador);
        controlador.vista(vista);
        vista.setCliente(this);
        
        crearConexionServidor();
        
        comenzarAEsperarEntrada();

    }
    
    
    private void crearConexionServidor() {
        try {
            socket = new Socket("localhost", 10000);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            
            nombreJugador = vista.preguntarNombre();
            salida.println(nombreJugador);
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void comenzarAEsperarEntrada() {
        
        oyenteEntrada = new Thread() {
            @Override
            public void run() {
                String mensajeEntrada;
                try {
                  do {
                       mensajeEntrada = entrada.readLine();
                       System.out.println("el server dice que " + mensajeEntrada);
                       switch ( mensajeEntrada ) {
                           case "Actualizar lista":
                               System.out.println("me mandan la lista");
                               leerListaActualizada();
                               break;
                               
                           case "Ganador":
                               System.out.println("he ganado :)");
                               salida.println("Ganador");
                               break;
                           case "Perdedor":
                               System.out.println("he perdido :(");
                               salida.println("Perdedor");
                               break;
                           case "Aceptar reto":
                               mensajeEntrada = entrada.readLine();
                               boolean aceptas = vista.preguntarReto(mensajeEntrada);
                               System.out.println("se acepta?" + aceptas);
                               if ( aceptas ) {
                                   salida.println("Aceptar reto");
                                   iniciarPartida(false);
                               }
                               else 
                                   salida.println("Cancelar reto");
                           case "Reto aceptado":
                               System.out.println("aceptas el desafio");
                               iniciarPartida(true);
                               break;
                           case "Reto rechazado":
                               System.out.println("el otro es un cobardeeeeee");
                               break;
                           case "Salir":
                               mensajeEntrada = null;
                               break;
                           default:
                               System.out.println("Ai dont anderstand llu");
                       }
                  } while ( mensajeEntrada != null );
                } catch (IOException ex) {
                    vista.mostrarError("Error de entrada salida");
                }
            }
        };

        oyenteEntrada.start();
        
    }
    
    public void actualizarListaJugadores() {
        System.out.println("actualizare la lista");
        
        salida.println("Actualizar lista");
        
//        String[] jugadores = {"diego", "ezequiel", "jugador1", "pruebasGUI2"};
//        vista.actualizarListaJugadores(jugadores);
        
    }
    
    private void leerListaActualizada() {
        ArrayList<String> listaJugadores = new ArrayList<>();
        try {
            String mensaje = entrada.readLine();
            while ( ! mensaje.equals("OK") ) {
                listaJugadores.add(mensaje);
                mensaje = entrada.readLine();
            }
            
        } catch (IOException ex) {
            vista.mostrarError("Error en la recepcion de datos con el servidor");
        } finally {
            vista.actualizarListaJugadores(listaJugadores);
        }
    }
    
    public void retarJugador(String nombreRival) {
        System.out.println("retare a "  + nombreRival);
        
        salida.println("Retar");
        salida.println(nombreRival);
    }
    
    public void cerrarCliente() {
        try {
            salida.println("Cerrar");
            entrada.close();
            salida.close();
            socket.close();
        } catch (IOException ex) {
            vista.mostrarError("Fallo en la comunicacion con el servidor");
        }
    }
    
    private void iniciarPartida(boolean soyJugador1) {
        tablero = new Tablero();
        miColor = ( soyJugador1 ? Ficha.BLANCA: Ficha.NEGRA );
        
        controladorPartida = new ControladorTableroJuego(this, vista);
        
        vista.setUIJuego();
        vista.crearTableroSwing(tablero.getFilaMaxima()+1, tablero.getColumnaMaxima()+1);
        vista.addControladorDePartida(controladorPartida);
    }
    
    public Tablero getTablero() {
        return tablero;
    }
    
    public void movimientosValidos(int fila, int columna) {
        boolean esUnPeon;
        String colorFicha;
        ArrayList<Movimiento> movimientosValidos;
        
        if ( ! tablero.estaLaCasillaVacia(fila, columna) ) {
            
            esUnPeon = tablero.getFicha(fila, columna).isTransformable();
            colorFicha = tablero.getFicha(fila, columna).getColor();
            if ( ! colorFicha.equals(miColor) )
                return;
            else if ( esUnPeon )
                movimientosValidos = movimientosPeonValidos(fila, columna, colorFicha);
            else
                movimientosValidos = movimientosDamaValidos(fila, columna, colorFicha);
         
            
            for (Movimiento m : movimientosValidos) {
                vista.mostrarMovimientoValido(m.getFilaFinal(), m.getColFinal());
            }
        }
    }
    
    private ArrayList<Movimiento> movimientosPeonValidos(int fila, int columna, String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>(2);
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        int avanceFila = ( color.equals(Ficha.BLANCA) ? -1 : 1 );
        
        if ( tablero.estaLaCasillaVacia(fila + avanceFila, columna - 1) )
            movimientosValidos.add(new Movimiento(fila, columna, fila + avanceFila, columna - 1));
        else if ( ! tablero.fichaDelMismoColor(fila + avanceFila, columna - 1, color) )
            if ( tablero.estaLaCasillaVacia(fila + ( 2 * avanceFila ), columna - 2) )
                movimientosValidos.add(new Movimiento(fila, columna, fila + (2 * avanceFila), columna - 2));
        
        if ( tablero.estaLaCasillaVacia(fila + avanceFila, columna + 1) )
            movimientosValidos.add(new Movimiento(fila, columna, fila + avanceFila, columna + 1));
        else if ( ! tablero.fichaDelMismoColor(fila + avanceFila, columna + 1, color) )
            if ( tablero.estaLaCasillaVacia(fila + ( 2 * avanceFila ), columna + 2) )
                movimientosValidos.add(new Movimiento(fila, columna, fila + (2 * avanceFila), columna + 2));
        
        return movimientosValidos;
    }
    
    private ArrayList<Movimiento> movimientosDamaValidos(int fila, int columna, String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>();
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, 1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, -1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, 1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, -1, color, movimientosValidos);
        
        return movimientosValidos;
    }
    
    private void rellenarMovimientosDamaEnDireccion(int fila, int columna, int aumentoFila, int aumentoColumna, String color, ArrayList<Movimiento> movimientos) {
        
        boolean finComprobacion = false;
        boolean fichaComida = false;
        
        int filaInvestigada = fila + aumentoFila;
        int columnaInvestigada = columna + aumentoColumna;
        
        while ( ! finComprobacion  )  {
            
            if ( tablero.estaLaCasillaVacia(filaInvestigada, columnaInvestigada) )
                movimientos.add(new Movimiento(fila, columna, filaInvestigada, columnaInvestigada));
            else if ( ! tablero.fichaDelMismoColor(filaInvestigada, columnaInvestigada, color) 
                    && ! fichaComida)
                fichaComida = true;
            else
                finComprobacion = true;
            
            filaInvestigada += aumentoFila;
            columnaInvestigada += aumentoColumna;
            
        }
        
    }
    
    public void terminarTurno() {
        // sin implementar
    }
    
    public void rendirse() {
        // sin implementar
        // conceder( (turno % 2 == 1) ? jugador2.getNombre() : jugador1.getNombre() );
    }
    
    
    
}