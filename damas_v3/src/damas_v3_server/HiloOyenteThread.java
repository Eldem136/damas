/**
 * Movimiento.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas_v3_server;

import damas.MensajesConexion;
import damas.Tablero;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import utilidades.Movimiento;

/**
 *Clase que se encargará de gestionar la conexión con los clientes individual-
 * mente.
 */
public class HiloOyenteThread extends Thread implements HiloCliente{
    private Socket socket;
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
    private String nombreCliente;
    
    private HiloPartida partida;
    private Queue<String> mensajesParaPartida;
    
    public HiloOyenteThread(Socket socket, String nombreCliente){
        this.socket = socket;
        this.nombreCliente = nombreCliente;
        
        mensajesParaPartida = new LinkedList<>();
        
        try {            
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
            salidaObjetos.flush();
            entradaObjetos = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida en constructor");
        }
        
    }

    public HiloOyenteThread(Socket socket) {
        this.socket = socket;
        
        mensajesParaPartida = new LinkedList<>();
        
        try {      
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
            salidaObjetos.flush();
            entradaObjetos = new ObjectInputStream(socket.getInputStream());
            nombreCliente = entradaObjetos.readObject().toString();
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida en constructor");
        } catch (ClassNotFoundException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de clase en constructor");
        }
    }
    
    

    
    
    @Override
    public void run() {
        try {
            insertarClienteEnBD();
            
            leerOrdenes();
            
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida en el run");
        }
        
        
    }
    
    /**
     * Método que inserta jugadores en la base de datos del servidor.
     */
    private void insertarClienteEnBD(){
        Servidor.instancia().insertarJugadorEnBD(nombreCliente);
    }
    
    /**
     * Método que en bucle leerá las órdenes del cliente conectado al 
     * hiloOyente y que actuará en consecuencia dependiendo de la órdenes.
     * @throws IOException 
     */
    private void leerOrdenes() throws IOException{
        boolean fin = false;
        String lectura;
        do{
            try {
                lectura = entradaObjetos.readObject().toString();
                switch(lectura){
                    case MensajesConexion.MOVIMIENTO:
                        Movimiento movimientoLeido = 
                                (Movimiento) entradaObjetos.readObject();
                        ejecutarMovimiento(movimientoLeido);
                        break;
                    case MensajesConexion.RENDICION:
                        rendicion();
                        break;
                    case MensajesConexion.CERRAR:
                        salidaObjetos.writeObject(MensajesConexion.CERRAR);
                        salidaObjetos.flush();
                        fin = true;
                        break;
                    case MensajesConexion.ACTUALIZAR_LISTA_USUARIOS:
                        leerConectados();
                        break;
                    case MensajesConexion.RETAR:
                        String rival = entradaObjetos.readObject().toString();
                        System.out.println(rival);
                        Servidor.instancia().enviarReto(nombreCliente, rival);
                        break;
                    case MensajesConexion.ACEPTAR_RETO:
                        respuestaReto(lectura);
                        break;
                    case MensajesConexion.CANCELAR_RETO:
                        respuestaReto(lectura);
                        break;
                    default:
                        System.err.println("Orden no reconocida: "+lectura);
                        break;
                }
            } catch (ClassNotFoundException ex) {
                System.err.println("Error HiloOyenteThread del cliente: " + 
                      nombreCliente + "\n\tError de clase en la lectura de ordenes");
            }
        }while(!fin);
        Servidor.instancia().adiosJugador(nombreCliente);
    }
    
    /**
     * Recibe como argumento el movimiento del jugador y lo coloca en la 
     * cola de mensajes para la partida y avisa a ésta de que se ha 
     * recibido el movimiento.
     * @param movimiento 
     */
    private void ejecutarMovimiento(Movimiento movimiento) {
        
        mensajesParaPartida.offer(movimiento.getFilaInicial()+
                " "+movimiento.getColInicial()+
                " "+movimiento.getFilaFinal()+
                " "+movimiento.getColFinal());

        synchronized(partida) {
            partida.notify();
        }
    }
    
    /**
     * Actualiza la lista de jugadores conectados del servidor.
     * @throws IOException 
     */
    private void leerConectados() throws IOException {
        salidaObjetos.writeObject(MensajesConexion.ACTUALIZAR_LISTA_USUARIOS);
        salidaObjetos.flush();
        Set<String> jugadoresConectados = Servidor.instancia().listaJugadores();
        for (String jugador : jugadoresConectados) {
            if ( ! jugador.equals(nombreCliente) ) {
                salidaObjetos.writeObject(jugador);
                salidaObjetos.flush();
        
            }
        }
        salidaObjetos.writeObject(MensajesConexion.OK);
        salidaObjetos.flush();
        
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    /**
     * Envía el mensaje pasado como argumento al cliente.
     * @param mensaje 
     */
    @Override
    public void enviaMensaje(String mensaje) {
        try {
            salidaObjetos.writeObject(mensaje);
            salidaObjetos.flush();
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida al enviar un mensaje");
        }
        
    }

    /**
     * @return el nombre del cliente.
     */
    @Override
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Setea la partida del Thread oyente para tener en él una referencia al
     * hilo que gestiona la partida.
     * @param hiloPartida 
     */
    @Override
    public void setOyentePartida(HiloPartida hiloPartida) {
        partida = hiloPartida;
    }

    /**
     * @return el primer mensaje de la cola mensajesParaPartida.
     */
    @Override
    public String leerMensaje() {
        return mensajesParaPartida.poll();
    }
    
    /**
     * Coloca en la cola de mensajes de la partida la respuesta a un reto en 
     * el caso del jugador retado.
     * @param respuesta 
     */
    private synchronized void respuestaReto(String respuesta) {
        mensajesParaPartida.offer(respuesta);
        synchronized(partida) {
            partida.notify();
        }
    }

    /**
     * Envía el tablero al jugador.
     * @param tablero 
     */
    @Override
    public void enviarTableto(Tablero tablero) {
        try {
            salidaObjetos.writeObject(MensajesConexion.FIN_TURNO);
            salidaObjetos.flush();
            salidaObjetos.reset();
            salidaObjetos.writeObject(tablero);
            salidaObjetos.flush();
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida al enviar el tablero");
        }
    }

    /**
     * Gestiona la victoria del jugador, aumentando sus victorias en la base 
     * de datos y devolviendolo a la lista de jugadores disponibles ahora que 
     * no está jugando.
     */
    @Override
    public void haGanado() {
        try {
            salidaObjetos.writeObject(MensajesConexion.GANADOR);
            salidaObjetos.flush();
            Servidor.instancia().aumentarPartidasGanadasJugador(nombreCliente);
            Servidor.instancia().addUsuarioDisponible(nombreCliente);
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida al enviar la victoria");
        }
        
    }

    /**
     * Gestiona la derrota del jugador, devolviendolo a la lista de jugadores
     * disponibles ahora que no está jugando.
     */
    @Override
    public void haPerdido() {
        try {
            salidaObjetos.writeObject(MensajesConexion.PERDEDOR);
            salidaObjetos.flush();
            Servidor.instancia().addUsuarioDisponible(nombreCliente);
        } catch (IOException ex) {
            System.err.println("Error HiloOyenteThread del cliente: " + nombreCliente
                    + "\n\tError de entrada/salida al enviar la derrota");
        }
    }
    
    private void  rendicion(){
        mensajesParaPartida.offer(MensajesConexion.RENDICION);
        
        synchronized(partida){
            partida.notify();
        }
    }
    
}