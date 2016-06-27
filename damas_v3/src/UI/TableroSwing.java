/**
 * TableroSwing.java
 * @author Ezequiel Barbudo (zeko3991@gmail.com)
 * @author Diego Malo (d.malo136@gmail.com)
 */
package UI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import utilidades.AdaptadorRatonFichas;

public class TableroSwing extends JPanel {

    private static final int ALTURA_FILA = 70;
    private static final int ANCHURA_COLUMNA = 70;
    private CasillaSwing casillas[][];
    private int filas, columnas;

    /**
     * PanelTablero
     * @param filas el total de filas que tiene el tablero
     * @param columnas el total de columnas que tiene el tablero
     */
    public TableroSwing(int filas, int columnas) {

        setLayout(new GridLayout(filas, columnas));

        casillas = new CasillaSwing[filas][columnas];
        this.filas = filas;
        this.columnas = columnas;

        for (int fil = 0; fil < filas; fil++) {
            for (int col = 0; col < columnas; col++) {
                casillas[fil][col] = new CasillaSwing(fil, col);

                casillas[fil][col].setOpaque(true);
                if ( (fil + col) % 2 == 1 ) {
                    casillas[fil][col].setBackground(java.awt.Color.lightGray);
                } else if ( (fil + col) % 2 == 0 ) {
                    casillas[fil][col].setBackground(java.awt.Color.white);
                }

                //AÑADO EL MOUSELISTENER DE LAS CASILLAS
                AdaptadorRatonFichas listenerCasillas = new AdaptadorRatonFichas();

                casillas[fil][col].addMouseListener(listenerCasillas);
                add(casillas[fil][col]);

            }
        }
        this.setPreferredSize(new Dimension(filas * ALTURA_FILA,
                columnas * ANCHURA_COLUMNA));
        setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }

    /**
     * @return la altura del tablero
     */
    public int getAltura() {
        return filas * ALTURA_FILA + ALTURA_FILA;
    }

    /**
     * @return la altura del tablero
     */
    public int getAnchura() {
        return columnas * ANCHURA_COLUMNA + ANCHURA_COLUMNA;
    }

    /**
     * @return la dimension de una casilla
     */  
    public Dimension dimensionCasilla() {
        return casillas[0][0].getSize();
    }

    /**
     * Cambia el icono de una casilla
     * @param fila la fila de la casilla seleccionada
     * @param columna la columna de la casilla seleccionada
     * @param icono el nuevo icono
     */   
    public void ponerIconoCasilla(int fila, int columna, Icon icono) {
        casillas[fila][columna].setIcon(icono);
    }

    /**
     * Cambia el texto contenido en una casilla
     * @param fila la fila de la casilla seleccionada
     * @param columna la columna de la casilla seleccionada
     * @param text el nuevo texto
     */
    public void textoEnCasilla(int fila, int columna, String text) {

        casillas[fila][columna].setText(text);

    }

    /**
     * Añade un controlador a una casilla
     * @param controlador el nuevo controlador
     */
    public void addControlador(ActionListener controlador) {
        for (int fil = 0; fil < filas; fil++) {
            for (int col = 0; col < columnas; col++) {
                casillas[fil][col].addActionListener(controlador);
            }
        }
    }

    /**
     * @return las casillas
     */
    public CasillaSwing[][] getCasillas() {
        return casillas;
    }

}
