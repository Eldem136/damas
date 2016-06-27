/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import reglas.ReglasDamas;


public class damas_v3_server {
    /**
     * Main del servidor, lo inicializa, le pasa como argumento las reglas
     * del juego y lo pone a esperar clientes.
     */
    public static void main(String[] args){
        Servidor servidor = Servidor.instancia();
        servidor.setReglas(new ReglasDamas());
        servidor.esperarClientes();
        
    }
    
}
