
package reglas;

import utilidades.Movimiento;
import damas.Tablero;
import damas.Ficha;
import damas.Peon;
import damas.Dama;
import java.io.Serializable;

/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class ReglasDamas implements Reglas, Serializable{
    
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
        
        int filaInicial = mov.getFilaInicial();
        int columnaInicial = mov.getColInicial();
        int filaFinal = mov.getFilaFinal();
        int columnaFinal = mov.getColFinal();
        
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
            if( filaFinal > tabl.getFilaMaxima() || filaFinal < tabl.getFilaMinima() ){
                return false;
            }
            if( columnaFinal > tabl.getColumnaMaxima()|| columnaFinal < tabl.getColumnaMinima() ){
                return false;
            }
            // comprueba que se mueve a un espacio vacio
            if ( ! tabl.estaLaCasillaVacia(filaFinal, columnaFinal) )
                return false;
            
            // se comprueba si el movimiento es de dos casillas y en caso de dos que coma una ficha rival

            if ( ! SaltoDeFichaCorrecto(mov, tabl) ) 
                return false;
        }

        return true;
    }

    @Override
    public boolean hayGanador() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean seTransforma(Ficha ficha, int fila, Tablero tablero) {
        
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
    
    @Override
    public int[] comeFicha(Movimiento movimiento, Tablero tablero) {
        
        int[] fichaComida = 
            {tablero.getFilaMinima()-1, tablero.getColumnaMinima()-1};
        
        int filaInicial = movimiento.getFilaInicial();
        int columnaInicial = movimiento.getColInicial();
        
        int avanceFila = movimiento.getFilaFinal() - filaInicial;
        int avanceColumna = movimiento.getColFinal() - columnaInicial;
        
        String colorQueCome = 
                tablero.getFicha(filaInicial, columnaInicial).getColor();
        
        // si se avanza en diagonal
        if ( Math.abs(avanceFila) == Math.abs(avanceColumna) ) {
            // si se avanza una cantidad mayor a 1 casilla
            if ( Math.abs(avanceFila) >= 2 ) {
                // para todas las casillas por las que pasa
                for ( int i = 1; i < Math.abs(avanceFila); i++ ) {
                    
                    int filaInvestigada = filaInicial + 
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

    /**
     * comprueba que en el caso de que se salte una ficha lo haga por encima 
     * de una sola ficha y del color contrario
     * @param movimiento el movimiento de la ficha
     * @return true si salta una unica ficha y del color contrario o ninguna
     */
    private boolean SaltoDeFichaCorrecto(Movimiento movimiento, Tablero tablero) {
        
        int filaInicial = movimiento.getFilaInicial();
        int columnaInicial = movimiento.getColInicial();
        int filaFinal = movimiento.getFilaFinal();
        int columnaFinal = movimiento.getColFinal();
        
        int avanceFila = filaFinal - filaInicial;
        int avanceColumna = columnaFinal - columnaInicial;
        
        boolean enemigoEncontrado = false;
        boolean movimientoCorrecto = true;
        
        String colorDeFicha = 
                tablero.getFicha(filaInicial, columnaInicial).getColor();
        
        for ( int i = 1; i < Math.abs(avanceFila) && movimientoCorrecto ; i++ ) {
                    
            int filaInvestigada = filaInicial + 
                    ( i * (int) Math.signum(avanceFila) );
            int columnaInvestigada = columnaInicial + 
                    ( i * (int) Math.signum(avanceColumna) );

            if ( ! tablero.estaLaCasillaVacia( filaInvestigada, 
                    columnaInvestigada ) ) {
                if ( ! tablero.fichaDelMismoColor(filaInvestigada, columnaInvestigada, colorDeFicha)) {
                    if ( ! enemigoEncontrado ) {
                        enemigoEncontrado = true;
                    } else 
                        movimientoCorrecto = false;
                } else if ( tablero.fichaDelMismoColor(filaInvestigada, columnaInvestigada, colorDeFicha)) {
                    movimientoCorrecto = false;
                }
            }
        }
        return movimientoCorrecto;
    }
    
}
