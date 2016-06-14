/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import UI.CasillaSwing;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;

/**
 *
 * @author Zeko
 */
public class AdaptadorRatonFichas extends MouseAdapter {
    CasillaSwing casillaSwingAux, ultimaPosicion, casillaOrigen, casillaFinal;
    JComponent jcomponent;
    Movimiento mov;
    @Override
    public void mousePressed(MouseEvent e) { 
        casillaOrigen = (CasillaSwing) e.getComponent();
        
    }

    

}
