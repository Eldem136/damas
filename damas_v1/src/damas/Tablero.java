/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

/**
 *
 * @author Zeko
 */
public class Tablero {
    private final Ficha[][] casillero;
    private final int MAX_FILAS = 8;
    private final int MAX_COL = 8;
    private int numFichasJ1 = 12;
    private int numFichasJ2 = 12;
    
    public Tablero(){
        casillero = new Ficha[MAX_FILAS][MAX_COL];
    }    
    
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
                    casillero[x][y] = new Peon("X");
                    }
                    else if(x%2!=0 && y%2==0){
                        casillero[x][y] = new Peon("X");
                    }
                    else{
                        casillero[x][y] = new Peon("·");
                    }
                }
                else{
                    if(x%2==0 && y%2!=0){
                    casillero[x][y] = new Peon("O");
                    }
                    else if(x%2!=0 && y%2==0){
                        casillero[x][y] = new Peon("O");
                    }
                    else{
                        casillero[x][y] = new Peon("·");
                    }                  
                }  
            }
        }
    }
    
    public boolean limpiarFichasMuertas(){
        return true;
    }
    
    @Override
    public String toString(){
        String tablero = "";
        String cabecero = "_";
        char[] letras = {'A', 'B','C','D','E','F','G','H'};
        
        for(int i=0; i<MAX_COL; i++){
            cabecero+= "| "+letras[i]+" ";
        }
        cabecero += "|\n";
        cabecero += "----------------------------------";
        System.out.println(cabecero);
        for(int i=0; i<MAX_FILAS; i++){
            tablero += (8-i)+"|";
            for(int j=0; j<MAX_COL; j++){
                tablero += " " + casillero[i][j].getColor() + " |";
            }
            tablero += "\n";
        }
        return tablero;
    }
}
