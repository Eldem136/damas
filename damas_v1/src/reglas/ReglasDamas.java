
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
        
        tabl.moverFicha(mov);
        
        //comprueba que la ficha debe convertirse en dama y la convierte si es necesario
        Ficha fichaFinal = tabl.getFicha(mov.getFilaFinal(), mov.getColFinal());
//        if(seHaceDama(fichaFinal, mov.getFilaFinal()))
//            tabl.cambiarADama(mov.getFilaFinal(), mov.getFilaInicial());
        
        tabl.limpiarFichasMuertas();
        
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
                    // comprueba que el movimiento comer se realiza sobre una ficha válida
                    Ficha fichaComida = tabl.getFicha((mov.getFilaInicial())+avanceFila/2, (mov.getColInicial())+avanceCol/2);
                    if(fichaComida.estaVacia())
                        return false;
                    else if(fichaComida.getColor().equals(ficha.getColor()))
                        return false;
                    else if(! fichaComida.getColor().equals(ficha.getColor()))
                        fichaComida.matar();
                }
            } else if(ficha instanceof Dama) {
                //aun no implementado
            } else
                return false;
        }

        return true;
    }

    @Override
    public boolean hayGanador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean seHaceDama(Ficha ficha, int fila, Tablero tablero) {
        
        //si es un peon
        if ( ficha.puedeTransformarse() ) {
            // si es blanca y esta en la fila superior
            if ( ficha.mismoColor(Ficha.BLANCO) && fila == tablero.getFilaMinima() )
                return true;
            // si es negra y esta en la fila inferior
            else if ( ficha.mismoColor(Ficha.NEGRO) && fila == tablero.getFilaMaxima() )
                return true;
        }
        
        return false;
    }
    
    /**
     * si un movimiento come una ficha la indica
     * @param movimiento movimiento realizado
     * @param tablero tablero donde se ejecuta el movimiento
     * @param colorQueCome color de la ficha que esta comiendo
     * @return la fila y columna de la ficha que se come y un array que 
     *  contiene {-1,-1} si no come ninguna ficha
     */
    public int[] comeFicha(Movimiento movimiento, Tablero tablero, 
            String colorQueCome) {
        
        int[] fichaComida = {-1, -1};
        
        int filaInicia = movimiento.getFilaInicial();
        int columnaInicial = movimiento.getColInicial();
        
        int avanceFila = movimiento.getFilaFinal() - filaInicia;
        int avanceColumna = movimiento.getColFinal() - columnaInicial;
        
        // si se avanza en diagonal
        if ( Math.abs(avanceFila) == Math.abs(avanceColumna) ) {
            // si se avanza una cantidad mayor a 1 casilla
            if ( Math.abs(avanceFila) >= 2 ) {
                // para todas las casillas por las que pasa
                for ( int i = 1; i < Math.abs(avanceFila); i++ ) {
                    
                    int filaInvestigada = filaInicia + 
                            ( i * (int) Math.signum(avanceFila) );
                    int columnaInvestigada = columnaInicial + 
                            ( i * (int) Math.signum(avanceColumna) );
                    
                    //si la casilla tiene una ficha enemiga retorna su posicion
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

    
    
}
