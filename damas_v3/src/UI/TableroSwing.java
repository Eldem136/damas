/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import com.sun.prism.paint.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import utilidades.AdaptadorRatonFichas;
import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public class TableroSwing extends JPanel {
  private static final int ALTURA_FILA = 70;
  private static final int ANCHURA_COLUMNA = 70;
  private CasillaSwing casillas[][];
  private int filas, columnas;
  


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
           
        casillas[fil][col].setOpaque(true);
        if( (fil+col)%2 == 1){
            casillas[fil][col].setBackground(java.awt.Color.lightGray);
        }
        else if ((fil+col)%2 == 0){
            casillas[fil][col].setBackground(java.awt.Color.white);
        }
        
        //AÑADO EL MOUSELISTENER DE LAS CASILLAS
        AdaptadorRatonFichas listenerCasillas = new AdaptadorRatonFichas();
       
                
        casillas[fil][col].addMouseListener(listenerCasillas);
        add(casillas[fil][col]);
        
        
        
    } 
    this.setPreferredSize(new Dimension(filas * ALTURA_FILA, 
                                        columnas * ANCHURA_COLUMNA));
    setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
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
  
  public void textoEnCasilla(int fila, int columna, String text){
      
      casillas[fila][columna].setText(text);
      
  }
  
  public void addControlador(ActionListener controlador){
        for(int fil = 0; fil < filas; fil++) 
            for(int col = 0; col < columnas; col++) {
                casillas[fil][col].addActionListener(controlador);
          } 
  }
 
  
  public CasillaSwing[][] getCasillas(){
      return casillas;
  }
  
  
}
