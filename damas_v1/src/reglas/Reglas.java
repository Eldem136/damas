/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reglas;

import damas.Ficha;
import utilidades.Movimiento;
import damas.Tablero;
/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public interface Reglas {
    
    public static int[] COORDENADAS_NO_COMER = {-1, -1};
    
    /**
     * Realiza el movimiento indicado en el tablero, si es valido
     * @param mov el movimiento
     * @param tabl  el tablero
     * @return true si el movimiento es valido
     */
    public boolean mover(Movimiento mov, Tablero tabl);
    /**
     * comprueba si un movimiento es valido en el tablero
    * @param mov el movimiento
     * @param tabl  el tablero
     * @return true si el movimiento es valido
     */
    public boolean movimientoValido(Movimiento mov, Tablero tabl);
    /**
     * Comprueba si hay un ganador de la partida
     * @return true si hay ganador
     */
    public boolean hayGanador();
    /**
     * si un movimiento come una ficha la indica
     * @param movimiento movimiento realizado
     * @param tablero tablero donde se ejecuta el movimiento
     * @param colorQueCome color de la ficha que esta comiendo
     * @return la fila y columna de la ficha que se come y un array que 
     *  contiene una posicion fuera si no come ninguna ficha
     */
    public int[] comeFicha(Movimiento movimiento, Tablero tablero);
    
    public boolean seTransforma(Ficha ficha, int fila, Tablero tablero);
}
