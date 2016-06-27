/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import damas.Tablero;

public interface HiloCliente{
    public void enviaMensaje(String mensaje);
    public void setOyentePartida(HiloPartida hiloPartida);
    public String leerMensaje();
    
    public String getNombreCliente();
    public void enviarTableto(Tablero tablero);
    public void haGanado();
    public void haPerdido();
    
}
