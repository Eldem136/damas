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

public class Cliente {

    private VistaJuego vista;
    private Controlador controlador;
    
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    
    private String nombreJugador;
    
    private Thread oyenteEntrada;
    
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
                               if ( aceptas )
                                   salida.println("Aceptar reto");
                               else 
                                   salida.println("Cancelar reto");
                           case "Reto aceptado":
                               System.out.println("aceptas el desafio");
                               break;
                           case "Reto rechazado":
                               System.out.println("cobardeeeeee");
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
            System.out.println("cierro thread");
            //oyenteEntrada.join();
            System.out.println("cierro entrada");
            entrada.close();
            System.out.println("cierro salida");
            salida.close();
            System.out.println("cierro socket");
            socket.close();
            System.out.println("cerre todo");
        } catch (IOException ex) {
            vista.mostrarError("Fallo en la comunicacion con el servidor");
        //} catch (InterruptedException ex) {
          //  vista.mostrarError("Fallo al cerrar el thread de comunicacion");
        }
    }
    
    
    
    
}