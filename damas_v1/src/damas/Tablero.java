/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import utilidades.Movimiento;

/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class Tablero {
    private final Ficha[][] casillero;
    public final int MAX_FILAS = 8;
    public final int MAX_COL = 8;
    private int numFichasJ1 = 12;
    private int numFichasJ2 = 12;
    
    private Peon fVacia = new Peon(Ficha.VACIA);
    
    public Tablero(){
        casillero = new Ficha[MAX_FILAS][MAX_COL];
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
        
        //comprueba que hay ficha
        if(casillero[fil1][col1].estaVacia()) 
            return false;
        
        ponerFicha(fil2, col2, casillero[fil1][col1]);
        quitarFicha(fil1, col1);
        
        return true;
    }
}
