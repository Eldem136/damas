/**
 * Controlador.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import reglas.Reglas;

public class Controlador implements java.awt.event.ActionListener{
    private VistaJuego vista;
    private Partida partida;
    private Reglas reglas;
    private Tablero tablero;
    
    private Cliente cliente;
    
    /**
    * Inicia un nuevo controlador para la interfaz grafica
    * @param reglas las reglas que se usaran
    */
    public Controlador(Reglas reglas){
        this.reglas = reglas;
    }
    
    /**
     * Inicia un nuevo controlador que contiene una referenca a un cliente
     * @param reglas las reglas que se usaran
     * @param cliente el cliente que gestiona la comunicacion con el servidor
     */
    public Controlador(Reglas reglas, Cliente cliente) {
        this(reglas);
        this.cliente = cliente;
    }

    /**
    * Sobreescribe en actionPerformed que se usara en la interfaz grafica
    * @param e el evento entrante
    */
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
                vista.mostrarError("No se ha podido cargar la partida");
            } catch (ClassNotFoundException ex) {
                vista.mostrarError("No se ha podido cargar la partida");
            } catch (Exception ex){
                vista.mostrarError("No se ha podido cargar la partida");
            }
        } else if ( e.getActionCommand().equals("Retar") ) {
            cliente.retarJugador(vista.getNombreJugadorRetado());
        } else if ( e.getActionCommand().equals("Retado") ) {
            vista.mostrarError("Te han retado");
        } else if ( e.getActionCommand().equals("Actualizar lista") ) {
            cliente.actualizarListaJugadores();
        }
    }
    
    /**
    * Asigna la vista
    * @param vista la vista
    */
    public void vista(VistaJuego vista){
        this.vista = vista;
    }
    
}
