package damas;

import UI.CasillaSwing;
import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import utilidades.Movimiento;

public class ControladorTableroJuego implements ActionListener {
    
    private Cliente cliente;
    private VistaJuego vista;
    
    private boolean primeraParteMovimientoRealizada;
    private boolean movimientoListo;
    private int filaInicial;
    private int columnaInicial;
    
    private Movimiento movimiento;

    public ControladorTableroJuego(Cliente cliente, VistaJuego vistaJuego) {
        this.cliente = cliente;
        this.vista = vistaJuego;
    }
    
    

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
                    movimientoListo = true;
                    vista.repintarTablero();
                    primeraParteMovimientoRealizada = false;
                    cliente.terminarTurno();
                }   break;
            case "Rendirse":
                cliente.rendirse();
                break;
        }
       
        
    }
    
    
}