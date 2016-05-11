/**
 * damas.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;
import utilidades.Movimiento;

public class Dama extends Ficha {

    /**
     * Crea una nueva ficha de tipo Dama
     * @param color de la dama
     */
    public Dama(String color) { 
        super(color, false);
    }
    
    /**
     * Comprueba si es un movimiento del tipo de ficha dama
     * 
     * @param movimiento movimiento a comprobar
     * @return 
     * true si el movimiento es valido
     */
    @Override
    public boolean mover(Movimiento movimiento){
        int avanceFila = movimiento.getFilaFinal() - movimiento.getFilaInicial();
        int avanceCol = movimiento.getColFinal() - movimiento.getColInicial();
        
        return Math.abs(avanceFila) == Math.abs(avanceCol);
    }
    
}