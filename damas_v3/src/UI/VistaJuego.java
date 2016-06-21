/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import damas.Cliente;
import damas.Controlador;
import damas.Ficha;
import damas.Tablero;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.Observable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import reglas.ReglasDamas;

/**
 *
 * @author Zeko
 */
public class VistaJuego extends JFrame implements java.util.Observer {
    JLabel contador; 
    JLabel texto, logo, version;
    JPanel panelJuego;
    int filas, columnas;
    TableroSwing tableroSwing;
    static final String DEC = "Dec", INC = "Inc", INICIO = "Inicio";
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
    
    private DefaultListModel modeloListaJugadores;
    private JList listaJugadores;
    private JButton botonRetar;
    private JButton botonActualizarJugadores;
    private Cliente cliente;
    
     
    public VistaJuego(String titulo) {
        super(titulo);
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) { 
              if ( cliente != null )
                  cliente.cerrarCliente();
              System.exit(0);
          }
        });  
        
        
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
        
//        ImageIcon icono = new ImageIcon("recursos/checkers_image.png");
//        logo = new JLabel();
//        logo.setIcon(icono);
//        logo.setHorizontalAlignment(JLabel.CENTER);
//        logo.setVerticalAlignment(JLabel.CENTER);
//        getContentPane().add(logo);
        getContentPane().add(crearListaJugadores());
        
        version = new JLabel();
        version.setText(textoVersion);
        version.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(version, BorderLayout.SOUTH);
        
        
         
        //crearTableroSwing(10, 10);
        
        //tableroSwing.setVisible(false);
        //setBounds(250,200,250,150); 
//        setVisible(true); 
//        setResizable(false);
        setSize(600,500); setVisible(true);  
        setLocationRelativeTo(null);
        setResizable(false);
    }

   

    public void addControlador(ActionListener controlador){
        nuevo.addActionListener(controlador);
        guardar.addActionListener(controlador);
        cargar.addActionListener(controlador);
        botonRetar.addActionListener(controlador);
        botonActualizarJugadores.addActionListener(controlador);
        
    }
    public void addControladorDePartida(ActionListener controlador){
        tableroSwing.addControlador(controlador);
        btnRendirse.addActionListener(controlador);
    }
    
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
    
    public void setUIInicio(){
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());            
        texto = new JLabel("");
        getContentPane().add(texto, BorderLayout.NORTH);
        panelJuego = new JPanel();
        panelJuego.setLayout(new FlowLayout());
        getContentPane().add(panelJuego,BorderLayout.CENTER);
        
//        ImageIcon icono = new ImageIcon("recursos/checkers_image.png");
//        logo = new JLabel();
//        logo.setIcon(icono);
//        logo.setHorizontalAlignment(JLabel.CENTER);
//        logo.setVerticalAlignment(JLabel.CENTER);
//        getContentPane().add(logo);
        getContentPane().add(crearListaJugadores());
        
        version = new JLabel();
        version.setText(textoVersion);
        version.setHorizontalAlignment(JLabel.CENTER);
        getContentPane().add(version, BorderLayout.SOUTH);

        setSize(600,500); setVisible(true);  
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
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
    
    public void crearTableroSwing(int filas, int columnas){      
        
        this.filas = filas;
        this.columnas = columnas;
        tableroSwing = new TableroSwing(filas, columnas);
        panelJuego.add(tableroSwing);        
        setSize(tableroSwing.getAnchura(),
                tableroSwing.getAltura() + 50);
        setLocationRelativeTo(this);
        
        
    }
    public void limpiarTableroSwing(){
        if(tableroSwing != null){
            getContentPane().remove(tableroSwing);
        }
    }
    public void actualizarTableroSwing(Tablero tablero){
        
        for(int cuentaFilas = 0; cuentaFilas<filas; cuentaFilas++){
            for(int cuentaColumnas = 0; cuentaColumnas<columnas; cuentaColumnas++){
                if(tablero.estaLaCasillaVacia(cuentaFilas, cuentaColumnas)){
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, null);
                }
                else if(tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, Ficha.BLANCA)){
                    //tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "BLANCO");
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoFichaBlanca);
                    if(!tablero.getCasillero()[cuentaFilas][cuentaColumnas].isTransformable())
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoDamaBlanca);
                }
                else if(tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, Ficha.NEGRA)){
                    //tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "NEGRO");
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoFichaNegra);
                    if(!tablero.getCasillero()[cuentaFilas][cuentaColumnas].isTransformable())
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoDamaNegra);
                }
                
            }
        }
    }
    
    public void resaltarCasilla(int fila, int columna){
        tableroSwing.getCasillas()[fila][columna].setBackground(java.awt.Color.red);
    }
    
    public void repintarTablero(){
        for(int i=0; i<filas; i++){
          for(int j=0; j<columnas; j++){
                if( (i+j)%2 == 1){
                    tableroSwing.getCasillas()[i][j].setBackground(java.awt.Color.lightGray);
                }
                else if ((i+j)%2 == 0){
                    tableroSwing.getCasillas()[i][j].setBackground(java.awt.Color.white);
                }
          }
      }
    }

    @Override
    public void update(Observable o, Object arg) {
        Tablero tablero = (Tablero) o;
        for(int cuentaFilas = 0; cuentaFilas<filas; cuentaFilas++){
            for(int cuentaColumnas = 0; cuentaColumnas<columnas; cuentaColumnas++){
                if(tablero.estaLaCasillaVacia(cuentaFilas, cuentaColumnas)){
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, null);
                }
                else if(tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, Ficha.BLANCA)){
                    //tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "BLANCO");
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoFichaBlanca);
                    if(!tablero.getCasillero()[cuentaFilas][cuentaColumnas].isTransformable())
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoDamaBlanca);
                }
                else if(tablero.fichaDelMismoColor(cuentaFilas, cuentaColumnas, Ficha.NEGRA)){
                    //tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "NEGRO");
                    tableroSwing.textoEnCasilla(cuentaFilas, cuentaColumnas, "");
                    tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoFichaNegra);
                    if(!tablero.getCasillero()[cuentaFilas][cuentaColumnas].isTransformable())
                        tableroSwing.ponerIconoCasilla(cuentaFilas, cuentaColumnas, iconoDamaNegra);
                }
                
            }
        }
    }
    
    public TableroSwing getTableroSwing(){
        return tableroSwing;
    }
    
    public String[] pedirJugadores(){
        String[] jugadores = new String[2];
        String jugador1 = "";
        String jugador2 = "";
        String jugador = "";
        while(jugador1 == ""){
            jugador = (String)JOptionPane.showInputDialog(getContentPane(), "Introduce el nombre del jugador 1.");
            if((jugador != null) && (jugador.length() > 0)){
                    jugador1 = jugador;
                    jugadores[0] = jugador1;
                }
        }
        while(jugador2 == ""){
            jugador = (String)JOptionPane.showInputDialog(getContentPane(), "Introduce el nombre del jugador 2.");
            if((jugador != null) && (jugador.length() > 0)){
                    jugador2 = jugador;
                    jugadores[1] = jugador2;
                }
        }
        
        
        return jugadores;
    }
    
    public void cambiarTexto(String t){
        texto.setText(t);
    }
    
    public String guardarPartida(){
        JFileChooser dialogoGuardar = new JFileChooser();
        if(dialogoGuardar.showSaveDialog(null) == dialogoGuardar.APPROVE_OPTION){
            return dialogoGuardar.getSelectedFile().getAbsolutePath();
        }
        return null;
    }
    
    public File cargarPartida(){
        File f = null;
        JFileChooser dialogoCargar = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("archivos .dat", "dat");
        dialogoCargar.setFileFilter(filtro);
        if(dialogoCargar.showOpenDialog(null) == dialogoCargar.APPROVE_OPTION){
            f = (File) dialogoCargar.getSelectedFile();
            return f;
        }
        return null;
    }
    
    public void mostrarMovimientoValido(int fila, int columna){
        tableroSwing.getCasillas()[fila][columna].setBackground(java.awt.Color.green);
    }
    
    public void mostrarFinal(String texto){
        JOptionPane ventanaFin = new JOptionPane();
        ventanaFin.showMessageDialog(ventanaFin, texto);
        setUIInicio();
        
    }
    
    public void mostrarError(String texto){
        JOptionPane ventanaError = new JOptionPane();
        ventanaError.showMessageDialog(ventanaError, texto);
    }
    
    public String preguntarNombre() {
        return (String)JOptionPane.showInputDialog(getContentPane(), "Introduce tu nombre");
    }
    
    public boolean preguntarReto(String nombreRival) {
        int respuesta = JOptionPane.showConfirmDialog(getContentPane(), "Aceptas el reto de " + nombreRival + "?");
        return respuesta == JOptionPane.OK_OPTION;
    }
    
    public String getNombreJugadorRetado() {
        return listaJugadores.getSelectedValue().toString();
    }
    
    public void actualizarListaJugadores(String[] nombresJugadores) {
        modeloListaJugadores.clear();
        
        for (String nuevoJugador : nombresJugadores) {
            modeloListaJugadores.addElement(nuevoJugador);
        }
        
        botonRetar.setEnabled(false);
    }
    
    public void actualizarListaJugadores(ArrayList<String> nombresJugadores) {
        modeloListaJugadores.clear();
        
        for (String nuevoJugador : nombresJugadores) {
            modeloListaJugadores.addElement(nuevoJugador);
        }
        
        botonRetar.setEnabled(false);
    }
    
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }
    
    public static void main(String[] args) {
        VistaJuego v = new VistaJuego("prueba GUI");
        Controlador c = new Controlador(new ReglasDamas());
        v.addControlador(c);
        c.vista(v);
    }
}
