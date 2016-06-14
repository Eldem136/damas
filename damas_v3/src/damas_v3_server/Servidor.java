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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zeko
 */
public class Servidor extends Thread{
    
    ServerSocket serverSocket;
    Socket socket;
    BufferedReader entrada;
    PrintWriter salida;
    
    public Servidor(){
        
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        esperarClientes();
    }
    
    private void esperarClientes(){
        System.out.println("Servidor esperando clientes...");
        while(true){
            try {
                socket = serverSocket.accept();
                System.out.println("Cliente reconocido");
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                
                salida.println("hola nuevo cliente");
                leerOrdenes();
                
                entrada.close();
                salida.close();
                socket.close(); 
                
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
                default:
                    System.out.println("Ai dont anderstand llu");
                    break;
            }
        }while(!fin);
        
    }

    private void ejecutarMovimiento(String lectura) {
        String[] leido = lectura.split(" ");
        System.out.println("long: "+leido.length);
        salida.println("muevo a ");
        System.out.println("respuesta");
    }
    
}
