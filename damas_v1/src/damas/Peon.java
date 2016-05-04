/**
 * Peon.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;
import utilidades.Movimiento;

public class Peon extends Ficha{

    /**
     * Crea un nuevo peon
     * 
     * @param color color del peon
     */
    public Peon(String color) {
        super(color, true);
    }
    
    /**
     * Comprueba si es un movimiento del tipo de ficha peon
     * 
     * @param movimiento movimiento a comprobar
     * @return 
     * true si el movimiento es valido
     */
    @Override
    public boolean mover(Movimiento movimiento){
        int avanceFila = movimiento.getFilaFinal() - movimiento.getFilaInicial();
        int avanceCol = movimiento.getColFinal() - movimiento.getColInicial();
        
        if ( Math.abs(avanceCol) == Math.abs(avanceFila) ) {
            
            if ( Math.abs(avanceFila) == 1 || Math.abs(avanceFila) == 2 ) {
                
                if ( this.getColor().equals(NEGRA) &&
                        Math.signum(avanceFila) == 1 ) {
                    return true;
                } else if ( this.getColor().equals(BLANCA) &&
                        Math.signum(avanceFila) == -1 )
                    return true;
            }
        }
        return false;
    }
    
}
