package damas;

import UI.VistaJuego;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
    
    private String nombreJugador;
    
    private Thread oyenteEntrada;
    
    private Tablero tablero;
    private ControladorTableroJuego controladorPartida;
    
    private String miColor;
    private boolean esMiTurno;
    private final static String MENSAJE_ES_MI_TURNO = "Jugador %s es tu turno de seleccionar movimiento";
    private final static String MENSAJE_NO_ES_MI_TURNO = "Esperando al servidor";
    
    public Cliente(Reglas reglamento) {
        vista = new VistaJuego("damas");
        
        controlador = new Controlador(reglamento, this);
        
        vista.addControlador(controlador);
        controlador.vista(vista);
        vista.setCliente(this);
        
        crearConexionServidor();
        System.out.println("he salido");
        comenzarAEsperarEntrada();

    }
    
    
    private void crearConexionServidor() {
        try {
            System.out.println("comienzo conexion server");
            socket = new Socket("localhost", 10000);
            //entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            //entradaObjetos = new ObjectInputStream(socket.getInputStream());
            //entradaObjetos = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
            salidaObjetos.flush();
            entradaObjetos = new ObjectInputStream(socket.getInputStream());
            
            System.out.println("termino conexion, voy a preguntar");
            nombreJugador = vista.preguntarNombre();
            //salida.println(nombreJugador);
            salidaObjetos.writeObject(nombreJugador);
            salidaObjetos.flush();
            
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
                       //mensajeEntrada = entrada.readLine();
                       mensajeEntrada = entradaObjetos.readObject().toString();
System.out.println("el server dice que " + mensajeEntrada);
                       switch ( mensajeEntrada ) {
                           case "Actualizar lista":
                               System.out.println("me mandan la lista");
                               leerListaActualizada();
                               break;
                               
                           case "Ganador":
                               System.out.println("he ganado :)");
                               vista.mostrarFinal("HAS GANADO :D");
                               vista.addControlador(controlador);
                               break;
                           case "Perdedor":
                               System.out.println("he perdido :(");
                               vista.mostrarFinal("Has perdido, buuuuu!");
                               vista.addControlador(controlador);
                               break;
                           case "Aceptar reto":
                               //mensajeEntrada = entrada.readLine();
                               mensajeEntrada = entradaObjetos.readObject().toString();
                               boolean aceptas = vista.preguntarReto(mensajeEntrada);
       System.out.println("se acepta?" + aceptas);
                               if ( aceptas ) {
                                   //salida.println("Aceptar reto");
                                   salidaObjetos.writeObject("Aceptar reto");
                                   salidaObjetos.flush();
                                   iniciarPartida(false);
                               }
                               else 
                                   salida.println("Cancelar reto");
                               break;
                           case "Reto aceptado":
                               System.out.println("acepta mi desafio");
                               iniciarPartida(true);
                               break;
                           case "Reto rechazado":
                               System.out.println("el otro es un cobardeeeeee");
                               break;
                           case "Salir":
                               mensajeEntrada = null;
                               break;
                           case "Fin turno":
                               actualizaTablero();
                               break;
                           case "Empieza turno":
                               iniciarTurno();
                               break;
                           default:
                               System.out.println("Ai dont anderstand llu");
                       }
                  } while ( mensajeEntrada != null );
                } catch (IOException ex) {
                    vista.mostrarError("Error de entrada salida");
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        oyenteEntrada.start();
        
    }
    
    public void actualizarListaJugadores() {
        try {
            System.out.println("actualizare la lista");
            
            //salida.println("Actualizar lista");
            salidaObjetos.writeObject("Actualizar lista");
            salidaObjetos.flush();
            
//        String[] jugadores = {"diego", "ezequiel", "jugador1", "pruebasGUI2"};
//        vista.actualizarListaJugadores(jugadores);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void leerListaActualizada() {
        ArrayList<String> listaJugadores = new ArrayList<>();
        try {
            //String mensaje = entrada.readLine();
            String mensaje = entradaObjetos.readObject().toString();
            while ( ! mensaje.equals("OK") ) {
                listaJugadores.add(mensaje);
                //mensaje = entrada.readLine();
                mensaje = entradaObjetos.readObject().toString();
            }
            
        } catch (IOException ex) {
            vista.mostrarError("Error en la recepcion de datos con el servidor");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            vista.actualizarListaJugadores(listaJugadores);
        }
    }
    
    public void retarJugador(String nombreRival) {
        try {
            System.out.println("retare a "  + nombreRival);
            
            //salida.println("Retar");
            //salida.println(nombreRival);
            salidaObjetos.writeObject("Retar");
            salidaObjetos.flush();
            salidaObjetos.writeObject(nombreRival);
            salidaObjetos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void cerrarCliente() {
        try {
            //salida.println("Cerrar");
            salidaObjetos.writeObject("Cerrar");
            salidaObjetos.flush();
            entradaObjetos.close();
            salidaObjetos.close();
            socket.close();
        } catch (IOException ex) {
            vista.mostrarError("Fallo en la comunicacion con el servidor");
        }
    }
    
    private void iniciarPartida(boolean soyJugador1) {
        tablero = new Tablero();
        //tablero.colocarFichas();
        tablero.colocarFichasParaGanar();
        miColor = ( soyJugador1 ? Ficha.NEGRA : Ficha.BLANCA );
        System.out.println("soy el jugador " + nombreJugador + " y me tocan " + miColor + " el bool es " + soyJugador1);
        controladorPartida = new ControladorTableroJuego(this, vista);

        vista.setUIJuego();
        vista.crearTableroSwing(tablero.getFilaMaxima()+1, tablero.getColumnaMaxima()+1);
        vista.addControladorDePartida(controladorPartida);

        vista.update(tablero, null);


        vista.cambiarTexto( ( ! soyJugador1 ?  String.format(MENSAJE_ES_MI_TURNO, "blanco") : MENSAJE_NO_ES_MI_TURNO ) );
        
        esMiTurno = ! soyJugador1;
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
    
    public void terminarTurno(Movimiento movimiento) {
        if ( ! esMiTurno )
            return;
        try {
            System.out.println("mando al servidor el movimiento " + movimiento.toString());
            salidaObjetos.writeObject("Movimiento");
            salidaObjetos.writeObject(movimiento);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void rendirse() {
        // sin implementar
        // conceder( (turno % 2 == 1) ? jugador2.getNombre() : jugador1.getNombre() );
    }
    
    public void actualizaTablero() {
        System.out.println("leere el tablero");
        try {
            tablero = (Tablero) entradaObjetos.readObject();
            System.out.println("tengo el tablero nuevo " + tablero.toString());
            vista.update(tablero, null);
            vista.cambiarTexto(MENSAJE_NO_ES_MI_TURNO);
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al actualizar el tablero");
        } catch (ClassNotFoundException ex) {
            vista.mostrarError("Error de clase al actualizar el tablero");
        }
    }
    
    private void iniciarTurno() {
        String mensaje = String.format(MENSAJE_ES_MI_TURNO, (miColor.equals(Ficha.BLANCA)? "blanco":"negro"));
        vista.cambiarTexto(mensaje);
        esMiTurno = true;
        
    }
    
}