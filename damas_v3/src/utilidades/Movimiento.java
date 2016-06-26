/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package utilidades;

import java.io.Serializable;

public class Movimiento implements Serializable{
    private int filaInicial;
    private int colInicial;
    private int filaFinal;
    private int colFinal;
    
    public static final int NUMERO_COORDENADAS_EN_MOVIMIENTO = 2;
    public static final int NUMERO_COMPONENETES_COORDENADA = 2;
    
    /**
     * 
     * @param fila1 la fila de la posicion actual de la ficha
     * @param col1 la columna de la posicion actual de la ficha
     * @param fila2 la fila de la posicion final de la ficha
     * @param col2 la columna de la posicion final de la ficha
     */
    public Movimiento ( int fila1, int col1, int fila2, int col2 ) {
        this.filaInicial = fila1;
        this.colInicial = col1;
        this.filaFinal = fila2;
        this.colFinal = col2;
    }

    /**
     * @return la fila actual
     */
    public int getFilaInicial() {
        return filaInicial;
    }

    /**
     * @return la columna actual
     */
    public int getColInicial() {
        return colInicial;
    }

    /**
     * @return la fila final
     */
    public int getFilaFinal() {
        return filaFinal;
    }

    /**
     * @return la columna final
     */
    public int getColFinal() {
        return colFinal;
    }
    
    public String toString(){
        return "("+filaInicial+","+colInicial+")"+"("+filaFinal+","+colFinal+")";
    }
    
}
