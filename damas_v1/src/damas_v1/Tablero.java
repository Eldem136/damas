/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v1;

import java.io.Serializable;
import static java.lang.Thread.sleep;

/**
 *
 * @author zeko
 */
public class Tablero extends java.util.Observable implements Serializable{
    private int posicion;
    private int idTablero;
    private Ficha[][] casillero;
    private static Ficha cVacia = new Ficha(".");
    private final int MAXFILAS = 8;
    private final int MAXCOLUMNAS = 10;
    
    
    
    
    public Tablero(int id){
        this.idTablero = id;
        
        casillero = new Ficha[MAXFILAS][MAXCOLUMNAS];
        for(int i=0; i<MAXFILAS; i++){
            for(int j=0; j<MAXCOLUMNAS; j++){
                casillero[i][j] = cVacia;
            }
        }
    }
    
    
    public CodigoError movimientoLegal(int columna){
        /*El método columnaLLena(int columna) devuelve True si la columna en la
        que se pretendría introducir la ficha está llena.
        Devuelve falso en caso de que no esté llena y se pueda introducir otra 
        ficha.
        */
        CodigoError error = null;
        if(!(1<=columna && columna<=MAXCOLUMNAS)){
            return error.COLUMNA_NO_VALIDA;
        }
        else if(!casillero[0][columna-1].equals(cVacia)){
            return error.COLUMNA_LLENA;
        }
        else{
            return error.NO_ERROR;
                    }
        }
    
    public void introducirFicha(int columna, Ficha ficha){
        /*El método introducirFicha(int columna, Ficha ficha) coloca la ficha
        a introducir en el primer espacio vacío de la columna del casillero, de
        abajo hacia arriba.
        */
        
        for(int i=(MAXFILAS -1); i>-1; i--){
            if(casillero[i][columna-1].equals(cVacia)){
                casillero[i][columna-1] = ficha;
                casillero[i][columna-1].setPosicion(i*10+(columna-1));
                setChanged();
                Ficha f = casillero[i][columna-1];
                notifyObservers(f);
                break;
            }
        }
    }
    
    public void cargar(){
        for(int fila=0; fila<MAXFILAS; fila++){
            for(int columna=0; columna<MAXCOLUMNAS; columna++){
                Ficha f = casillero[fila][columna];
                System.out.println("fila:"+fila+"__columna:"+columna);
                f.setPosicion(fila*10+columna);
                System.out.println("posicion:"+fila*10+columna);
                setChanged();
                notifyObservers(f);
            }
        }
        
    }
    
    
    
    public boolean comprobar4enRaya(){
        /*El métoddo comprobarOrtogonal comprobará, para cada casilla, si las
        adyacentes, en vertical u horizontal forman un cuatro en linea respecto
        a la ficha en la casilla de referencia.
        */
        Ficha ficha;
        /*comprobación de cuatro en linea horizontal*/
        for(int i=0; i<MAXFILAS; i++){
            for(int j=0; j<MAXCOLUMNAS; j++){
                ficha = casillero[i][j];
                if(!ficha.equals(cVacia)){
                    if(j+3 < MAXCOLUMNAS){
                        if(casillero[i][j+1].equals(ficha) && 
                           casillero[i][j+2].equals(ficha) &&
                           casillero[i][j+3].equals(ficha)){
                            return true;
                        }
                    }
                }
            }
        }
        /*comprobación de cuatro en linea en vertical*/
        for(int j=0; j<MAXCOLUMNAS; j++){
            for(int i=0; i<MAXFILAS; i++){
                ficha = casillero[i][j];
                if(!ficha.equals(cVacia)){
                    if(i+3 < MAXFILAS){
                        if(casillero[i+1][j].equals(ficha) &&
                           casillero[i+2][j].equals(ficha) &&
                           casillero[i+3][j].equals(ficha)){
                            return true;
                        }
                    }
                }
            }
        }
        
         /*El método comprobarDiagonal comprobará, para cada casilla, si las
        diagonales, en sentido ascendente o descendente forman un cuatro en
        línea respecto a la ficha en la casilla de referencia.
        */
        /*comprobación de cuatro en linea en diagonal ascendente*/
        for(int i=3; i<MAXFILAS; i++){
            for(int j=0; j<MAXCOLUMNAS; j++){
                ficha=casillero[i][j];
                if(!ficha.equals(cVacia)){
                    if((i-3)>0 && (j+3)<MAXCOLUMNAS){
                        if(casillero[i-1][j+1].equals(ficha) &&
                           casillero[i-2][j+2].equals(ficha) &&
                           casillero[i-3][j+3].equals(ficha)){
                            return true;
                        }
                    }
                }
            }
        }
        
          /*comprobacion de cuatro en linea en diagonal descendente*/
        for(int i=0; i<7; i++){
            for(int j=0; j<MAXCOLUMNAS; j++){
                ficha=casillero[i][j];
                if(!ficha.equals(cVacia)){
                    if((i+3)<MAXFILAS && (j+3)<MAXCOLUMNAS){
                        if(casillero[i+1][j+1].equals(ficha) &&
                           casillero[i+2][j+2].equals(ficha) &&
                           casillero[i+3][j+3].equals(ficha)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public void reiniciar() throws InterruptedException{
        Ficha f = new Ficha("·");
        
        for(int i=0; i<MAXFILAS; i++){
            for(int j=0; j<MAXCOLUMNAS; j++){
                casillero[i][j] = f;
                setChanged();
                
            }
        }
        
    }
   
    
    public void mostrarTablero(){
        System.out.print("||-1--2--3--4--5--6--7--8--9--10||\n");
        for(int i=0; i<MAXFILAS; i++){
            System.out.print("||");
            for(int j=0; j<MAXCOLUMNAS; j++){
                System.out.print(" "+casillero[i][j].getColor()+" ");
            }
            System.out.print("||\n");
        }
        System.out.print("----------------------------------\n");
    }
    
}
