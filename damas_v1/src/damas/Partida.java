/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import reglas.Reglas;
import reglas.ReglasDamas;
import utilidades.Consola;
import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    public Tablero tablero;
    private Reglas reglas;
    private int turno;
    private boolean fin;
    
    private Consola consola;
    
    public Partida(String n1, String n2, Reglas reglas){
        this.tablero = new Tablero();
        this.jugador1 = new Jugador(n1, Ficha.BLANCO);
        this.jugador2 = new Jugador(n2, Ficha.NEGRO);
        this.reglas = reglas;
        this.turno = 0;
        this.fin = false;
        this.consola = new Consola();
        
        this.tablero.colocarFichas();
        
    }
    
    public boolean guardar(){
        return true;
    }
    
    public boolean cargar(){
        return true;
    }
    
    public void jugar(){
        Movimiento movimiento;
        boolean movimientoValido;
        
        do{
            turno++;
            System.out.println(tablero.toString());
            
            System.out.print("TURNO DEL JUGADOR: ");
            System.out.println( ( turno % 2 == 1 ) ? "BLANCO" : "NEGRO" );
            do {
                movimiento = leerMovimiento(( turno % 2 == 1 ) ? Ficha.BLANCO : Ficha.NEGRO );
                movimientoValido =reglas.movimientoValido(movimiento, tablero);
                if(!movimientoValido){
                    consola.imprimirError("No es un movimiento valido");
                }
            }while(!movimientoValido);
                
            
        }while(!fin);
        
    }
    
    
    private Movimiento leerMovimiento(String color) {

        int[] coordenadasMovimiento;
        coordenadasMovimiento = new int[ Movimiento.NUMERO_COORDENADAS_EN_MOVIMIENTO * 2 ];
        int posicionEnVectorCoordenadas = 0;
        
        for ( int i = 0; i < Movimiento.NUMERO_COORDENADAS_EN_MOVIMIENTO ; i++ ) {
            int filaAuxiliar = consola.leerNumero("Introduzca la coordenada de fila");
            while ( 
                    filaAuxiliar < tablero.getFilaMinima() || 
                    filaAuxiliar > tablero.getFilaMaxima() ) {
                
                consola.imprimirLinea("Error, la fila debe estar comprendida entre " 
                        + tablero.getFilaMinima() + " y " 
                        + tablero.getFilaMaxima() + "\n");
                filaAuxiliar = consola.leerNumero("Introduzca la coordenada de fila");
            }
            
            coordenadasMovimiento[posicionEnVectorCoordenadas++] = filaAuxiliar;
            
            int columnaAuxiliar = consola.leerNumero("Introduzca la coordenada de columna");
            while ( 
                    columnaAuxiliar < tablero.getColumnaMinima()|| 
                    columnaAuxiliar > tablero.getColumnaMaxima()) {
                
                consola.imprimirLinea("Error, la columna debe estar comprendida entre " 
                        + tablero.getColumnaMinima()+ " y " 
                        + tablero.getColumnaMaxima()+ "\n");
                columnaAuxiliar = consola.leerNumero("Introduzca la coordenada de columna");
            }
            
            coordenadasMovimiento[posicionEnVectorCoordenadas++] = columnaAuxiliar;
        }
            
        return new Movimiento(
            coordenadasMovimiento[0], // fila inicial
            coordenadasMovimiento[1], // columna inicial
            coordenadasMovimiento[2], // fila final
            coordenadasMovimiento[3]);// columna final
    }

}
