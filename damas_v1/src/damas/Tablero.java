/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import java.io.Serializable;
import utilidades.Movimiento;

/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class Tablero extends java.util.Observable implements Serializable {
    private final Ficha[][] casillero;
    private static final int MAX_FILAS = 8;
    private static final int MAX_COL = 8;
    
    private int filaMinima = 0;
    private int columnaMinima = 0;
    private int filaMaxima = MAX_FILAS - 1;
    private int columnaMaxima = MAX_COL - 1;
    
    private int numFichasJ1 = 12;
    private int numFichasJ2 = 12;
    
    private Peon fVacia = new Peon(Ficha.VACIA);
    
    public Tablero(){
        this(Tablero.MAX_FILAS, Tablero.MAX_COL);
    }
    
    public Tablero(int filas, int columnas) {
        
        casillero = new Ficha[filas][columnas];
        filaMaxima = filas - 1;
        columnaMaxima = columnas - 1;
        
    }
    
    /**
     * Coloca las fichas en el tablero siguiendo el reglamento español de las
     * damas: 12 fichas de cada color, blancas abajo, negras arriba, 
     * colocadas todas alternadas con un hueco entre medias
     */
    public void colocarFichas(){
        int x; //filas
        int y; //columnas
        
        for(x=0; x<MAX_FILAS; x++){
            for(y=0; y<MAX_COL; y++){
                if(x==3 || x==4){
                    casillero[x][y] = new Peon("·");
                }
                else if(x>=0 && x<3){
                    if(x%2==0 && y%2!=0){
                    casillero[x][y] = new Peon(Ficha.NEGRO);
                    }
                    else if(x%2!=0 && y%2==0){
                        casillero[x][y] = new Peon(Ficha.NEGRO);
                    }
                    else{
                        casillero[x][y] = new Peon("·");
                    }
                }
                else{
                    if(x%2==0 && y%2!=0){
                    casillero[x][y] = new Peon(Ficha.BLANCO);
                    }
                    else if(x%2!=0 && y%2==0){
                        casillero[x][y] = new Peon(Ficha.BLANCO);
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
     * @return 
     */
    public boolean limpiarFichasMuertas(){
        if(casillero==null){
            return false;
        }
        for(int x=0; x<MAX_FILAS; x++){
            for(int y=0; y<MAX_COL; y++){
                if(casillero[x][y].estaMuerta()){
                    casillero[x][y] = fVacia;
                }
           }
        }
        return true;
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
     * @param fila la fila
     * @param col la columna
     * @return devuelve una ficha si y solo si las coordenadas especificadas
     * están dentro de los limites del tablero. En caso contrario devuelve null
     */
    public Ficha getFicha(int fila, int col){
        if(fila >=0 && fila <MAX_FILAS && col >=0 && col <MAX_COL){
            return casillero[fila][col];
        }
        return null;
    }
    
    /**
     * Elimina una ficha del tablero, si existe
     * @param fila la fila
     * @param col la columna
     * @return true si elimina exitosamente la ficha, false si la posición 
     * esta fuera de los limites del tablero
     */
    public boolean quitarFicha(int fila, int col){
        if(fila >=0 && fila <MAX_FILAS && col >=0 && col <MAX_COL){
            casillero[fila][col] = new Peon("·");
            return true;
        }
        return false;
    }
    
    /**
     * coloca en el tablero una ficha en la posicion correspondiente
     * @param fila la fila
     * @param col la columna
     * @param ficha la ficha a colocar en el tablero
     * @return true si coloca la ficha, false si la posición esta fuera de los
     * limites del tablero
     */
    public boolean ponerFicha(int fila, int col, Ficha ficha){
        if(fila >=0 && fila <MAX_FILAS && col >=0 && col <MAX_COL){
            casillero[fila][col] = ficha;
            return true;
        }
        return false;
    }
    
    public boolean cambiarADama(int fila, int col) {
        
        //comprueba que hay ficha
        if(casillero[fila][col].estaVacia()) 
            return false;
        
        //crea la nueva dama
        String color = casillero[fila][col].getColor();
        casillero[fila][col] = new Dama(color);
        
        return true;
    }
    
    public boolean moverFicha(Movimiento mov) {
        
        int fil1 = mov.getFilaInicial();
        int col1 = mov.getColInicial();
        int fil2 = mov.getFilaFinal();
        int col2 = mov.getColFinal();
        
        int avanceFila = fil2 - fil1;
        int avanceColumna = col2 - col1;
        
        //comprueba que hay ficha
        if( casillero[fil1][col1].estaVacia() ) 
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
     * @param fila la fila de la posicion en el tablero
     * @param columna la columna de la posicion en el tablero
     * @return falso si la posicion indicada contiene una ficha o 
     *  esta fuera de los limites del tablero
     *  verdadero en caso contrario
     */
    public boolean estaLaCasillaVacia(int fila, int columna) {
        if ( ! posicionDentroTablero(fila, columna) )
            return false;
        else 
            return casillero[fila][columna].estaVacia();
    }
    
    /**
     * Retorna si la ficha es del mismo color
     * @param fila la fila de la ficha en el tablero
     * @param columna la columna de la ficha en el tablero
     * @param color el color a comprobar
     * @return falso si la ficha indicada en las coordenadas es de otro color, 
     *  esta vacia o si las coordenadas indican una posicion fuera del tablero
     *  verdadero en caso contrario
     */
    public boolean fichaDelMismoColor(int fila, int columna, String color) {
        
        if ( ! posicionDentroTablero(fila, columna) )
            return false;
        else if ( casillero[fila][columna].estaVacia() ) 
            return false;
        else
            return casillero[fila][columna].mismoColor(color);
        
    }
    
    public void matarFicha (int fila, int columna) {
        
        if ( posicionDentroTablero(fila, columna) )
            casillero[fila][columna].matar();
        
    }
        
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

    
    
}
