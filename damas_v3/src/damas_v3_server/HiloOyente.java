/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloOyente implements Runnable {
    BufferedReader entrada;
    PrintWriter salida;
    Socket socket;
    String nombreCliente;
    public HiloOyente(Socket socket){
        this.socket = socket;
        
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
            nombreCliente = entrada.readLine();
            insertarClienteEnBD();
            
            String opcion = entrada.readLine();
            
            while ( ! opcion.equals("cerrarConexion") ) {
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(HiloOyente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    private void insertarClienteEnBD(){
        Servidor.instancia().insertarJugadorEnBD(nombreCliente);
    }
    
}
