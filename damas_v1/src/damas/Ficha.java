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
public abstract class Ficha {
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
    
    public boolean mover(){
        return true;
    }
    
}
