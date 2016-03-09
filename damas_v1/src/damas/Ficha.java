/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public abstract class Ficha {
    public static String BLANCO = "O";
    public static String NEGRO = "X";
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
    
    public abstract boolean mover(Movimiento mov);
    
}
