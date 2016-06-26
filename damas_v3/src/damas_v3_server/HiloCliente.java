/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v3_server;

import damas.Tablero;

/**
 *
 * @author diego
 */
public interface HiloCliente{
    public void enviaMensaje(String mensaje);
    public void esperoMensajes(HiloPartida hiloPartida);
    public String leerMensaje();
    
    public String getNombreCliente();
    public void enviarTableto(Tablero tablero);
    public void haGanado();
    public void haPerdido();
    
    
}
