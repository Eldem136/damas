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
 * @author diego
 */
public class damas_v1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Reglas reglas = new ReglasDamas();
        System.out.println("hola diegooo punto y coma \n");
        Tablero tablero = new Tablero();
        tablero.colocarFichas();
        
        System.out.println(tablero);
        
        System.out.println(".............................");
        
        reglas.mover(new Movimiento(5, 0, 4, 1), tablero);
        System.out.println(tablero);
        reglas.mover(new Movimiento(7, 0, 8, 1), tablero);
        System.out.println(tablero);
        reglas.mover(new Movimiento(5, 2, 4, 1), tablero);
        System.out.println(tablero);
        reglas.mover(new Movimiento(4, 1, 3, 1), tablero);
        System.out.println(tablero);
    }
    
}
