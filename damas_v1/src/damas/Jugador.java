/**
 * Jugador.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import java.io.Serializable;

public class Jugador implements Serializable{
    private final String nombre;
    
    /* color de las fichas del jugador */
    private final String colorFicha;
    
    /**
     * Crea un nuevo jugador
     * 
     * @param nombre el nombre
     * @param color el color de sus fichas
     */
    public Jugador(String nombre, String color){
        this.nombre = nombre;
        this.colorFicha = color;
    }
    
    /**
     * @return el nombre del jugador
     */
    public String getNombre(){
        return this.nombre;
    }
    
    /**
     * @return el color asignado al jugador
     */
    public String getColorFicha(){
        return this.colorFicha;
    }
    
}
