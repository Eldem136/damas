/**
 * Partida.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import UI.CasillaSwing;
import UI.VistaJuego;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import reglas.Reglas;
import reglas.ReglasDamas;
import utilidades.Consola;
import utilidades.Movimiento;

public class Partida  implements Serializable, java.awt.event.ActionListener{
        
    /* jugadores en la partida */
    private Jugador jugador1;
    private Jugador jugador2;
    
    /* atributos de la partida */
    public Tablero tablero;
    private Reglas reglas;
    private int turno;
    private boolean fin;
    
    /* consola que se emplea para la comunicacion por linea de comandos */
    private transient Consola consola;
    
    private VistaJuego vista;
    
    int filaInicial, columnaInicial;
    private Movimiento movimiento = null;
    private boolean movimientoListo = false;
    
    /**
     * Crea una nueva partida con un nuevo tablero, dos jugadores nuevos 
     * y las reglas suministradas
     * 
     * @param nombre1 el nombre del jugador 1
     * @param nombre2 el nombre del jugador 2
     * @param reglas las reglas que se seguiran en la partida
     */
    public Partida(String nombre1, String nombre2, Reglas reglas){
        this.tablero = new Tablero();
        this.jugador1 = new Jugador(nombre1, Ficha.BLANCA);
        this.jugador2 = new Jugador(nombre2, Ficha.NEGRA);
        this.reglas = reglas;
        this.turno = 0;
        this.fin = false;
        
        this.tablero.colocarFichas();
    }
    
    /**
     * inicializa la entrada/salida por teclado y consola
     */
    public void iniciarConsola() {
        this.consola = new Consola();
    }
    
    
    public void iniciarTableroSwing(){
        vista.crearTableroSwing(tablero.getFilaMaxima()+1, tablero.getColumnaMaxima()+1);
        
    }
    
    /**
     * Guarda la partida en un nuevo fichero
     * 
     * @param partida la partida que se guarda
     * @throws IOException 
     */
    public static boolean guardar(Partida partida) throws IOException{        
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("partidasGuardadas/j1_VS_j2.dat"));
        out.writeObject(partida);
        out.close();
        return true;
    }
    
    /**
     * Carga una partida comenzada anteriormente
     * 
     * @return la partida que ha sido cargada
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Partida cargar() throws IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream("partidasGuardadas/j1_VS_j2.dat"));
        Partida partida = (Partida) in.readObject();
        in.close();
        return partida;

    }
    
    /**
     * Comienza una partida
     * La partida consiste en una iteracion hasta que hay un ganador
     * El bucle guarda la partida antes de cada turno, muestra el teclado, 
     * pide un movimiento y comprueba que sea válido, mueve la ficha, comprueba 
     * si se comen fichas o se combierten damas y si se termina la partida
     * 
     * @throws IOException 
     */
    public void jugar() throws IOException{
        boolean movimientoValido, turnoValido;
        iniciarConsola();
        iniciarTableroSwing();
        vista.addControlador(this);
        do{
            vista.actualizarTableroSwing(tablero);
            consola.imprimirLinea("PARTIDA GUARDADA");
            
            turno++;
            consola.imprimirLinea(tablero.toString());
            guardar(this);
            consola.imprimir("TURNO DEL JUGADOR: ");
            consola.imprimirLinea( ( turno % 2 == 1 ) ? "BLANCO" : "NEGRO" );
            do {
//                movimiento = leerMovimiento(( turno % 2 == 1 ) ? Ficha.BLANCA : Ficha.NEGRA );
//                movimientoValido = reglas.movimientoValido(movimiento, tablero);
//                if ( ! movimientoValido ) {
//                    consola.imprimirError("No es un movimiento valido");
//                }
                while(!movimientoListo){
                    try {
                        Thread.sleep(100);
                        //System.err.println("movimiento no listo");
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                turnoValido = leerTurnoMovimiento(movimiento, (turno % 2 == 1) ? Ficha.BLANCA : Ficha.NEGRA);
                movimientoValido = reglas.movimientoValido(this.movimiento, tablero);
                if(!turnoValido){
                    this.movimiento = null;
                    this.movimientoListo = false;
                }
                if(!movimientoValido){
                    this.movimiento = null;
                    this.movimientoListo = false;
                }
                
                
            } while ( ! movimientoValido || ! turnoValido);
            movimientoListo = false;
            tablero.moverFicha(this.movimiento);
            comerFicha(this.movimiento);          
            hacerDama(this.movimiento);            
            tablero.limpiarFichasMuertas();            
            fin = finalizaLaPartida();   
            this.movimiento = null;
        } while ( ! fin );
    }
    
    /**
     * Lee un nuevo movimiento que sea del color del jugador indicado
     * 
     * @param color color que debe tener la ficha que moveremos
     * @return el movimiento, que debe pertenecer al jugador actual
     */
    private boolean leerTurnoMovimiento(Movimiento movimiento, String color) {        
        if ( ! tablero.fichaDelMismoColor(movimiento.getFilaInicial(), movimiento.getColInicial(), color) ) {
            consola.imprimirError("Esa ficha no es de tu color");
            return false;
        }
        return true;
    }
    
    /**
     * comprueba si el movimiento realizado se come alguna ficha enemiga 
     * y ejecuta la accion de matarla
     * 
     * @param movimiento el movimiento que realizamos
     */
    private void comerFicha(Movimiento movimiento) {
        
        int[] coordenadasFichaComida = 
                reglas.comeFicha(movimiento, tablero);
        
        System.err.println(coordenadasFichaComida[0]+","+coordenadasFichaComida[1]);
        tablero.matarFicha(coordenadasFichaComida[0], coordenadasFichaComida[1]);
        //System.err.println("coord ficha comida:"+coordenadasFichaComida[0]+","+coordenadasFichaComida[1]);
    }
    
    /**
     * comprueba si despues del movimiento la ficha sobre la que lo hemos realizado 
     * se convierte en una dama y si es asi la convertimos
     * 
     * @param movimiento el movimiento que realizamos
     */
    private void hacerDama(Movimiento movimiento) {
        
        int fila = movimiento.getFilaFinal();
        int columna = movimiento.getColFinal();
        
        Ficha ficha = tablero.getFicha(fila, columna);
        
        
        if ( reglas.seTransforma(ficha, fila, tablero) ){
            tablero.cambiarADama(fila, columna);
            System.err.println("hacemos dama");
        }
        
    }
    
    /**
     * comprobamos si finaliza la partida, en el casi de que si indicamos de ello por la consola asignada
     * 
     * @return 
     * true si la partida finaliza
     * false si la partida no finaliza
     */
    private boolean finalizaLaPartida() {
        int ganador = reglas.hayGanador(tablero, jugador1.getColorFicha());
        
        if ( ganador == Reglas.SIN_GANADOR )
            return false;
        
        consola.imprimir("Gana el jugador ");
        consola.imprimirLinea( ganador == Reglas.GANADOR_JUGADOR_1 ? 
                jugador1.getNombre() : jugador2.getNombre() );
        
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        CasillaSwing c = (CasillaSwing) e.getSource();
        System.out.println("fila:"+c.getFila()+" columna:"+c.getColumna());
        if(e.getActionCommand().equals("BLANCO") || 
                e.getActionCommand().equals("NEGRO") ||
                e.getActionCommand().equals("")){
            if(movimiento == null){
                System.err.println("cojo primera");
                filaInicial = c.getFila();
                columnaInicial = c.getColumna();
                movimiento = new Movimiento(filaInicial, columnaInicial, 0, 0);
            }
            else{
                System.err.println("cojo segunda");
                movimiento = new Movimiento(filaInicial, columnaInicial, c.getFila(), c.getColumna());
                movimientoListo = true;
            }
                
            
        }
    }
    
    public void vista(VistaJuego v){ 
        this.vista = v; 
    }

}
