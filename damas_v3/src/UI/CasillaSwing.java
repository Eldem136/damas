/**
 * CasillaSwing.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package UI;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class CasillaSwing extends JButton {
    private int fila, columna; 
    private String actionCommand;
    
    /**
     * Constructor completo de una casilla en la interfaz grafica
     * @param fila la fila en el tablero
     * @param columna la columna en el tablero
     * @param icon el icono de la ficha contenida
     */
    private CasillaSwing(int fila, int columna, Icon icon) {
        this.fila = fila;
        this.columna = columna;
        this.actionCommand = "casilla";
        setIcon(icon);

        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }
    
    /**
     * Constructor para una casilla vacia
     * @param fila la fila en el tablero
     * @param columna la columna en el tablero
     */
    public CasillaSwing(int fila, int columna) {
        this(fila, columna, null);
    }
    
    /**
     * @return la columna
     */
    public int getColumna() {
        return columna;
    }
  
    /**
     * @return la fila
     */
    public int getFila() {
        return fila;
    }
    
    /**
     * Sobreescribe el metodo toString
     * @return representacion en string de una CasillaSwing
     */
    @Override
    public String toString() {
        return "Casilla{" + "fil=" + fila + ", col=" + columna + '}';
    }
  
}
