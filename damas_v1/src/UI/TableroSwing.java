/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JPanel;

/**
 *
 * @author Zeko
 */
public class TableroSwing extends JPanel {
  private static final int ALTURA_FILA = 60;
  private static final int ANCHURA_COLUMNA = 60;
  private CasillaSwing casillas[][];
  private int filas, columnas;

  public static final boolean RECIBIR_EVENTOS_RATON = true;
  public static final boolean NO_RECIBIR_EVENTOS_RATON = false;

  /**
   * PanelTablero
   */
  public TableroSwing(int filas, int columnas) {   
      
    setLayout(new GridLayout(filas, columnas));
    casillas = new CasillaSwing[filas][columnas];
    this.filas = filas;
    this.columnas = columnas;
    
    for(int fil = 0; fil < filas; fil++) 
      for(int col = 0; col < columnas; col++) {
        casillas[fil][col] = new CasillaSwing(fil, col);         
        add(casillas[fil][col]);      
        
        
        
    } 
    this.setPreferredSize(new Dimension(filas * ALTURA_FILA, 
                                        columnas * ANCHURA_COLUMNA));
  }
  
  public int getAltura(){
      return filas * ALTURA_FILA + ALTURA_FILA;
  }
  
  public int getAnchura(){
      return columnas * ANCHURA_COLUMNA + ANCHURA_COLUMNA;
  }

  /**
   * dimensionCasilla
   */  
  public Dimension dimensionCasilla() {
    return casillas[0][0].getSize();
  }
  
  /**
   * ponerIconoCasilla
   */   
  public void ponerIconoCasilla(int fil, int col, Icon icono) {     
    casillas[fil][col].setIcon(icono);
  }

  /**
   * actualiza
   */     
  private void actualiza(CasillaSwing casilla) {
  }
}
