/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import damas.Ficha;
import damas.MensajesConexion;
import damas.Tablero;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;
import utilidades.Movimiento;

public class HiloPartida implements Runnable{
    
    private HiloCliente jugador1;
    private HiloCliente jugador2;
    private HiloCliente jugadorTurnoActual;
    
    private int turno;
    
    private Tablero tablero;
    
    private Reglas reglas;
    
    private boolean finPartida;

    public HiloPartida(HiloCliente hiloJugador1,
            HiloCliente hiloJugador2, Reglas reglamento) {
        this.jugador1 = hiloJugador1;
        this.jugador2 = hiloJugador2;
        this.turno = 1;
        this.finPartida = false;
        this.reglas = reglamento;
    }

    @Override
    public void run() {
        retar();
    }
    
    /**
     * Método para retar un jugador a otro una vez que el primero realiza un 
     * reto, si el jugador retado lo acepta la partida continuará normalmente,
     * en caso de que no, el runnable HiloPartida no hará nada más.
     */
    private synchronized void retar() {
        try {
            jugador2.enviaMensaje(MensajesConexion.ACEPTAR_RETO);
            jugador2.enviaMensaje(jugador1.getNombreCliente());
            
            jugador1.setOyentePartida(this);
            jugador2.setOyentePartida(this);
            
            wait();
            String respuesta = jugador2.leerMensaje();
            
            if ( respuesta.equals(MensajesConexion.ACEPTAR_RETO) ) {
                jugador1.enviaMensaje(MensajesConexion.RETO_ACEPTADO);
                iniciarPartida();
            } else {
                jugador1.enviaMensaje(MensajesConexion.RETO_RECHAZADO);
                Servidor.instancia().addUsuarioDisponible(
                        jugador1.getNombreCliente());
                Servidor.instancia().addUsuarioDisponible(
                        jugador2.getNombreCliente());
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloPartida.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método que inicia el tablero y el turno para poder comenzar la partida.
     */
    private void iniciarPartida() {
        tablero = new Tablero();
        tablero.colocarFichas();
        jugadorTurnoActual = jugador2;
        turno();
    }
    
    /**
     * Método que gestiona cada turno de la partida.
     */
    private synchronized void turno() {

        try {
            Movimiento movimientoLeido = leerMovimientoJugador( 
                    ( turno % 2 ) == 0 );
            boolean valido = reglas.movimientoValido(movimientoLeido, tablero);
            if ( valido ) {

                tablero.moverFicha(movimientoLeido);

                // calcular las fichas que se han comido
                matarFichas(movimientoLeido);

                // comprobar si se ha hecho dama

                transformarDama(movimientoLeido.getFilaFinal(),
                        movimientoLeido.getColFinal(), turno);

                // eliminar fichas muertas

                finTurno();

                turno++;

                (( turno % 2 ) == 0 ? jugador1 : jugador2)
                        .enviaMensaje(MensajesConexion.EMPIEZA_TURNO);

                if ( ( turno % 2 ) == 0 )
                    jugadorTurnoActual = jugador1;
                else
                    jugadorTurnoActual = jugador2;

            //Si el turno no ha finalizado correctamente, por ejemplo, si
            //el movimiento que realizó el jugador no era válido y tiene que 
            //repetirlo hasta elegir uno válido.
            } else {
                (( turno % 2 ) == 0 ? jugador2 : jugador1).enviaMensaje(
                        MensajesConexion.EMPIEZA_TURNO);
            }
            
            comprobarFinalPartida();
            
        } catch ( NullPointerException ex ) {
            turno();
        }
    }
    
    /**
     * Método que lee el movimiento del jugador al que le corresponde el turno.
     * @param esJugador1
     * @return
     * @throws NullPointerException 
     */
    private synchronized Movimiento leerMovimientoJugador(boolean esJugador1)
            throws NullPointerException{
        
        Movimiento movimientoRecibido = null;
        
        try {
            wait();
            HiloCliente jugadorDelTurno = ( esJugador1 ? jugador1 : jugador2 );
            
            String movimientoString = jugadorTurnoActual.leerMensaje();
            if(movimientoString.equals(MensajesConexion.RENDICION)){
                rendirPartida();
            }
            String[] movimientos = movimientoString.split(" ");
            int fila1 = Integer.parseInt(movimientos[0]);
            int columna1 = Integer.parseInt(movimientos[1]);
            int fila2 = Integer.parseInt(movimientos[2]);
            int columna2 = Integer.parseInt(movimientos[3]);
            
            movimientoRecibido = new Movimiento(fila1, columna1,
                    fila2, columna2);
            
        } catch (InterruptedException ex) {
            System.err.println("Se ha interrumpido el"
                    + " hilo de la partida mientras se esperaba un movimiento");
        }
        return movimientoRecibido;
    }
    
    /**
     * Método que marcará como fichas muertas las que quedaran en ese estado
     * después del movimiento del jugador.
     * @param movimientoRealizado 
     */
    private void matarFichas(Movimiento movimientoRealizado) {
        int[] coordenadasFichaComida = 
                reglas.comeFicha(movimientoRealizado, tablero);
        
        tablero.matarFicha(coordenadasFichaComida[0], coordenadasFichaComida[1]);
    }
    
    /**
     * Método que transformará las fichas de tipo peón que deban promocionar en
     * fichas de tipo Dama.
     * @param fila
     * @param columna
     * @param turno 
     */
    private void transformarDama(int fila, int columna, int turno) {
        
        Ficha ficha = tablero.getFicha(fila, columna);
        
        if ( reglas.seTransforma(ficha, fila, tablero) ){
            tablero.cambiarADama(fila, columna);
        }
    }
    
    /**
     * Método que se ejecutará al final de cada turno, limpiando el tablero
     * de fichas muertas  y enviando el tablero a los dos jugadores.
     */
    private void finTurno() {
        tablero.limpiarFichasMuertas();
        jugador1.enviarTableto(tablero);
        jugador2.enviarTableto(tablero);
    }
    
    /**
     * Método que se ejecutará al final de cada turno, si según las reglas 
     * hay un ganador, se informará a los jugadores y se volverá a la pantalla
     * de inicio del juego, si no lo hay, se llamará al siguiente turno.
     */
    private void comprobarFinalPartida() {
        
        int ganador = reglas.hayGanador(tablero, Ficha.NEGRA);
        
        switch (ganador) {
            case Reglas.SIN_GANADOR:
                turno();
                break;
            case Reglas.GANADOR_JUGADOR_1:
                jugador1.haGanado();
                jugador2.haPerdido();
                break;
            case Reglas.GANADOR_JUGADOR_2:
                jugador2.haGanado();
                jugador1.haPerdido();
                break;
            default:
                turno();
                break;
        }
    }
    
    private void rendirPartida(){
        if ( ( turno % 2 ) == 0 ){
            jugador2.haGanado();
            jugador1.haPerdido();
        }
        else{
            jugador1.haGanado();
            jugador2.haPerdido();
        }
    }
    
}