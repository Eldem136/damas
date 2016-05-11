/**
 * ReglasDamas.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
import damas.Ficha;
import java.io.Serializable;

public class ReglasDamas implements Reglas, Serializable{

    /**
     * comprueba si un movimiento es valido en el tablero
     * 
     * @param movimiento el movimiento
     * @param tablero el tablero
     * @return true si el movimiento es valido
     */
    @Override
    public boolean movimientoValido(Movimiento movimiento, Tablero tablero) {
        Ficha ficha;
        ficha = tablero.getFicha(movimiento.getFilaInicial(), movimiento.getColInicial());
        
        int filaFinal = movimiento.getFilaFinal();
        int columnaFinal = movimiento.getColFinal();
        
        if ( ficha == null )
            return false;
        /* 
        comprueba que el movimiento indicado es lógico según la ficha, 
        que el movimiento termina dentro del tablero,
        que la casilla final esta vacia
        y que salta solo fichas enemigas, y nunca mas de una de ellas por movimiento
        */
        else if ( ficha.mover(movimiento) == false )
            return false;
        else if ( ! finalizaMovimientoDentroDeTablero(filaFinal, columnaFinal, tablero) )
            return false;
        else if ( ! tablero.estaLaCasillaVacia(filaFinal, columnaFinal) )
            return false;
        else if ( ! saltoDeFichaCorrecto(movimiento, tablero) )
            return false;
        
        return true;
    }

    /**
     * retorna si hay ganador en el tablero
     * 
     * @param tablero tablero sobre el que comprueba si hay ganador
     * @param idJugador1 color del jugador 1
     * @return 
     * Reglas.GANADOR_JUGADOR_1 si gana el jugador 1
     * Reglas.GANADOR_JUGADOR_2 si gana el jugador 2
     * Reglas.SIN_GANADOR si no gana ningun jugador
     */
    @Override
    public int hayGanador(Tablero tablero, String idJugador1) {
        
        int fichasJugador1 = 0;
        int fichasJugador2 = 0;
        int x;
        int y;
        
        for ( x = tablero.getFilaMinima(); x < tablero.getFilaMaxima(); x++ ) {
            
           for( y = tablero.getColumnaMinima(); y < tablero.getColumnaMaxima(); y++){
                
                if ( tablero.fichaDelMismoColor(x, y, idJugador1) )
                    fichasJugador1++;
                else if ( ! tablero.estaLaCasillaVacia(x, y) )
                    fichasJugador2++;
                
            }
        }
        
        if ( fichasJugador2 == 0 )
            return Reglas.GANADOR_JUGADOR_1;
        else if ( fichasJugador1 == 0 )
            return Reglas.GANADOR_JUGADOR_2;
        else
            return Reglas.SIN_GANADOR;
        
    }
    
    /**
     * comprueba si la ficha en dada en la posicion indicada del tablero indicado 
     * es un peon y se puede transformar en dama
     * 
     * @param ficha la ficha que comprobamos
     * @param fila la fila en el tablero donde comprobamos la transformacion
     * @param tablero el tablero donde comprobamos la transformacion
     * @return 
     * true si la ficha se transforma
     * false si la ficha no se transforma
     */
    @Override
    public boolean seTransforma(Ficha ficha, int fila, Tablero tablero) {
        
        //si es un peon
        if ( ficha.isTransformable() ) {
            // si es blanca y esta en la fila superior
            if ( ficha.mismoColor(Ficha.BLANCA) && 
                    fila == tablero.getFilaMinima() )
                return true;
            // si es negra y esta en la fila inferior
            else if ( ficha.mismoColor(Ficha.NEGRA) && 
                    fila == tablero.getFilaMaxima() )
                return true;
        }
        
        return false;
    }
    
    /**
     * comprueba si tras realizar un movimiento se "come" una ficha enemiga e indica 
     * en tal caso cual es la ficha comida. 
     * 
     * @param movimiento movimiento realizado
     * @param tablero tablero donde se ejecuta el movimiento
     * @return 
     * un array de dimension 2 con la fila en el tablero de la ficha comida en la posicion 0 
     *  y la columna en la posicion 1
     * Reglas.COORDENADAS_NO_COMER en el caso de que no se "come" ninguna ficha
     */
    @Override
    public int[] comeFicha(Movimiento movimiento, Tablero tablero) {
        
        int[] fichaComida = Reglas.COORDENADAS_NO_COMER;
        
        int filaInicial = movimiento.getFilaInicial();
        int columnaInicial = movimiento.getColInicial();
        
        int avanceFila = movimiento.getFilaFinal() - filaInicial;
        int avanceColumna = movimiento.getColFinal() - columnaInicial;
        
        String colorQueCome = 
                tablero.getFicha(filaInicial, columnaInicial).getColor();
        
        if ( Math.abs(avanceFila) == Math.abs(avanceColumna) ) {
            if ( Math.abs(avanceFila) >= 2 ) {
                for ( int i = 1; i < Math.abs(avanceFila); i++ ) {
                    
                    int filaInvestigada = filaInicial + 
                            ( i * (int) Math.signum(avanceFila) );
                    int columnaInvestigada = columnaInicial + 
                            ( i * (int) Math.signum(avanceColumna) );
                    
                    if ( ! tablero.estaLaCasillaVacia( filaInvestigada, 
                            columnaInvestigada ) ) {
                        if ( ! tablero.fichaDelMismoColor(filaInvestigada, 
                                columnaInvestigada, colorQueCome)) {
                            fichaComida[0] = filaInvestigada;
                            fichaComida[1] = columnaInvestigada;
                            return fichaComida;
                        }
                    }
                }
            }
        }
        return fichaComida;
    }
    
    /**
     * comprueba que el movimiento termina dentro del tablero
     * 
     * @param filaFinal la fila donde termina el movimiento
     * @param columnaFinal la columna donde termina el movimiento
     * @param tablero el tablero donde se realiza
     * @return 
     * true si el movimiento termina dentro de los limites del tablero
     * false en caso contrario
     */
    private boolean finalizaMovimientoDentroDeTablero(int filaFinal, int columnaFinal, Tablero tablero) {
        
        if( filaFinal > tablero.getFilaMaxima() )
            return false;
        else if ( filaFinal < tablero.getFilaMinima() )
            return false;
        else if( columnaFinal > tablero.getColumnaMaxima() )
            return false;
        else if ( columnaFinal < tablero.getColumnaMinima() )
            return false;   

        return true;
        
    }

    /**
     * comprueba que en el caso de que se salte una ficha lo haga por encima 
     * de una sola ficha y del color contrario
     * 
     * @param movimiento el movimiento de la ficha
     * @return 
     * true si salta una unica ficha y del color contrario o ninguna
     * false si salta alguna ficha del mismo color o mas de una ficha de color contrario
     */
    private boolean saltoDeFichaCorrecto(Movimiento movimiento, Tablero tablero) {
        
        int filaInicial = movimiento.getFilaInicial();
        int columnaInicial = movimiento.getColInicial();
        int filaFinal = movimiento.getFilaFinal();
        int columnaFinal = movimiento.getColFinal();
        
        int avanceFila = filaFinal - filaInicial;
        int avanceColumna = columnaFinal - columnaInicial;
        
        boolean enemigoEncontrado = false;
        boolean movimientoCorrecto = true;
        
        String colorDeFicha = 
                tablero.getFicha(filaInicial, columnaInicial).getColor();
        
        for ( int i = 1; i < Math.abs(avanceFila) && movimientoCorrecto ; i++ ) {
                    
            int filaInvestigada = filaInicial + 
                    ( i * (int) Math.signum(avanceFila) );
            int columnaInvestigada = columnaInicial + 
                    ( i * (int) Math.signum(avanceColumna) );

            if ( ! tablero.estaLaCasillaVacia( filaInvestigada, 
                    columnaInvestigada ) ) {
                if ( ! tablero.fichaDelMismoColor(
                        filaInvestigada, columnaInvestigada, colorDeFicha)) {
                    if ( ! enemigoEncontrado ) {
                        enemigoEncontrado = true;
                    } else 
                        movimientoCorrecto = false;
                } else if ( tablero.fichaDelMismoColor(
                        filaInvestigada, columnaInvestigada, colorDeFicha)) {
                    movimientoCorrecto = false;
                }
            }
        }
        return movimientoCorrecto;
    }
    
}
