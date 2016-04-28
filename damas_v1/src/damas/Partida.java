/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import reglas.Reglas;
import reglas.ReglasDamas;
import utilidades.Consola;
import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public class Partida implements Serializable{
    private Jugador jugador1;
    private Jugador jugador2;
    public Tablero tablero;
    private Reglas reglas;
    private int turno;
    private boolean fin;
    
    private transient Consola consola;
    
    public Partida(String n1, String n2, Reglas reglas){
        this.tablero = new Tablero();
        this.jugador1 = new Jugador(n1, Ficha.BLANCO);
        this.jugador2 = new Jugador(n2, Ficha.NEGRO);
        this.reglas = reglas;
        this.turno = 0;
        this.fin = false;
        
        this.tablero.colocarFichas();
        
    }
    
    public void iniciarConsola() {
        this.consola = new Consola();
    }
    
    public static boolean guardar(Partida partida) throws IOException{        
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("partidasGuardadas/j1_VS_j2.dat"));
        out.writeObject(partida);
        out.close();
        return true;
    }
    
    public static Partida cargar() throws IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("partidasGuardadas/j1_VS_j2.dat"));
        Partida partida = (Partida) in.readObject();
        in.close();
        return partida;

    }
    
    public void jugar() throws IOException{
        Movimiento movimiento;
        boolean movimientoValido;
        iniciarConsola();
        do{
            
                
                this.guardar(this);
                consola.imprimirLinea("PARTIDA GUARDADA");
            
            turno++;
            System.out.println(tablero.toString());
            
            System.out.print("TURNO DEL JUGADOR: ");
            System.out.println( ( turno % 2 == 1 ) ? "BLANCO" : "NEGRO" );
            do {
                movimiento = leerMovimiento(( turno % 2 == 1 ) ? Ficha.BLANCO : Ficha.NEGRO );
                movimientoValido = reglas.movimientoValido(movimiento, tablero);
                if(!movimientoValido){
                    consola.imprimirError("No es un movimiento valido");
                }
            }while(!movimientoValido);
            
            tablero.moverFicha(movimiento);
            
            comerFicha(movimiento);
            
            hacerDama(movimiento);
            
            tablero.limpiarFichasMuertas();
            
            
            
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
        
        if ( ! tablero.fichaDelMismoColor(coordenadasMovimiento[0], coordenadasMovimiento[1], color) ) {
            consola.imprimirError("Esa ficha no es de tu color");
            return leerMovimiento(color);
        }
            
        return new Movimiento(
            coordenadasMovimiento[0], // fila inicial
            coordenadasMovimiento[1], // columna inicial
            coordenadasMovimiento[2], // fila final
            coordenadasMovimiento[3]);// columna final
    }
    
    private void comerFicha(Movimiento movimiento) {
        
        int[] coordenadasFichaComida = 
                reglas.comeFicha(movimiento, tablero);
        
        System.err.println(coordenadasFichaComida[0]+","+coordenadasFichaComida[1]);
        tablero.matarFicha(coordenadasFichaComida[0], coordenadasFichaComida[1]);
        
    }
    
    private void hacerDama(Movimiento movimiento) {
        
        int fila = movimiento.getFilaFinal();
        int columna = movimiento.getColFinal();
        
        Ficha ficha = tablero.getFicha(fila, columna);
        
        
        if ( reglas.seTransforma(ficha, fila, tablero) )
            tablero.cambiarADama(fila, columna);
        
    }

}
