/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v1;

import java.io.Serializable;

/**
 *
 * @author zeko
 */
public class Jugador implements Serializable{
    private String id;
    private Ficha ficha;
    
    public Jugador(String id, Ficha ficha){
        this.id = id;
        this.ficha = ficha;
    }
    
    public String getId(){
        return id;
    }
    
    public Ficha getFichaJugador(){
        return ficha;
    }
    
    
}
