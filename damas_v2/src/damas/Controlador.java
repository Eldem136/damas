/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import reglas.Reglas;

/**
 *
 * @author Zeko
 */
public class Controlador implements java.awt.event.ActionListener{
    private VistaJuego vista;
    private Partida partida;
    private Reglas reglas;
    private Tablero tablero;
    
    public Controlador(Reglas reglas){
        this.reglas = reglas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    
        if(e.getActionCommand().equals("Nueva partida")){
            
            partida = new Partida("Jugador1", "Jugador2", reglas);

            tablero = partida.tablero;
            tablero.addObserver(vista);
            partida.vista(vista);

//            partida.iniciarTableroSwing();
//            vista.addControladorDePartida(partida);
            
            partida.jugar();
            //vista.actualizarTableroSwing(tablero);
            //SwingUtilities.invokeLater(partida);
//            SwingUtilities.invokeLater(partida);
            
            
        }
        else if(e.getActionCommand().equals("Cargar partida")){
            
        }
        else if(e.getActionCommand().equals("Guardar partida")){
            
        }
    }
    
    public void vista(VistaJuego vista){
        this.vista = vista;
    }
    
}
