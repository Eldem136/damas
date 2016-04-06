
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
import damas.Ficha;
import damas.Peon;
import damas.Dama;

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
        // Carga la ficha
        Ficha ficha;
        ficha = tabl.getFicha(mov.getFilaInicial(), mov.getColInicial());
        int avanceFila = mov.getFilaFinal() - mov.getFilaInicial();
        int avanceCol = mov.getColFinal() - mov.getColInicial();
        
        // comprobar que la ficha existe
        if(ficha == null){
            return false;
        }
        /* 
        * comprueba que el movimiento indicado es lógico según la ficha, 
        * es decir, su movimiento es diagonal, hacia adelante 
        * y con un recorrido adecuado
        */
        else if(ficha.mover(mov)==false){
            return false;
        }
        // comprueba que el movimiento sigue las reglas del juego
        else{
            // comprobar si el movimiento está dentro del tablero.
            if(mov.getFilaFinal()>=tabl.MAX_FILAS || mov.getFilaFinal()<0){
                return false;
            }
            if(mov.getColFinal()>=tabl.MAX_COL || mov.getColFinal()<0){
                return false;
            }
            // comprueba que se mueve a un espacio vacio
            if(! tabl.getFicha(mov.getFilaFinal(), mov.getColFinal()).estaVacia())
                return false;
            
            // En caso de que la ficha sea un peon
            if(ficha instanceof Peon){
                // se comprueba si el movimiento es de dos casillas y en caso de dos que coma una ficha rival
                
                if(Math.abs(avanceCol) == 2){
                    // comprueba que el movimiento comer se realiza sobre una ficha
                    Ficha fichaComida = tabl.getFicha((mov.getFilaInicial())+avanceFila/2, (mov.getColInicial())+avanceCol/2);
                    if(fichaComida.estaVacia())
                        return false;
                    else if(fichaComida.getColor().equals(ficha.getColor()))
                        return false;
                    else if(! fichaComida.getColor().equals(ficha.getColor()))
                        fichaComida.matar();
                }
            }
        }

        return true;
    }

    @Override
    public boolean hayGanador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
