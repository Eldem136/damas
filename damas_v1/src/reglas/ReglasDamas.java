
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
import damas.Ficha;

/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class ReglasDamas implements Reglas {
    
    public ReglasDamas (){
        
    }

    @Override
    public boolean mover(Movimiento mov, Tablero tabl) {
        Ficha ficha;
        ficha = tabl.getFicha(mov.getFilaInicial(), mov.getColInicial());
        
        if(! movimientoValido(mov, tabl))
            return false;
        
        tabl.ponerFicha(mov.getFilaFinal(), mov.getColFinal(), ficha);
        tabl.quitarFicha(mov.getFilaInicial(), mov.getColInicial());
        
        return true;
    }

    @Override
    public boolean movimientoValido(Movimiento mov, Tablero tabl) {
        Ficha ficha;
        ficha = tabl.getFicha(mov.getFilaInicial(), mov.getColInicial());
        
        // comprobar que la ficha existe
        if(ficha == null){
            return false;
        }
        // comprueba que el movimiento indicado es lógico
        else if(ficha.mover(mov)==false){
            return false;
        }
        // comprueba que el movimiento sigue las reglas del juego
        else{
            //comprobar si el movimiento está dentro del tablero.
            if(mov.getFilaFinal()>=tabl.MAX_FILAS || mov.getFilaFinal()<0){
                return false;
            }
            if(mov.getColFinal()>=tabl.MAX_COL || mov.getColFinal()<0){
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean hayGanador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
