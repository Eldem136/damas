/**
 * Tablero.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import java.io.Serializable;
import utilidades.Movimiento;

public class Tablero extends java.util.Observable implements Serializable {
    private final Ficha[][] casillero;
    private static final int MAX_FILAS = 8;
    private static final int MAX_COL = 8;
    
    private int filaMinima;
    private int columnaMinima;
    private int filaMaxima;
    private int columnaMaxima;
    
    private final Peon fVacia = new Peon(Ficha.VACIA);
    
    /**
     * crea un nuevo tablero con el numero de filas y columnas por defecto
     */
    public Tablero(){
        this(Tablero.MAX_FILAS, Tablero.MAX_COL);
    }
    
    /**
     * crea un tablero con un numero de filas y columnas especificio
     * 
     * @param filas numero de filas
     * @param columnas numero de columnas
     */
    public Tablero(int filas, int columnas) {
        this.columnaMinima = 0;
        this.filaMinima = 0;
        
        casillero = new Ficha[filas][columnas];
        filaMaxima = filas - 1;
        columnaMaxima = columnas - 1;
        
    }
    
    /**
     * Coloca las fichas para un juego de damas.
     * Coloca en las tres filas superiores las fichas del jugador 2 que seran de color negro
     * Coloca en las tres filas inferiores las fichas del jugador 1 que seran de color blanco
     */
    public void colocarFichas(){
        int x; //filas
        int y; //columnas
        
        for ( x = filaMinima; x <= filaMaxima; x++ ) {
            for ( y = columnaMinima; y <= columnaMaxima; y++ ) {
                if ( x == 3 || x == 4 ) {
                    casillero[x][y] = new Peon("·");
                }
                else if ( x >= 0 && x < 3 ) {
                    if ( x % 2 == 0 && y % 2 != 0 ) {
                    casillero[x][y] = new Peon(Ficha.NEGRA);
                    }
                    else if ( x % 2 != 0 && y % 2 == 0 ) {
                        casillero[x][y] = new Peon(Ficha.NEGRA);
                    }
                    else{
                        casillero[x][y] = new Peon("·");
                    }
                }
                else{
                    if ( x % 2 == 0 && y % 2 != 0 ) {
                    casillero[x][y] = new Peon(Ficha.BLANCA);
                    }
                    else if ( x % 2 != 0 && y % 2 == 0 ) {
                        casillero[x][y] = new Peon(Ficha.BLANCA);
                    }
                    else{
                        casillero[x][y] = new Peon("·");
                    }                  
                }  
            }
        }
    }
    
    /**
     * Elimina del tablero todas las fichas que se han marcado como muertas
     */
    public void limpiarFichasMuertas(){
        if ( casillero != null )
            for ( int x = 0; x < MAX_FILAS; x++ ) {
                for ( int y = 0; y < MAX_COL; y++ ) {
                    if ( casillero[x][y].estaMuerta() ) {
                        casillero[x][y] = fVacia;
                    }
               }
            }
    }
    
    @Override
    public String toString(){
        String tablero = "_";
        char[] letras = {'0', '1','2','3','4','5','6','7'};
        
        for(int i=0; i<MAX_COL; i++){
            tablero+= "| "+letras[i]+" ";
        }
        tablero += "|\n";
        tablero += "----------------------------------\n";
        for(int i=0; i<MAX_FILAS; i++){
            tablero += (i)+"|";
            for(int j=0; j<MAX_COL; j++){
                Ficha fichaAImprimir = casillero[i][j];
                if (fichaAImprimir instanceof Peon)
                    tablero += " " + casillero[i][j].getColor() + " |";
                else if (fichaAImprimir instanceof Dama) 
                    tablero += " \033[1m" + casillero[i][j].getColor() + "\033[0m |";
            }
            tablero += "\n";
        }
        return tablero;
    }
    
    /**
     * Obtiene la ficha situada en la fila y colunma indicadas
     * 
     * @param fila la fila
     * @param columna la columna
     * @return 
     * la ficha si las fichas estan dentro de las coordenadas del tablero
     * null si las coordenadas indicadas no corresponden a una posicion dentro del tablero
     */
    public Ficha getFicha(int fila, int columna){
        if ( posicionDentroTablero(fila, columna) )
            return casillero[fila][columna];
        else
            return null;
    }
    
    /**
     * Elimina una ficha del tablero, si existe
     * 
     * @param fila la fila
     * @param columna la columna
     * @return true si elimina exitosamente la ficha, false si la posición 
     * esta fuera de los limites del tablero
     */
    public boolean quitarFicha(int fila, int columna){
        if ( posicionDentroTablero(fila, columna) ) {
            casillero[fila][columna] = new Peon("·");
            return true;
        }
        return false;
    }
    
    /**
     * coloca en el tablero una ficha en la posicion correspondiente
     * 
     * @param fila la fila
     * @param columna la columna
     * @param ficha la ficha a colocar en el tablero
     * @return 
     * true si coloca la ficha
     * false si la posición esta fuera de los limites del tablero
     */
    public boolean ponerFicha(int fila, int columna, Ficha ficha){
        if ( posicionDentroTablero(fila, columna) ) {
            casillero[fila][columna] = ficha;
            return true;
        }
        return false;
    }
    
    /**
     * cambia a dama la ficha de la fila y la columna indicadas
     * 
     * @param fila la fila
     * @param columna la columna
     */
    public void cambiarADama(int fila, int columna) {
        
        if ( casillero[fila][columna].estaVacia() ) {
            //crea la nueva dama
            String color = casillero[fila][columna].getColor();
            casillero[fila][columna] = new Dama(color);
        }
        
    }
    
    /**
     * mueve la ficha siguiendo el movimiento indicado
     * 
     * @param movimiento el movimiento que sigue la ficha
     * @return 
     * true si mueve la ficha sin problemas
     * false si intentamos mover una ficha vacia o intentamos mover a una posicion ocupada
     */
    public boolean moverFicha(Movimiento movimiento) {
        
        int fil1 = movimiento.getFilaInicial();
        int col1 = movimiento.getColInicial();
        int fil2 = movimiento.getFilaFinal();
        int col2 = movimiento.getColFinal();
        
        //comprueba que hay ficha
        if ( casillero[fil1][col1].estaVacia() ) 
            return false;
        
        //comprobamos que la posicion final no esta ocupada ya
        if ( ! casillero[fil2][col2].estaVacia() )
            return false;
        
        ponerFicha(fil2, col2, casillero[fil1][col1]);
        quitarFicha(fil1, col1);
        
        return true;
    }

    /**
     * Retorna si la posicion indicada esta vacia
     * 
     * @param fila la fila de la posicion en el tablero
     * @param columna la columna de la posicion en el tablero
     * @return 
     * true si la posicion tiene una ficha
     * false si la posicion esta fuera de los limites o tiene una ficha
     */
    public boolean estaLaCasillaVacia(int fila, int columna) {
        if ( ! posicionDentroTablero(fila, columna) )
            return false;
        else 
            return casillero[fila][columna].estaVacia();
    }
    
    /**
     * Retorna si la ficha es del mismo color
     * 
     * @param fila la fila de la ficha en el tablero
     * @param columna la columna de la ficha en el tablero
     * @param color el color a comprobar
     * @return
     * true si la ficha indicada por las coordenadas es del color indicado
     * false si la ficha indicada por las coordenadas es de otro color, esta vacia 
     *  o se indican coordenadas fuera del tablero
     */
    public boolean fichaDelMismoColor(int fila, int columna, String color) {
        
        if ( ! posicionDentroTablero(fila, columna) )
            return false;
        else if ( casillero[fila][columna].estaVacia() ) 
            return false;
        else
            return casillero[fila][columna].mismoColor(color);
        
    }
    
    /**
     * Mata la ficha indicada por las coordenadas
     * @param fila la fila
     * @param columna la columna
     */
    public void matarFicha (int fila, int columna) {
        
        if ( posicionDentroTablero(fila, columna) )
            casillero[fila][columna].matar();
        
    }
        
    /**
     * Comprueba que la fila y la columna se encuentran dentro de los limites del tablero
     * 
     * @param fila la fila
     * @param columna la columna
     * @return 
     * true si las coordenadas pertenecen al tablero
     * false si las coordenadas no pertenecen al tablero
     */
    private boolean posicionDentroTablero(int fila, int columna) {
        return ( fila >= this.filaMinima &&
                fila <= this.filaMaxima &&
                columna >= this.columnaMinima &&
                columna <= this.columnaMaxima );
    }
    
    /**
     * @return the filaMinima
     */
    public int getFilaMinima() {
        return filaMinima;
    }

    /**
     * @return the columnaMinima
     */
    public int getColumnaMinima() {
        return columnaMinima;
    }

    /**
     * @return the filaMaxima
     */
    public int getFilaMaxima() {
        return filaMaxima;
    }

    /**
     * @return the columnaMaxima
     */
    public int getColumnaMaxima() {
        return columnaMaxima;
    }
    
    /**
     * @return the casillero
     */
    public Ficha[][] getCasillero(){
        return this.casillero;
    }

    
    
}
