/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v3_server;

import damas.Jugador;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zeko
 */
public class Servidor{
    
    ServerSocket serverSocket;
    Socket socket;
    BufferedReader entrada;
    PrintWriter salida;
    final String dbUrl = "jdbc:mysql://web-ter.unizar.es:3306/648391";
    final String usuario = "648391";
    final String pass = "648391";
    static final String driverSql = "com.mysql.jdbc.Driver";
    Jugador[] jugadoresActivos;
    Connection conexion = null;
    Statement statement;
    
    private static Servidor instancia;
    
    private Map<String, Thread> ListaConexionesClientes;
    private Set<Thread> ListaPartidasActuales;
    private ExecutorService poolPartidas;
    private final static int MAXIMO_CONEXIONES = 6;
    
    private final static String QUERY_BUSCAR_JUGADOR = "SELECT * FROM `648391`.jugador WHERE nombre = '%s'";
    private final static String QUERY_JUGADOR_INSERTAR_NUEVO = "INSERT INTO jugador VALUES('%s', 0)";
    private final static String QUERY_JUGADOR_UPDATE_GANADOR = "UPDATE `648391`.jugador SET partidas_ganadas = partidas_ganadas + 1 WHERE nombre = '%s'";
    
    private Servidor(){
        
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        conectarABaseDatos();
        
        ListaConexionesClientes = new HashMap<>();
        ListaPartidasActuales = new HashSet<>();
        poolPartidas = Executors.newFixedThreadPool(MAXIMO_CONEXIONES/2);
        
    }
    
    public static synchronized Servidor instancia(){
        if (instancia == null){
            instancia = new Servidor();
        }
        return instancia;
    }
    
//    public void esperarClientes(){
//        System.out.println("Servidor esperando clientes...");
//        ExecutorService poolThreads = Executors.newFixedThreadPool(MAXIMO_CONEXIONES);
//        while(true){
//            try {
//                socket = serverSocket.accept();
//                
//                poolThreads.execute(new HiloOyente(socket));
//       
//            } catch (IOException ex) {
//                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }   
//    }
    
    public void esperarClientes () {
        System.out.println("Servidor esperando clientes...");
        
        while(true){
            try {
                socket = serverSocket.accept();
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String nombreCliente = entrada.readLine();
                System.out.println("nuevo cliente entra. socket closed? "+socket.isClosed());
                Thread nuevaConexion = new HiloOyenteThread(socket, nombreCliente);
                ListaConexionesClientes.put(nombreCliente, nuevaConexion);
                nuevaConexion.start();
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
    
    private void conectarABaseDatos(){
        
        try {
            Class.forName(driverSql);
            conexion =  DriverManager.getConnection(dbUrl, usuario, pass);
            
            //si la base de datos esta vacia, se crea una tabla jugador.
            statement = conexion.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS jugador (nombre varchar(16) PRIMARY KEY, partidas_ganadas INTEGER)");
            statement.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized boolean aumentarPartidasGanadasJugador(String nombre){
        try {
            statement = conexion.createStatement();
            
            statement.executeUpdate(String.format(QUERY_JUGADOR_UPDATE_GANADOR, nombre));
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    public synchronized boolean insertarJugadorEnBD(String nombre){
        try {
            statement = conexion.createStatement();
            ResultSet resultset = statement.executeQuery(String.format(QUERY_BUSCAR_JUGADOR, nombre));
            
            if(!resultset.first()){
                statement.execute(String.format(QUERY_JUGADOR_INSERTAR_NUEVO, nombre));
                System.out.println("metoa " + nombre);
            }

            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public void metodo(){
        System.out.println("ey soy un metodo");
    }
    
    public Set<String> listaJugadores() {
        return ListaConexionesClientes.keySet();
    }
    
    public void adiosJugador(String nombreJugador) {
        ListaConexionesClientes.remove(nombreJugador);
    }
    
    public void enviarReto(String retador, String retado) {
        HiloOyenteThread threadJugador1 = (HiloOyenteThread) ListaConexionesClientes.get(retador);
        HiloOyenteThread threadJugador2 = (HiloOyenteThread) ListaConexionesClientes.get(retado);
        poolPartidas.execute(new HiloPartida(threadJugador1, threadJugador2));
    }
    
    
}
