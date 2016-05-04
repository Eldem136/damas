/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import java.io.Serializable;

/**
 *
 * @author Zeko
 */
public class Jugador implements Serializable{
    private String nombre;
    private String colorFicha;
    
    public Jugador(String nombre, String color){
        this.nombre = nombre;
        this.colorFicha = color;
        
    }
    public String getNombre(){
        return this.nombre;
    }
    
    public String getFicha(){
        return this.colorFicha;
    }
    
}
