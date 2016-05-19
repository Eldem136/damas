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
import java.util.ArrayList;
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
    private String fin;
    
    /* consola que se emplea para la comunicacion por linea de comandos */
    private transient Consola consola;
    
    private VistaJuego vista;
    
    int filaInicial, columnaInicial;

    private transient Movimiento movimiento = new Movimiento(0, 0, 0, 0);
    private boolean movimientoListo = false;
    private boolean primeraParteMovimiento = false;
    
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
        this.fin = null;
        
        this.tablero.colocarFichas();
    }
    
    public void reiniciar() throws IOException{
        this.tablero.colocarFichas();
        this.turno = 0;
        this.fin = null;
        jugar();
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
    public boolean guardar(Partida partida) throws IOException{        
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("partidasGuardadas/j1_VS_j2.dat"));
//        out.writeObject(partida);
//        out.close();
        String ruta = vista.guardarPartida();
        if(ruta != null){
            turno--; //corregir doble paso de turno por guardar y ejecutar nuevo turno dos veces
            File f = new File(ruta+".dat");
            f.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(f));
            out.writeObject(partida);
            out.close();
        }
        else{
            vista.cambiarTexto("No se puede guardar la partida.");
        }
        
        return true;
    }
    
    /**
     * Carga una partida comenzada anteriormente
     * 
     * @param partida
     * @return la partida que ha sido cargada
     * @throws IOException 
     * @throws ClassNotFoundException 
     */
    public static Partida cargar(File partida) throws IOException, ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(partida));
        Partida p1 = (Partida) in.readObject();
        in.close();
        return p1;

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
    

    
    
    public synchronized void jugar(){
        boolean movimientoValido = false, turnoValido = false;
        movimiento = null;
        primeraParteMovimiento = false;
        iniciarConsola();
        vista.setUIJuego();
        iniciarTableroSwing();
        vista.addControladorDePartida(this);
        
        nuevoTurno();
//        do{
//            System.err.println("llego hasta aqui");
//            vista.actualizarTableroSwing(tablero);
//            consola.imprimirLinea("PARTIDA GUARDADA");
//            
//            turno++;
//            consola.imprimirLinea(tablero.toString());
//           // guardar(this);
//            consola.imprimir("TURNO DEL JUGADOR: ");
//            consola.imprimirLinea( ( turno % 2 == 1 ) ? "BLANCO" : "NEGRO" );
            
//            do {
//                 System.err.println("llego hasta aqui");
////                movimiento = leerMovimiento(( turno % 2 == 1 ) ? Ficha.BLANCA : Ficha.NEGRA );
////                movimientoValido = reglas.movimientoValido(movimiento, tablero);
////                if ( ! movimientoValido ) {
////                    consola.imprimirError("No es un movimiento valido");
////                }
////                while(!movimientoListo){
////                    System.err.println("qui estoy");
////                    try {
////                        Thread.sleep(100);
////                        //System.err.println("movimiento no listo");
////                    } catch (InterruptedException ex) {
////                        Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
////                    }
////                }
//                try {
//                    wait();
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                
//                
//                
//                //if(movimientoListo){
//                    turnoValido = leerTurnoMovimiento(movimiento, (turno % 2 == 1) ? Ficha.BLANCA : Ficha.NEGRA);
//                    movimientoValido = reglas.movimientoValido(this.movimiento, tablero);
//                    if(!turnoValido){
//                        this.movimiento = null;
//                        this.movimientoListo = false;
//                    }
//                    if(!movimientoValido){
//                        this.movimiento = null;
//                        this.movimientoListo = false;
//                    }
//               //}
//                
//            } while ( ! movimientoValido || ! turnoValido);
//            movimientoListo = false;
//            tablero.moverFicha(this.movimiento);
//            comerFicha(this.movimiento);          
//            hacerDama(this.movimiento);            
//            tablero.limpiarFichasMuertas();            
//            fin = finalizaLaPartida();  
    //    } while ( ! fin );
    }
    
    private void nuevoTurno() {
         vista.actualizarTableroSwing(tablero);
        
           turno++;
            System.err.println("turno "+turno);
        
        vista.cambiarTexto("Turno de las fichas "+((turno % 2 == 1) ?
                "Blancas. ("+jugador1.getNombre()+")" :
                "Negras. ("+jugador2.getNombre()+")"));
           
    }
    
    private void terminarTurno(){
        boolean turnoValido = false;
        boolean movimientoValido = false;
        turnoValido = leerTurnoMovimiento(movimiento, (turno % 2 == 1) ? Ficha.BLANCA : Ficha.NEGRA);
        movimientoValido = reglas.movimientoValido(this.movimiento, tablero);
        if(!turnoValido){
            this.movimiento = null;
            this.movimientoListo = false;
        }
        else if(!movimientoValido){
            this.movimiento = null;
            this.movimientoListo = false;
        }else{
                
        movimientoListo = false;
        tablero.moverFicha(this.movimiento);
        comerFicha(this.movimiento);          
        hacerDama(this.movimiento);            
        tablero.limpiarFichasMuertas();            
        fin = finalizaLaPartida();  
        if(fin == null)
            nuevoTurno();
        else
            vista.mostrarFinal(fin);
        }
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
            vista.cambiarTexto("Esa ficha no es de tu color");
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
    private String finalizaLaPartida() {
        int ganador = reglas.hayGanador(tablero, jugador1.getColorFicha());
        String texto;
        
        switch (ganador) {
            case Reglas.SIN_GANADOR:
                return null;
            case Reglas.GANADOR_JUGADOR_1:
                texto = "Gana el jugador: "+jugador1.getNombre();
                break;
            case Reglas.GANADOR_JUGADOR_2:
                texto = "Gana el jugador: "+jugador2.getNombre();
                break;
            default:
                texto = "¡Hay un empate!";
                break;
        }
        
//        consola.imprimir("Gana el jugador ");
//        consola.imprimirLinea( ganador == Reglas.GANADOR_JUGADOR_1 ? 
//                jugador1.getNombre() : jugador2.getNombre() );
        
        return texto;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        
        //System.out.println("fila:"+c.getFila()+" columna:"+c.getColumna());
        if(e.getActionCommand().equals("BLANCO") || 
                e.getActionCommand().equals("NEGRO") ||
                e.getActionCommand().equals("")){
            CasillaSwing c = (CasillaSwing) e.getSource();
            CasillaSwing[][] cs = vista.getTableroSwing().getCasillas();
            
           
            
            vista.resaltarCasilla(c.getFila(), c.getColumna());
            if(primeraParteMovimiento == false){
                System.err.println("cojo primera");
                filaInicial = c.getFila();
                columnaInicial = c.getColumna();
                movimiento = new Movimiento(filaInicial, columnaInicial, 0, 0);
                primeraParteMovimiento = true;
                movimientosValidos();
            }
            else{
                System.err.println("cojo segunda");
                movimiento = new Movimiento(filaInicial, columnaInicial, c.getFila(), c.getColumna());
                movimientoListo = true;
                vista.repintarTablero();
                primeraParteMovimiento = false;
                terminarTurno();
            }  
        } else if(e.getActionCommand().equals("Nueva partida")){
            System.err.println("nueva");
        }
       
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    public void vista(VistaJuego v){ 
        this.vista = v; 
    }

    private void movimientosValidos(){
        int fila = movimiento.getFilaInicial();
        int columna = movimiento.getColInicial();
        boolean esUnPeon;
        String colorFicha;
        ArrayList<Movimiento> movimientosValidos;
        
        if ( ! tablero.estaLaCasillaVacia(fila, columna) ) {
            
            esUnPeon = tablero.getFicha(fila, columna).isTransformable();
            colorFicha = tablero.getFicha(fila, columna).getColor();
            if ( ! colorFicha.equals( (turno%2 == 1) ? Ficha.BLANCA : Ficha.NEGRA ) )
                return;
            else if ( esUnPeon )
                movimientosValidos = movimientosPeonValidos(fila, columna, colorFicha);
            else
                movimientosValidos = movimientosDamaValidos(fila, columna, colorFicha);
         
            
            for (Movimiento m : movimientosValidos) {
                System.err.println("Es valido el movimiento: " + m.getFilaFinal()+","+m.getColFinal());
                vista.mostrarMovimientoValido(m.getFilaFinal(), m.getColFinal());
            }
            
        }
        
    }
    
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
    

}
