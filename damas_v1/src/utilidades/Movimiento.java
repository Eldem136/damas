/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

/**
 *
 * @author diego
 */
public class Movimiento {
    private int filaInicial;
    private int colInicial;
    private int filaFinal;
    private int colFinal;
    
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
    
    
}
