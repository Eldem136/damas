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
public abstract class Ficha {
    public static String BLANCO = "O";
    public static String NEGRO = "X";
    public static String VACIA = "·";
    private String color;
    private boolean muerta;
    
    public Ficha(String color){
        this.color = color;
        this.muerta = false;
    }
    
    public String getColor(){
        return this.color;
    }
    
    public boolean estaMuerta(){
        return this.muerta;
    }
    
    public boolean matar(){
        this.muerta = true;
        return true;
    }
    
    public boolean estaVacia(){
        return this.color.equals(VACIA);
    }
    
    /**
     * comprueba si un movimiento es lógico para la ficha en cuestion, 
     * sin inportar la posicion de la ficha ni la de las demas dentro 
     * del tablero
     * @param mov movimiento
     * @return true si es un movimiento lógico, false en caso contrario
     */
    public abstract boolean mover(Movimiento mov);
    
}
