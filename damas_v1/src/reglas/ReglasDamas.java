
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
import damas.Ficha;

/**
 *
 * @author diego
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
        if(ficha == null){
            return false;
        }
        else if(ficha.mover(mov)==false){
            return false;
        }
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
