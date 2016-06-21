/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v3_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zeko
 */
public class HiloOyenteThread extends Thread implements HiloCliente{
    BufferedReader entrada;
    PrintWriter salida;
    Socket socket;
    String nombreCliente;
    
    private HiloPartida partida;
    private Queue<String> mensajesParaPartida;
    
    public HiloOyenteThread(Socket socket, String nombreCliente){
        this.socket = socket;
        this.nombreCliente = nombreCliente;
        
        mensajesParaPartida = new LinkedList<>();
        
        try {
            
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        } catch (IOException ex) {
            Logger.getLogger(HiloOyente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    
    @Override
    public void run() {
        try {
            //leer nombre del jugador
            insertarClienteEnBD();
            
            leerOrdenes();
            
        } catch (IOException ex) {
            Logger.getLogger(HiloOyente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void insertarClienteEnBD(){
        Servidor.instancia().insertarJugadorEnBD(nombreCliente);
    }
    
    private void leerOrdenes() throws IOException{
        System.out.println("leyendo ordenes");
        boolean fin = false;
        String lectura;
        do{
            lectura = entrada.readLine();
            System.out.println("he leido: "+lectura);
            switch(lectura){
                case "movimiento":
                    lectura = entrada.readLine();
                    System.out.println("pasa: "+lectura);
                    ejecutarMovimiento(lectura);
                    break;
                case "rendicion":
                    System.out.println("ha ganado el que no se ha rendido");
                    fin = true;
                    break;
                case "Cerrar":
                    System.out.println("se nos va nuestro mejor cliente");
                    salida.println("Salir");
                    fin = true;
                    break;
                case "Actualizar lista":
                    System.out.println("mandando lista actualizada");
                    leerConectados();
                    break;
                case "Retar":
                    System.out.print("me llega un reto a ");
                    String rival = entrada.readLine();
                    System.out.println(rival);
                    Servidor.instancia().enviarReto(nombreCliente, rival);
                    break;
                case "Ganador":
                    aumentarVictoria();
                    break;
                case "Aceptar reto":
                    respuestaReto(lectura);
                    break;
                case "Cancelar reto":
                    respuestaReto(lectura);
                    break;
                default:
                    System.out.println("Ai dont anderstand llu");
                    break;
            }
        }while(!fin);
        Servidor.instancia().adiosJugador(nombreCliente);
    }
    
    private void ejecutarMovimiento(String lectura) {
        String[] leido = lectura.split(" ");
        System.out.println("long: "+leido.length);
        salida.println("muevo a ");
        System.out.println("respuesta");
    }
    
    private void leerConectados() {
        salida.println("Actualizar lista");
        Set<String> jugadoresConectados = Servidor.instancia().listaJugadores();
        for (String jugador : jugadoresConectados) {
            if ( ! jugador.equals(nombreCliente) )
                salida.println(jugador);
        }
        salida.println("OK");
    }
    
    private void aumentarVictoria() {
        Servidor.instancia().aumentarPartidasGanadasJugador(nombreCliente);
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    @Override
    public void enviaMensaje(String mensaje) {
        salida.println(mensaje);
    }

    @Override
    public String getNombreCliente() {
        return nombreCliente;
    }

    @Override
    public void esperoMensajes(HiloPartida hiloPartida) {
        partida = hiloPartida;
    }

    @Override
    public String leerMensaje() {
        return mensajesParaPartida.poll();
    }
    
    private synchronized void respuestaReto(String respuesta) {
        mensajesParaPartida.offer(respuesta);
        synchronized(partida) {
            partida.notifyAll();
        }
    }
    
}