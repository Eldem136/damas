/**
 * AdaptadorRatonFichas.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
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
    
    /**
     * Sobreescribe el metodo mousePressed para guardar la casilla pulsada
     * @param e evento de raton entrante
     */
    @Override
    public void mousePressed(MouseEvent e) { 
        casillaOrigen = (CasillaSwing) e.getComponent();
        
    }

    

}
