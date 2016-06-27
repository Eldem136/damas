package damas;

import UI.VistaJuego;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;
import utilidades.LectorXML;
import utilidades.Movimiento;

public class Cliente {

    /* referencia a la interfaz grafica y al controlador de la vista 
    principal de esta */
    private VistaJuego vista;
    private Controlador controlador;
    
    /* atributos de la conexion con el servidor */
    private String direccion;
    private int puerto;
    private Socket socket;
    private ObjectInputStream entradaObjetos;
    private ObjectOutputStream salidaObjetos;
    
    /* nombre del jugador */
    private String nombreJugador;
    
    /* Thread auxiliar para controlar la entrada sin quedarse 
    el thread principal en espera */
    private Thread oyenteEntrada;
    
    /* atributos del tablero y controlador de este */
    private Tablero tablero;
    private ControladorTableroJuego controladorPartida;
    
    /* atributos de control de turno */
    private String miColor;
    private boolean esMiTurno;
    private final static String MENSAJE_ES_MI_TURNO = 
            "Jugador %s es tu turno de seleccionar movimiento";
    private final static String MENSAJE_NO_ES_MI_TURNO = 
            "Esperando al servidor";
    
    /* atributos de lectura del xml con los datos de conexion */
    private final static String FICHERO_XML = "recursos/info_conexion.xml";
    private LectorXML lectorXML;
    
    /**
     * Crea un nuevo cliente con un reglamento
     * @param reglamento las reglas que se seguiran en una partida local
     */
    public Cliente(Reglas reglamento) {
        vista = new VistaJuego("damas");
        controlador = new Controlador(reglamento, this);
        
        vista.addControlador(controlador);
        controlador.vista(vista);
        vista.setCliente(this);
        
        cargarInfoConexion();
        
        crearConexionServidor();
        comenzarAEsperarEntrada();

    }
    
    /**
     * Carga los datos de conexion con el servidor
     */
    private void cargarInfoConexion() {
        try {
            System.out.println("intento crear lector");
            File ficheroXML = new File(FICHERO_XML);
            lectorXML = new LectorXML(new FileInputStream(ficheroXML));
            System.out.println("creo lector");
            direccion = lectorXML.getValorEtiqueta("direccion");
            System.out.println("leo direccion: "+direccion);
            puerto = Integer.parseInt(lectorXML.getValorEtiqueta("puerto"));
            System.out.println("leo puerto: " + puerto);
            
        } catch (Exception ex) {
            vista.mostrarError("Error al cargar los datos de la conexion con el servidor");
        } 
    }
    
    /**
     * Realiza la conexion con el servidor por sockets, pregunta el nombre 
     * al usuario y lo envia al servidor para registrarlo
     */
    private void crearConexionServidor() {
        try {
            socket = new Socket(direccion, puerto);
            
            salidaObjetos = new ObjectOutputStream(socket.getOutputStream());
            salidaObjetos.flush();
            entradaObjetos = new ObjectInputStream(socket.getInputStream());
            
            nombreJugador = vista.preguntarNombre();
            salidaObjetos.writeObject(nombreJugador);
            salidaObjetos.flush();
            
        } catch (IOException ex) {
            vista.mostrarError("Se ha producido un error con la entrada/salida "
                    + "al iniciar la conexion con el servidor");
        }
    }
    
    /**
     * Comienza el bucle que gestiona la entrada de datos por medio de otro 
     * thread para evitar bloquearse en la entrada
     */
    private void comenzarAEsperarEntrada() {
        
        oyenteEntrada = new Thread() {
            @Override
            public void run() {
                String mensajeEntrada;
                try {
                  do {
                       mensajeEntrada = entradaObjetos.readObject().toString();
                       switch ( mensajeEntrada ) {
                           case MensajesConexion.ACTUALIZAR_LISTA_USUARIOS:
                               leerListaActualizada();
                               break;
                           case MensajesConexion.GANADOR:
                               vista.mostrarFinal("HAS GANADO :D");
                               vista.addControlador(controlador);
                               break;
                           case MensajesConexion.PERDEDOR:
                               vista.mostrarFinal("Has perdido, buuuuu!");
                               vista.addControlador(controlador);
                               break;
                           case MensajesConexion.ACEPTAR_RETO:
                               mensajeEntrada = entradaObjetos.readObject().toString();
                               aceptarReto(mensajeEntrada);
                               break;
                           case MensajesConexion.RETO_ACEPTADO:
                               iniciarPartida(true);
                               break;
                           case MensajesConexion.RETO_RECHAZADO:
                               vista.mostrarError("El otro usuario ha "
                                       + "rechazado el reto");
                               break;
                           case MensajesConexion.SALIR:
                               mensajeEntrada = null;
                               break;
                           case MensajesConexion.FIN_TURNO:
                               actualizaTablero();
                               break;
                           case MensajesConexion.EMPIEZA_TURNO:
                               iniciarTurno();
                               break;
                       }
                  } while ( mensajeEntrada != null );
                } catch (IOException ex) {
                    vista.mostrarError("Error de entrada/salida en el thread"
                            + "de lectura");
                } catch (ClassNotFoundException ex) {
                    vista.mostrarError("Error de clases en el thread de "
                            + "lectura");
                }
            }
        };
        oyenteEntrada.start();
    }
    
    /**
     * Indica al servidor que se quiere actualizar la lista de jugadores
     */
    public void actualizarListaJugadores() {
        try {
            
            salidaObjetos.writeObject(MensajesConexion.ACTUALIZAR_LISTA_USUARIOS);
            salidaObjetos.flush();
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Acualiza la lista de jugadores con los datos actuales del servidor
     */
    private void leerListaActualizada() {
        ArrayList<String> listaJugadores = new ArrayList<>();
        try {
            String mensaje = entradaObjetos.readObject().toString();
            while ( ! mensaje.equals("OK") ) {
                listaJugadores.add(mensaje);
                mensaje = entradaObjetos.readObject().toString();
            }
            
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al leer la lista de usuarios");
        } catch (ClassNotFoundException ex) {
            vista.mostrarError("Error de clases al leer la lista de usuarios");
        } finally {
            vista.actualizarListaJugadores(listaJugadores);
        }
    }
    
    /**
     * Envia al servidor una peticion de reto hacia un jugador
     * @param nombreRival el nombre del jugador que se quiere retar
     */
    public void retarJugador(String nombreRival) {
        try {
            salidaObjetos.writeObject(MensajesConexion.RETAR);
            salidaObjetos.flush();
            salidaObjetos.writeObject(nombreRival);
            salidaObjetos.flush();
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al retar a un jugador");
        }
    }
    
    /**
     * Cierra la conexion con el servidor
     */
    public void cerrarCliente() {
        try {
            salidaObjetos.writeObject(MensajesConexion.CERRAR);
            salidaObjetos.flush();
            entradaObjetos.close();
            salidaObjetos.close();
            socket.close();
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al cerrar la conexion");
        }
    }
    
    /**
     * Inicia una nueva partida multijugador indicando si se es el jugador
     * retador o no para iniciar los turnos
     * @param soyJugadorRetador es true si se es el jugador retador
     */
    private void iniciarPartida(boolean soyJugadorRetador) {
        tablero = new Tablero();
        tablero.colocarFichas();
        
        miColor = ( soyJugadorRetador ? Ficha.NEGRA : Ficha.BLANCA );
        controladorPartida = new ControladorTableroJuego(this, vista);

        vista.setUIJuego();
        vista.crearTableroSwing(tablero.getFilaMaxima()+1, 
                tablero.getColumnaMaxima()+1);
        vista.addControladorDePartida(controladorPartida);

        vista.update(tablero, null);
        vista.cambiarTexto(( ! soyJugadorRetador ?  
                String.format(MENSAJE_ES_MI_TURNO, "blanco") : 
                MENSAJE_NO_ES_MI_TURNO ) );
        
        esMiTurno = ! soyJugadorRetador;
    }
    
    /**
     * @return el tablero
     */
    public Tablero getTablero() {
        return tablero;
    }
    
    /**
    * Calcula cuales son los movimientos validos a los que puede moverse
    * la ficha seleccionada y envia la informacion a la vista para marcarlos
    * en el tablero
    * @param fila la fila seleccionada
    * @param columna la columna seleccionada
    */
    public void movimientosValidos(int fila, int columna) {
        boolean esUnPeon;
        String colorFicha;
        ArrayList<Movimiento> movimientosValidos;
        
        if ( ! tablero.estaLaCasillaVacia(fila, columna) ) {
            
            esUnPeon = tablero.getFicha(fila, columna).isTransformable();
            colorFicha = tablero.getFicha(fila, columna).getColor();
            if ( ! colorFicha.equals(miColor) )
                return;
            else if ( esUnPeon )
                movimientosValidos = movimientosPeonValidos(fila, columna, colorFicha);
            else
                movimientosValidos = movimientosDamaValidos(fila, columna, colorFicha);
         
            
            for (Movimiento m : movimientosValidos) {
                vista.mostrarMovimientoValido(m.getFilaFinal(), m.getColFinal());
            }
        }
    }
    
    /**
    * Metodo auxiliar de movimientosValidos
    * Calcula los movimientos validos en el caso de mover un peon
    * @param fila la fila original del peon
    * @param columna la columna original del peon
    * @param color el color del peon
    * @return una lista con todos los movimientos posibles y validos
    */
    private ArrayList<Movimiento> movimientosPeonValidos(int fila, int columna, String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>(2);
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        int avanceFila = ( color.equals(Ficha.BLANCA) ? -1 : 1 );
        
        if ( tablero.estaLaCasillaVacia(fila + avanceFila, columna - 1) )
            movimientosValidos.add(new Movimiento(fila, columna, fila + avanceFila, columna - 1));
        else if ( ! tablero.fichaDelMismoColor(fila + avanceFila, columna - 1, color) )
            if ( tablero.estaLaCasillaVacia(fila + ( 2 * avanceFila ), columna - 2) )
                movimientosValidos.add(new Movimiento(fila, columna, fila + (2 * avanceFila), columna - 2));
        
        if ( tablero.estaLaCasillaVacia(fila + avanceFila, columna + 1) )
            movimientosValidos.add(new Movimiento(fila, columna, fila + avanceFila, columna + 1));
        else if ( ! tablero.fichaDelMismoColor(fila + avanceFila, columna + 1, color) )
            if ( tablero.estaLaCasillaVacia(fila + ( 2 * avanceFila ), columna + 2) )
                movimientosValidos.add(new Movimiento(fila, columna, fila + (2 * avanceFila), columna + 2));
        
        return movimientosValidos;
    }
    
    /**
    * Metodo auxiliar de movimientosValidos
    * Calcula los movimientos validos en el caso de mover una dama
    * @param fila la fila original de la dama
    * @param columna la columna original de la dama
    * @param color el color de la dama
    * @return una lista con todos los movimientos posibles y validos
    */
    private ArrayList<Movimiento> movimientosDamaValidos(int fila, int columna, String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>();
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, 1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, -1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, 1, color, movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, -1, color, movimientosValidos);
        
        return movimientosValidos;
    }
    
    /**
    * Metodo auxiliar de movimientosDamaValidos
    * Calcula los movimientos validos para la dama en una diagonal y direccion
    * @param fila la fila original de la dama
    * @param columna la columna original de la dama
    * @param aumentoFila indica la componente de las filas en la direccion
    * @param aumentoColumna indica la componente de las columnas en la direccion
    * @param color el color de la dama
    * @param movimientos la lista de los movimientos posibles donde
    * añadira los que encuentre
    */
    private void rellenarMovimientosDamaEnDireccion(int fila, int columna, int aumentoFila, int aumentoColumna, String color, ArrayList<Movimiento> movimientos) {
        
        boolean finComprobacion = false;
        boolean fichaComida = false;
        
        int filaInvestigada = fila + aumentoFila;
        int columnaInvestigada = columna + aumentoColumna;
        
        while ( ! finComprobacion  )  {
            
            if ( tablero.estaLaCasillaVacia(filaInvestigada, columnaInvestigada) )
                movimientos.add(new Movimiento(fila, columna, filaInvestigada, columnaInvestigada));
            else if ( ! tablero.fichaDelMismoColor(filaInvestigada, columnaInvestigada, color) 
                    && ! fichaComida)
                fichaComida = true;
            else
                finComprobacion = true;
            
            filaInvestigada += aumentoFila;
            columnaInvestigada += aumentoColumna;
            
        }
        
    }
    
    /**
     * Termina el turno y envia el movimiento al servidor
     * @param movimiento el movimiento seleccionado
     */
    public void terminarTurno(Movimiento movimiento) {
        if ( ! esMiTurno )
            return;
        try {
            salidaObjetos.writeObject(MensajesConexion.MOVIMIENTO);
            salidaObjetos.flush();
            salidaObjetos.writeObject(movimiento);
            salidaObjetos.flush();
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al enviar movimiento");
        }
    }
    
    /**
     * Indica al servidor que te rindes
     */
    public void rendirse() {
        try {
            salidaObjetos.writeObject(MensajesConexion.RENDICION);
            salidaObjetos.flush();
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al rendirse");
        }
    }
    
    /**
     * Actualiza el tablero con los datos obtenidos del servidor
     */
    public void actualizaTablero() {
        try {
            tablero = (Tablero) entradaObjetos.readObject();
            vista.update(tablero, null);
            vista.cambiarTexto(MENSAJE_NO_ES_MI_TURNO);
        } catch (IOException ex) {
            vista.mostrarError("Error de entrada/salida al actualizar el tablero");
        } catch (ClassNotFoundException ex) {
            vista.mostrarError("Error de clase al actualizar el tablero");
        }
    }
    
    /**
     * Comienza un nuevo turno
     */
    private void iniciarTurno() {
        String mensaje = String.format(MENSAJE_ES_MI_TURNO, 
                (miColor.equals(Ficha.BLANCA) ? "blanco" : "negro"));
        vista.cambiarTexto(mensaje);
        esMiTurno = true;
    }
    
    /**
     * Muestra un mensaje al usuario preguntando si acepta un reto del jugador leido
     * @throws IOException si se produce un error de entrada/salida en el socket
     */
    private void aceptarReto(String rival) throws IOException {
        boolean aceptar = 
                vista.preguntarReto(rival);
        if ( aceptar ) {
            salidaObjetos.writeObject(MensajesConexion.ACEPTAR_RETO);
            salidaObjetos.flush();
            iniciarPartida(false);
        }
        else {
             salidaObjetos.writeObject(
                    MensajesConexion.CANCELAR_RETO);
             salidaObjetos.flush();
        }
    }
    
}