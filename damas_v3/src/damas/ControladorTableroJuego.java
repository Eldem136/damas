/**
 * ControladorTableroJuego.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import UI.CasillaSwing;
import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import utilidades.Movimiento;

public class ControladorTableroJuego implements ActionListener {
    
    /* las referencias al modelo y la vista */
    private Cliente cliente;
    private VistaJuego vista;
    
    /* atributos que gestionan la entrada de un movimiento en dos partes */
    private boolean primeraParteMovimientoRealizada;
    private int filaInicial;
    private int columnaInicial;
    
    private Movimiento movimiento;

    /**
     * Crea un nuevo controlador que se utilizara para el tablero de las 
     * partidas multijugador
     * @param cliente el cliente que gestiona la conexion con el servidor
     * @param vistaJuego la vista que contiene el tablero
     */
    public ControladorTableroJuego(Cliente cliente, VistaJuego vistaJuego) {
        this.cliente = cliente;
        this.vista = vistaJuego;
    }
    
    /**
     * Sobreescribe el actionPerformed que se utilizara para el tablero
     * @param e evento entrante
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        switch (e.getActionCommand()) {
            case "BLANCO":
            case "NEGRO":
            case "":
                CasillaSwing c = (CasillaSwing) e.getSource();
                
                vista.resaltarCasilla(c.getFila(), c.getColumna());
                
                if ( ! primeraParteMovimientoRealizada ) {
                    filaInicial = c.getFila();
                    columnaInicial = c.getColumna();
                    movimiento = new Movimiento(filaInicial, columnaInicial, 0, 0);
                    primeraParteMovimientoRealizada = true;
                    cliente.movimientosValidos(filaInicial, columnaInicial);
                }
                else{
                    movimiento = new Movimiento(filaInicial, columnaInicial, c.getFila(), c.getColumna()); 
                    vista.repintarTablero();
                    primeraParteMovimientoRealizada = false;
                    cliente.terminarTurno(movimiento);
                }   break;
            case "Rendirse":
                cliente.rendirse();
                break;
        }
       
        
    }
    
    
}