package damas_v3_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloPartida implements Runnable{
    
    private HiloCliente jugador1;
    private HiloCliente jugador2;

    public HiloPartida(HiloCliente hiloJugador1, HiloCliente hiloJugador2) {
        this.jugador1 = hiloJugador1;
        this.jugador2 = hiloJugador2;
    }

//    @Override
//    public void run() {
//        
//        try {
//            salida2.println("Aceptar reto");
//            salida2.println(nombre1);
//            String respuesta = entrada2.readLine();
//            System.out.println("la respuesta al desafio es " + respuesta);
//            if ( respuesta.equals("OK") ){
//                salida1.println("Reto aceptado");
//                
//                System.out.println("Hey! Listen! La partida entre estos dos jugadores comienza");
//
//                salida1.println("Ganador");
//                salida2.println("Perdedor");
//            
//                System.out.println("Hey! Listen! La partiida entre estos dos jugadores ha terminado");  
//            } else 
//                salida1.println("Reto rechazado");
//            
//            
//        } catch (IOException ex) {
//            Logger.getLogger(HiloPartida.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    @Override
    public void run() {
        retar();
    }
    
    private synchronized void retar() {
        try {
            jugador2.enviaMensaje("Aceptar reto");
            jugador2.enviaMensaje(jugador1.getNombreCliente());
            
            jugador2.esperoMensajes(this);
            wait();
            String respuesta = jugador2.leerMensaje();
            
            if ( respuesta.equals("Aceptar reto") ) {
                jugador1.enviaMensaje("Reto aceptado");
                
                System.out.println("Hey! Listen! La partida entre estos dos jugadores comienza");
                
                jugador1.enviaMensaje("Ganador");
                jugador2.enviaMensaje("Perdedor");
            
                System.out.println("Hey! Listen! La partiida entre estos dos jugadores ha terminado");  
            } else
                jugador1.enviaMensaje("Reto rechazado");
            
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloPartida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
}