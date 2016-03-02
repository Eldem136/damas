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
public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    private Tablero tablero;
    private Reglas reglas;
    private int turno;
    private boolean fin;
    
    public Partida(){
        
    }
    
    public boolean guardar(){
        return true;
    }
    
    public boolean cargar(){
        return true;
    }
    
    public void jugar(){
        
    }
}
