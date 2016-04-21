/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import reglas.*;
import utilidades.Movimiento;
/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class damas_v1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Reglas reglas = new ReglasDamas();
        
        Partida p = new Partida("pepito", "josito", reglas);
       //p.jugar();
        
//        System.out.println("hola diegooo punto y coma \n");
//        Tablero tablero = new Tablero();
//        tablero.colocarFichas();
//        
//        System.out.println(tablero);
//        
//        System.out.println(".............................");
//        
//            //Test Dama
        p.tablero.quitarFicha(1, 0);
        p.tablero.quitarFicha(0, 1);
        p.tablero.ponerFicha(1, 0, new Peon(Ficha.BLANCO));
        System.out.println("Prueba de convertir a dama\nAntes");
        System.out.println(p.tablero);
        reglas.mover(new Movimiento(1, 0, 0 ,1), p.tablero);
        System.out.println("Despues\n"+p.tablero);
        p.jugar();
        
        
//        System.out.println("movemos 5,0 a 4,1");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(5, 0, 4, 1), tablero));
//        System.out.println(tablero);
//        System.out.println("movemos 7,0 a 8,1");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(7, 0, 8, 1), tablero));
//        System.out.println(tablero);
//        System.out.println("movemos 5,2 a 4,1");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(5, 2, 4, 1), tablero));
//        System.out.println(tablero);
//        System.out.println("movemos 4,1 a 3,1");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(4, 1, 3, 1), tablero));
//        System.out.println(tablero);
//        
//        System.out.println("Test matar:");
//        System.out.println("movemos 2,1 a 3,2");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(2, 1, 3, 2), tablero));
//        System.out.println(tablero);
//        System.out.println("movemos 3,2 a 5,0");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(3, 2, 5, 0), tablero));
//        System.out.println(tablero);
//        System.out.println("movemos 2,5 a 4,3");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(2,5,4,3), tablero));
//        System.out.println(tablero);
//        
//        System.out.println("movemos 4,1 a 3,2");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(4,1,3,2), tablero));
//        System.out.println(tablero);
//        
//        System.out.println("prueba matar");
//        System.out.println("movemos 2,3 a 4,1");
//        System.out.println("movemos: " + reglas.mover(new Movimiento(2,3,4,1), tablero));
//        System.out.println(tablero);
        
    
        
    }
    
}
