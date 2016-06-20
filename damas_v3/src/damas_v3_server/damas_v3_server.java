/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v3_server;

/**
 *
 * @author Zeko
 */
public class damas_v3_server {
    
    public static void main(String[] args){
        Servidor servidor = Servidor.instancia();
        servidor.esperarClientes();
    }
    
    
}
