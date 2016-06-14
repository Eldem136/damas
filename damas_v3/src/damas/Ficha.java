/**
 * Ficha.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import java.io.Serializable;
import utilidades.Movimiento;

public abstract class Ficha implements Serializable{
    
    /* colores posibles de las fichas */
    public static String BLANCA = "O";
    public static String NEGRA = "X";
    public static String VACIA = "·";
    
    /* atributos de una ficha */
    private String color;
    private boolean muerta;
    private final boolean transformable;
    
    /**
     * Crea una nueva ficha
     * @param color el color
     * @param puedeTransformarse si la ficha puede transformarse o no
     */
    public Ficha(String color, boolean puedeTransformarse){
        this.color = color;
        this.muerta = false;
        this.transformable = puedeTransformarse;
    }
    
    /**
     * 
     * @return the color
     */
    public String getColor(){
        return this.color;
    }
    
    /**
     * indica si la ficha esta marcada como muerta antes de que se elimine
     * del tablero
     * 
     * @return 
     * true si esta muerta
     * false si no esta muerta o esta vacia
     */
    public boolean estaMuerta(){
        if(this.estaVacia()){
            return false;
        }
        return this.muerta;
    }
    
    /**
     * marca la ficha como muerta
     * 
     */
    public void matar(){
        this.muerta = true;
    }
    
    public void revivir(){
        this.muerta = false;
    }
    
    /**
     * indica si la ficha no contiene ningun color y es, por tanto, un separador
     * 
     * @return 
     * true si esta vacia
     * false si no esta vacia
     */
    public boolean estaVacia(){
        return this.color.equals(VACIA);
    }
    
    /**
     * indica si la ficha es del color indicado
     * 
     * @param color color con el que comprobar
     * @return 
     * true si el color de la ficha corresponde con el del argumento
     */
    public boolean mismoColor(String color){
        return this.color.equals(color);
    }
    
    /**
     * comprueba si un movimiento es lógico para la ficha en cuestion, 
     * sin inportar la posicion de la ficha ni la de las demas dentro 
     * del tablero
     * 
     * @param movimiento el movimiento
     * @return 
     * true si es un movimiento lógico
     * false en caso contrario
     */
    public abstract boolean mover(Movimiento movimiento);
    
    /**
     * @return the transformable
     */
    public boolean isTransformable() {
        return transformable;
    }
    
}
