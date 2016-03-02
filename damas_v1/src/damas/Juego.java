/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import reglas.Reglas;

/**
 *
 * @author Zeko
 */
public class Juego {
    private Partida[] partidas;
    private Reglas reglas;
    
    public Juego(){
        
    }
    
    public boolean nuevaPartida(){
        return true;
    }
    
    public boolean cargarPartida(){
        return true;
    }
    
    public boolean guardarPartida(){
        return true;
    }
}

