package damas_v3_server;

import damas.Ficha;
import damas.Tablero;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;
import utilidades.Movimiento;

public class HiloPartida implements Runnable{
    
    private HiloCliente jugador1;
    private HiloCliente jugador2;
    
    private int turno;
    
    private Tablero tablero;
    
    private Reglas reglas;

    public HiloPartida(HiloCliente hiloJugador1, HiloCliente hiloJugador2) {
        this.jugador1 = hiloJugador1;
        this.jugador2 = hiloJugador2;
        this.turno = 0;
    }

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
    
    private void iniciarPartida() {
        tablero = new Tablero();
        
        
    }
    
    private synchronized void turno() {

        Movimiento movimientoLeido = leerMovimientoJugador( ( turno % 2 ) == 0 );

        if ( movimientoCorrecto(movimientoLeido) ) {

            // calcular las fichas que se han comido
            matarFichas(movimientoLeido);

            // comprobar si se ha hecho dama

            transformarDama(movimientoLeido.getFilaFinal(), movimientoLeido.getColFinal(), turno);
            
            // eliminar fichas muertas

            finTurno();

            (( turno % 2 ) == 0 ? jugador2 : jugador1).enviaMensaje("Empieza turno");

            turno++;
        } else {
            (( turno % 2 ) == 0 ? jugador1 : jugador2).enviaMensaje("Empieza turno");
        }
    }
    
    private synchronized Movimiento leerMovimientoJugador(boolean esJugador1) {
        Movimiento movimientoRecibido = null;
        
        try {
            wait();
            
            HiloCliente jugadorDelTurno = ( esJugador1 ? jugador1 : jugador2 );
            
            String movimiento = jugadorDelTurno.leerMensaje();
            String f1 = jugadorDelTurno.leerMensaje();
            String c1 = jugadorDelTurno.leerMensaje();
            String f2 = jugadorDelTurno.leerMensaje();
            String c2= jugadorDelTurno.leerMensaje();
            
            int fila1 = Integer.parseInt(f1);
            int columna1 = Integer.parseInt(c1);
            int fila2 = Integer.parseInt(f2);
            int columna2 = Integer.parseInt(c2);
            
            movimientoRecibido = new Movimiento(fila1, columna1, fila2, columna2);
            
        } catch (InterruptedException ex) {
            System.err.println("Se ha interrumpido el hilo de la partida mientras se esperaba un movimiento");
        }
        return movimientoRecibido;
    }
    
    private boolean movimientoCorrecto(Movimiento movimiento) {
        return false;
    }
    
    private void matarFichas(Movimiento movimientoRealizado) {
        int[] coordenadasFichaComida = 
                reglas.comeFicha(movimientoRealizado, tablero);
        
        tablero.matarFicha(coordenadasFichaComida[0], coordenadasFichaComida[1]);
    }
    
    private void transformarDama(int fila, int columna, int turno) {
        
        Ficha ficha = tablero.getFicha(fila, columna);
        
        if ( reglas.seTransforma(ficha, fila, tablero) ){
            tablero.cambiarADama(fila, columna);
        }
    }
    
    private void finTurno() {
        tablero.limpiarFichasMuertas();
        jugador1.enviarTableto(tablero);
        jugador2.enviarTableto(tablero);
    }
    
    
    
    
    
    
    
}