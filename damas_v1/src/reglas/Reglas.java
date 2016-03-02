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
 * @author diego
 */
public interface Reglas {
    public boolean mover(Movimiento mov, Tablero tabl);
    public boolean movimientoValido(Movimiento mov, Tablero tabl);
    public boolean hayGanador();
}
