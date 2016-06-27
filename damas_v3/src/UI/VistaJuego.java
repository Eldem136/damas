/**
 * VistaJuego.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package UI;

import damas.Cliente;
import damas.Controlador;
import damas.Ficha;
import damas.Tablero;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Observable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import reglas.ReglasDamas;

public class VistaJuego extends JFrame implements java.util.Observer {
    
    JLabel contador; 
    JLabel texto, logo, version;
    JPanel panelJuego;
    int filas, columnas;
    TableroSwing tableroSwing;
    
    static final String DEC = "Dec", INC = "Inc", INICIO = "Inicio";
    
    // Rutas de los recursos
    String rutaCarpetaRecursos = "recursos";
    Icon iconoFichaBlanca = new ImageIcon("recursos/white_checker.png");
    Icon iconoFichaNegra = new ImageIcon("recursos/black_checker.png");
    Icon iconoDamaBlanca = new ImageIcon("recursos/white_dama.png");
    Icon iconoDamaNegra = new ImageIcon("recursos/black_dama.png");
    
    JMenu menu;
    JMenuItem nuevo, guardar, cargar;
    JMenuBar barraMenu;
    
    String textoVersion = "Cliente Damas V3. EUPT Tecnología de la programación."
            +"\n Diego Malo Mateo. \n Ezequiel Barbudo Revuelto.";
    JButton btnRendirse;
    
    /* Atributos de la lista de jugadores conectador */
    private DefaultListModel modeloListaJugadores;
    private JList listaJugadores;
    private JButton botonRetar;
    private JButton botonActualizarJugadores;
    private Cliente cliente;
    
    /**
     * Crea una nueva ventana de juego
     * @param titulo el titulo de la ventana
     */
    public VistaJuego(String titulo) {
        super(titulo);
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) { 
              if ( cliente != null )
                  cliente.cerrarCliente();
              System.exit(0);
          }
        });  
        
        comprobarFicheroRecursos();
        getContentPane().setLayout(new BorderLayout());            
        texto = new JLabel("");
        getContentPane().add(texto, BorderLayout.NORTH);
        panelJuego = new JPanel();
        panelJuego.setLayout(new FlowLayout());
        getContentPane().add(panelJuego,BorderLayout.CENTER);
        
        barraMenu = new JMenuBar();
        setJMenuBar(barraMenu);
        menu = new JMenu("Opciones");
        barraMenu.add(menu);
        nuevo = new JMenuItem("Nueva partida");
        cargar = new JMenuItem("Cargar partida");
        guardar = new JMenuItem("Guardar partida");
        menu.add(nuevo);
        menu.add(cargar);
        menu.add(guardar);
        
        getContentPane().add(crearListaJugadores());
        
        version = new JLabel();
        version.setText(textoVersion);
        version.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(version, BorderLayout.SOUTH);

        setSize(600,500); setVisible(true);  
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Comprueba que la carpeta de recursos existe, en caso contrario la 
     * ejecucion se detiene tras avisar al usuario de la falta de dicha carpeta
     */
    private void comprobarFicheroRecursos() {
        File directorioRecursos = new File(rutaCarpetaRecursos);
        if ( ! directorioRecursos.exists() ) {
            mostrarError("No existe la carpeta de recursos");
            System.exit(-1);
        }
    }

    /**
     * Añade un nuevo controlador a la ventana
     * @param controlador el nuevo controlador
     */
    public void addControlador(ActionListener controlador){
        nuevo.addActionListener(controlador);
        guardar.addActionListener(controlador);
        cargar.addActionListener(controlador);
        botonRetar.addActionListener(controlador);
        botonActualizarJugadores.addActionListener(controlador);
    }
    
    /**
     * Añade un nuevo controlador de partida al tablero que contiene la ventana
     * @param controlador el nuevo controlador de tablero
     */
    public void addControladorDePartida(ActionListener controlador){
        tableroSwing.addControlador(controlador);
        btnRendirse.addActionListener(controlador);
    }
    
    /**
     * Crea la interfaz de partida
     */
    public void setUIJuego(){
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());            
        texto = new JLabel("");
        texto.setHorizontalAlignment(JLabel.CENTER);
        texto.setVerticalAlignment(JLabel.CENTER);
        getContentPane().add(texto, BorderLayout.NORTH);
        panelJuego = new JPanel();
        panelJuego.setLayout(new FlowLayout());
        getContentPane().add(panelJuego,BorderLayout.CENTER);
        btnRendirse = new JButton("Rendirse");
        getContentPane().add(btnRendirse, BorderLayout.SOUTH);
    }
    
    /**
     * Crea la interfaz del menu principal
     */
    public void setUIInicio(){
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());            
        texto = new JLabel("");
        getContentPane().add(texto, BorderLayout.NORTH);
        panelJuego = new JPanel();
        panelJuego.setLayout(new FlowLayout());
        getContentPane().add(panelJuego,BorderLayout.CENTER);
        
        getContentPane().add(crearListaJugadores());
        
        version = new JLabel();
        version.setText(textoVersion);
        version.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(version, BorderLayout.SOUTH);

        setSize(600,500); setVisible(true);  
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    /**
     * Crea una lista de jugadores que se rellenara con los datos obtenidos del
     * servidor y que contiene un boton para actualizar la lista y otro para 
     * retar al rival seleccionado, este ultimo solo estara activo si se ha 
     * seleccionado algun rival de la lista
     * 
     * @return el panel que contiene la lista
     */
    private JPanel crearListaJugadores() {
        JPanel panelListaJugadores = new JPanel();
        panelListaJugadores.setLayout(new BorderLayout());
        
        modeloListaJugadores = new DefaultListModel();
        listaJugadores = new JList(modeloListaJugadores);
        listaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaJugadores.setLayoutOrientation(JList.VERTICAL);
        listaJugadores.setEnabled(true);
        
        listaJugadores.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                botonRetar.setEnabled(true);
            }
        });
        
        JScrollPane scrollPanelListaJugadores = new JScrollPane(listaJugadores);
        scrollPanelListaJugadores.setPreferredSize(new Dimension(20, 30));
        panelListaJugadores.add(scrollPanelListaJugadores, BorderLayout.CENTER);
        
        botonRetar = new JButton("Retar");
        panelListaJugadores.add(botonRetar, BorderLayout.SOUTH);
        botonRetar.setEnabled(false);
        
        botonActualizarJugadores = new JButton("Actualizar lista");
        panelListaJugadores.add(botonActualizarJugadores, BorderLayout.NORTH);
        
        return panelListaJugadores;
    }
    
    /**
     * Crea un nuevo tablero de juego
     * @param filas
     * @param columnas 
     */
    public void crearTableroSwing(int filas, int columnas){      
        
        this.filas = filas;
        this.columnas = columnas;
        tableroSwing = new TableroSwing(filas, columnas);
        panelJuego.add(tableroSwing);        
        setSize(tableroSwing.getAnchura(),
                tableroSwing.getAltura() + 50);
        setLocationRelativeTo(this);   
    }
    
    /**
     * Elimina el tablero de juego
     */
    public void limpiarTableroSwing(){
        if(tableroSwing != null){
            getContentPane().remove(tableroSwing);
        }
    }
    
    /**
     * Resalta una casilla con el color rojo
     * @param fila la fila de la casilla
     * @param columna la columna de la casilla
     */
    public void resaltarCasilla(int fila, int columna){
        tableroSwing.getCasillas()[fila][columna].setBackground(java.awt.Color.red);
    }
    
    /**
     * Pinta todas las casillas del tablero a sus colores originales despues de 
     * resaltar casillas
     */
    public void repintarTablero(){
        for(int i=0; i<filas; i++){
          for(int j=0; j<columnas; j++){
                if( (i+j)%2 == 1){
                    tableroSwing.getCasillas()[i][j].setBackground(
                            java.awt.Color.lightGray);
                }
                else if ((i+j)%2 == 0){
                    tableroSwing.getCasillas()[i][j].setBackground(
                            java.awt.Color.white);
                }
          }
      }
    }

    /**
     * Actualiza todas las casillas del tablero grafico para adecuarlas al 
     * valor logico actual
     * @param o el tablero que se leera mediante un observer
     * @param arg Atributo heredado del metodo update original sin ningun uso en
     * esta implementacion
     */
    @Override
    public void update(Observable o, Object arg) {
        Tablero tablero = (Tablero) o;
        for( int cuentaFilas = 0; cuentaFilas < filas; cuentaFilas++ ){
            for(int cuentaColumnas = 0; cuentaColumnas < columnas; cuentaColumnas++){
                if( tablero.estaLaCasillaVacia(cuentaFilas, cuentaColumnas) ){
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas,null);
                }
                else if( tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, 
                        Ficha.BLANCA) ){
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, 
                            iconoFichaBlanca);
                    Ficha casillaObservada = 
                            tablero.getCasillero()[cuentaFilas][cuentaColumnas];
                    if( ! casillaObservada.isTransformable() )
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, 
                                iconoDamaBlanca);
                }
                else if( tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, 
                        Ficha.NEGRA) ){
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, 
                            iconoFichaNegra);
                    Ficha casillaObservada = 
                            tablero.getCasillero()[cuentaFilas][cuentaColumnas];
                    if( ! casillaObservada.isTransformable() )
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, 
                                iconoDamaNegra);
                }
            }
        }
    }
    
    /**
     * @return el tablero de Swing
     */
    public TableroSwing getTableroSwing(){
        return tableroSwing;
    }
    
    /**
     * Pide mediante mensajes los nombres de los dos jugadores
     * @return un vector con los dos jugadores
     */
    public String[] pedirJugadores(){
        String[] jugadores = new String[2];
        String jugador1 = "";
        String jugador2 = "";
        String jugador;
        
        while("".equals(jugador1)){
            jugador = (String)JOptionPane.showInputDialog(getContentPane(), 
                    "Introduce el nombre del jugador 1.");
            if ( ( jugador != null ) && ( jugador.length() > 0 ) ){
                    jugador1 = jugador;
                    jugadores[0] = jugador1;
                }
        }
        while("".equals(jugador2)){
            jugador = (String)JOptionPane.showInputDialog(getContentPane(), 
                    "Introduce el nombre del jugador 2.");
            if ( ( jugador != null ) && ( jugador.length() > 0 ) ){
                    jugador2 = jugador;
                    jugadores[1] = jugador2;
                }
        }
        
        return jugadores;
    }
    
    /**
     * Cambia el contenido del cuadro de texto de la interfaz principal
     * @param nuevoTexto el nuevo texto
     */
    public void cambiarTexto(String nuevoTexto){
        this.texto.setText(nuevoTexto);
    }
    
    /**
     * Crea un dialogo para seleccionar la ruta de guardado 
     * y el nombre del archivo
     * @return la ruta absoluta del archivo o null en caso de cancelar el proceso
     */
    public String guardarPartida(){
        JFileChooser dialogoGuardar = new JFileChooser();
        if(dialogoGuardar.showSaveDialog(null) == dialogoGuardar.APPROVE_OPTION){
            return dialogoGuardar.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    /**
     * Crea un dialogo para seleccionar el archivo que contiene una partida anterior
     * @return el fichero que contiene los datos de la partida anterior o null
     * en caso de cancelar el proceso
     */
    public File cargarPartida(){
        File f = null;
        JFileChooser dialogoCargar = new JFileChooser();
        FileNameExtensionFilter filtro = 
                new FileNameExtensionFilter("archivos .dat", "dat");
        dialogoCargar.setFileFilter(filtro);
        if(dialogoCargar.showOpenDialog(null) == dialogoCargar.APPROVE_OPTION){
            f = (File) dialogoCargar.getSelectedFile();
            return f;
        }
        return null;
    }
    
    /**
     * Marca en el tablero una posicion con el color verde para indicar que 
     * es un movimiento valido
     * @param fila la fila
     * @param columna la columna
     */
    public void mostrarMovimientoValido(int fila, int columna){
       tableroSwing.getCasillas()[fila][columna].setBackground(java.awt.Color.green);
    }
    
    /**
     * Muestra un panel modal con un mensaje que marca el vencedor y despues
     * vuelve al menu principal
     * @param texto que contiene el panel
     */
    public void mostrarFinal(String texto){
        JOptionPane.showMessageDialog(getContentPane(), texto);
        setUIInicio();
    }
    
    /**
     * Muestra un panel modal con un mensaje de error
     * @param texto el mensaje de error
     */
    public void mostrarError(String texto){
        JOptionPane.showMessageDialog(getContentPane(), texto);
    }
    
    /**
     * Pregunta al usuario por su nombre para utilizarlo en partidas multijugador
     * @return el nombre del jugador
     */
    public String preguntarNombre() {
        return (String)JOptionPane.showInputDialog(getContentPane(), 
                "Introduce tu nombre");
    }
    
    /**
     * Pregunta al usuario si acepta un reto y recoge la respuesta
     * @param nombreRival nombre del otro jugador
     * @return true si el cliente acepta el reto, false en caso contrario
     */
    public boolean preguntarReto(String nombreRival) {
        int respuesta = JOptionPane.showConfirmDialog(getContentPane(), 
                "Aceptas el reto de " + nombreRival + "?");
        return respuesta == JOptionPane.OK_OPTION;
    }
    
    /**
     * Obtiene el nombre del jugador seleccionado en la lista de jugadores
     * @return el nombre del otro jugador
     */
    public String getNombreJugadorRetado() {
        return listaJugadores.getSelectedValue().toString();
    }
    
    /**
     * Actualiza la lista de jugadores mediante la entrada de los nombres en 
     * un array de String
     * @param nombresJugadores la lista de jugadores
     */
    public void actualizarListaJugadores(String[] nombresJugadores) {
        modeloListaJugadores.clear();
        
        for (String nuevoJugador : nombresJugadores) {
            modeloListaJugadores.addElement(nuevoJugador);
        }
        
        botonRetar.setEnabled(false);
    }
    
    /**
     * Actualiza la lista de jugadores mediante la entrada de los nombres en 
     * un ArrayList
     * @param nombresJugadores la lista de jugadores
     */
    public void actualizarListaJugadores(ArrayList<String> nombresJugadores) {
        modeloListaJugadores.clear();
        
        for (String nuevoJugador : nombresJugadores) {
            modeloListaJugadores.addElement(nuevoJugador);
        }
        
        botonRetar.setEnabled(false);
    }
    
    /**
     * @param cliente el cliente que usa la vista
     */
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    /**
     * metodo main para la prueba de la interfaz
     * @param args 
     */
    public static void main(String[] args) {
        VistaJuego v = new VistaJuego("prueba GUI");
        Controlador c = new Controlador(new ReglasDamas());
        v.addControlador(c);
        c.vista(v);
    }
}
