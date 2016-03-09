/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public interface Reglas {
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
}
