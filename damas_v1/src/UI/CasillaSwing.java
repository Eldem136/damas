/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Zeko
 */
public class CasillaSwing extends JLabel {
    private int fila, columna; 
    
    private CasillaSwing(int fila, int columna, Icon icon) {
    this.fila = fila;
    this.columna = columna;
    setIcon(icon);
        
    setHorizontalAlignment(SwingConstants.CENTER);
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
  }
    
    public CasillaSwing(int fila, int columna) {
    this(fila, columna, null);
  }
    
    public int getColumna() {
    return columna;
  }
  
  public int getFila() {
    return fila;
  }
    
  @Override
  public String toString() {
    return "Casilla{" + "fil=" + fila + ", col=" + columna + '}';
  }
  
}
