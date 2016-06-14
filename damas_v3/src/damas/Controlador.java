/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.io.File;
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
            String[] jugadores = vista.pedirJugadores();
            vista.limpiarTableroSwing();
            vista.revalidate();
            vista.repaint();
            partida = new Partida(jugadores[0], jugadores[1], reglas);

            tablero = partida.tablero;
            tablero.addObserver(vista);
            partida.vista(vista);
            partida.jugar();
            
            
        }
        else if(e.getActionCommand().equals("Guardar partida")){
            try {
                if(partida!= null)
                    partida.guardar(partida);
            } catch (IOException ex) {
                //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                vista.mostrarError("No se ha podido guardar la partida");
            }
        }
        else if(e.getActionCommand().equals("Cargar partida")){
            try {
                File file = vista.cargarPartida();
                if((partida = Partida.cargar(file)) != null){
                    tablero = partida.getTablero();
                    tablero.addObserver(vista);
                    partida.vista(vista);
                    partida.jugar();
                }
            } catch (IOException ex) {
                //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                vista.mostrarError("No se ha podido cargar la partida");
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
                vista.mostrarError("No se ha podido cargar la partida");
            } catch (Exception ex){
                vista.mostrarError("No se ha podido cargar la partida");
            }
        }
    }
    
    public void vista(VistaJuego vista){
        this.vista = vista;
    }
    
}
