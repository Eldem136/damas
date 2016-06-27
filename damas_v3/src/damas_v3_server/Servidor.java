/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import damas.Jugador;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;


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
    
    private Map<String, HiloOyenteThread> ListaConexionesClientes;
    private Set<String> usuariosConectadosDisponibles;
    private ExecutorService poolPartidas;
    private final static int MAXIMO_CONEXIONES = 20;
    
    //Strings de Querys para la base de datos.
    private final static String QUERY_BUSCAR_JUGADOR =
            "SELECT * FROM `648391`.jugador WHERE nombre = '%s'";
    private final static String QUERY_JUGADOR_INSERTAR_NUEVO = 
            "INSERT INTO jugador VALUES('%s', 0)";
    private final static String QUERY_JUGADOR_UPDATE_GANADOR = 
            "UPDATE `648391`.jugador SET partidas_ganadas = partidas_ganadas "
            + "+ 1 WHERE nombre = '%s'";
    
    private Reglas reglamento;
    
    /**
     * Constructor de la clase Servidor. Una vez creado, se conectará a la
     * base de datos y creará un ThreadPool de partidas.
     */
    private Servidor(){
        
        try {
            serverSocket = new ServerSocket(10000);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        conectarABaseDatos();
        
        ListaConexionesClientes = new HashMap<>();
        usuariosConectadosDisponibles = new HashSet<>();
        poolPartidas = Executors.newFixedThreadPool(MAXIMO_CONEXIONES/2);
        
    }
    
    /**
     * Singleton para el servidor.
     * @return objeto de tipo servidor único.
     */
    public static synchronized Servidor instancia(){
        if (instancia == null){
            instancia = new Servidor();
        }
        return instancia;
    }

    /**
     * Bucle de espera de sockets clientes desde el ServerSocket.
     * Cada vez que llega un cliente nuevo, se crea un HiloOyenteThread que 
     * gestionará la comunicación con ese cliente.
     */
    public void esperarClientes () {
        System.out.println("Servidor esperando clientes...");
        
        while(true){
            try {
                socket = serverSocket.accept();
                System.out.println("nuevo cliente entra.");
                HiloOyenteThread nuevaConexion = new HiloOyenteThread(socket);
                
                ListaConexionesClientes.put(nuevaConexion.getNombreCliente(),
                        nuevaConexion);
                usuariosConectadosDisponibles.add(
                        nuevaConexion.getNombreCliente());
                
                nuevaConexion.start();
            } catch (IOException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Método para conectar el servidor a la base de datos MySql.
     */
    private void conectarABaseDatos(){
        
        try {
            Class.forName(driverSql);
            conexion =  DriverManager.getConnection(dbUrl, usuario, pass);
            
            //si la base de datos esta vacia, se crea una tabla jugador.
            statement = conexion.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS jugador"
               + " (nombre varchar(16) PRIMARY KEY, partidas_ganadas INTEGER)");
            statement.close();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método que aumentará el número de partidas ganadas de un jugador
     * en la base de datos, dado su nombre.
     * @param nombre
     * @return true si se actualiza la base de datos correctamente, false en 
     * caso contrario.
     */
    public synchronized boolean aumentarPartidasGanadasJugador(String nombre){
        try {
            statement = conexion.createStatement();
            
            statement.executeUpdate(String.format
                      (QUERY_JUGADOR_UPDATE_GANADOR, nombre));
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName())
                    .log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    /**
     * Método para insertar un jugador en la base de datos con su nombre.
     * @param nombre
     * @return true si la inserción se realiza correctamente, false
     * en caso contrario.
     */
    public synchronized boolean insertarJugadorEnBD(String nombre){
        try {
            statement = conexion.createStatement();
            ResultSet resultset = statement.executeQuery(String.format(QUERY_BUSCAR_JUGADOR, nombre));
            
            if(!resultset.first()){
                statement.execute(String.format(QUERY_JUGADOR_INSERTAR_NUEVO, nombre));
            }

            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return La lista de usuarios conectados disponibles.
     */
    public Set<String> listaJugadores() {
        return usuariosConectadosDisponibles;
    }
    
    /**
     * Elimina un jugador de la Lista de jugadores disponibles.
     * @param nombreJugador 
     */
    public void adiosJugador(String nombreJugador) {
        ListaConexionesClientes.remove(nombreJugador);
    }
    
    /**
     * Método para hacer efectivo el reto de un jugador hacia otro, es decir,
     * la invitación a jugar, en caso de realizarse, se elimina a ambos juga-
     * dores de la lista de disponibles y se inicia un nuevo Thread de Partida
     * dentro del ThreadPool de partidas.
     * @param retador -- jugador que envía el reto.
     * @param retado  -- jugador que es retado.
     */
    public void enviarReto(String retador, String retado) {
        HiloOyenteThread threadJugador1 = (HiloOyenteThread) ListaConexionesClientes.get(retador);
        HiloOyenteThread threadJugador2 = (HiloOyenteThread) ListaConexionesClientes.get(retado);
        usuariosConectadosDisponibles.remove(retador);
        usuariosConectadosDisponibles.remove(retado);
        poolPartidas.execute(new HiloPartida(threadJugador1, threadJugador2, reglamento));
    }
    
    /**
     * Método para hacer set de las reglas de juego en el servidor.
     * @param reglas 
     */
    public void setReglas(Reglas reglas) {
        this.reglamento = reglas;
    }
    
    /**
     * Método para añadir un cliente a la lista de conexiones de clientes,
     * guardando el Thread Oyente que está gestionando la conexión con el 
     * mismo.
     * @param nombre
     * @param thread 
     */
    public void addCliente(String nombre, HiloCliente thread) {
        ListaConexionesClientes.put(nombre, (HiloOyenteThread)thread);
    }
    
    /**
     * Añade un usuario disponible a la lista de objetos no repetidos 
     * usuariosConectadosDisponibles.
     * @param nombre 
     */
    public void addUsuarioDisponible(String nombre) {
        usuariosConectadosDisponibles.add(nombre);
    }
    
}
