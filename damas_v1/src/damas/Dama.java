/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import utilidades.Movimiento;
/**
 *
 * @author Zeko
 */
public class Dama extends Ficha {

    public Dama(String color) {
        super(color);
    }
    @Override
    public boolean mover(Movimiento mov){
        int avanceFila = mov.getFilaFinal() - mov.getFilaInicial();
        int avanceCol = mov.getColFinal() - mov.getColInicial();
        
        //para el caso de la dama solo devuelve valido si el movimiento es diagonal
        return Math.abs(avanceFila) == Math.abs(avanceCol);
    }
    
}
