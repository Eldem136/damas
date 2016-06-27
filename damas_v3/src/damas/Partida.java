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
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import reglas.Reglas;
import utilidades.Consola;
import utilidades.Movimiento;

public class Partida  implements Serializable, java.awt.event.ActionListener{
        
    /* jugadores en la partida */
    private Jugador jugador1;
    private Jugador jugador2;
    
    /* atributos de la partida */
    public Tablero tablero;
    private Reglas reglas;
    private int turno; // si turno%2 == 1 --> es turno de las blancas
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
    
    /**
     * inicializa la entrada/salida por teclado y consola
     */
    public void iniciarConsola() {
        this.consola = new Consola();
    }
    
    /**
     * Inicia el tablero grafico realizado usando Java Swing
     */
    public void iniciarTableroSwing(){
        vista.crearTableroSwing(tablero.getFilaMaxima()+1, 
                tablero.getColumnaMaxima()+1);
    }
    
    /**
     * Guarda la partida en un nuevo fichero
     * 
     * @param partida la partida que se guarda
     * @throws IOException 
     */
    public boolean guardar(Partida partida) throws IOException{       
        String ruta = vista.guardarPartida();
        if(ruta != null){
            turno--; //correccion para turnos duplicados al guardar/cargar
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
     * @throws IOException si se produce error de entrada/salida con el lector
     * @throws ClassNotFoundException si el archivo contiene otra cosa
     */
    public static Partida cargar(File partida) throws IOException, 
            ClassNotFoundException{
        ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(partida));
        Partida p1 = (Partida) in.readObject();
        in.close();
        return p1;

    }
    
    /**
     * Comienza una partida iniciando el primer turno
     * 
     * Una partida consiste en una sucesion de turnos que empiezan en nuevoTurno
     * y terminan en terminarTurno
     * 
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
    }
   
    /**
     * Comienza un nuevo turno y actualiza la interfaz
     */
    private void nuevoTurno() {
        vista.update(tablero, null);

        turno++;

        vista.cambiarTexto("Turno de las fichas " + ((turno % 2 == 1)
                ? "Blancas. (" + jugador1.getNombre() + ")"
                : "Negras. (" + jugador2.getNombre() + ")"));
    }
    
    /**
     * Comprueba que el movimiento solicitado es legal y de ser asi lo aplica
     * y cambia de turno o termina la partida, segun proceda
     */
    private void terminarTurno(){
        boolean turnoValido;
        boolean movimientoValido;
        
        turnoValido = comprobarColorMovimiento(movimiento);
        movimientoValido = reglas.movimientoValido(this.movimiento, tablero);
        if ( ! turnoValido ) {
            this.movimiento = null;
        }
        else if ( ! movimientoValido ) {
            this.movimiento = null;
        } else {
            tablero.moverFicha(this.movimiento);
            comerFicha(this.movimiento);          
            hacerDama(this.movimiento);            
            tablero.limpiarFichasMuertas();            
            fin = finalizaLaPartida();  
            if( fin == null )
                nuevoTurno();
            else
                vista.mostrarFinal(fin);
        }
    }
    
    /**
     * Comprueba que el movimiento indicado es del color del jugador actual
     * 
     * @param color color que debe tener la ficha que moveremos
     * @return true si el movimiento es del color correcto
     * false si el movimiento no es del color correcto
     */
    private boolean comprobarColorMovimiento(Movimiento movimiento) {   
        String color = (turno % 2 == 1) ? Ficha.BLANCA : Ficha.NEGRA;
        int fila = movimiento.getFilaInicial();
        int columna = movimiento.getColInicial();
        if ( ! tablero.fichaDelMismoColor(fila, columna, color) ) {
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
        
        tablero.matarFicha(coordenadasFichaComida[0], coordenadasFichaComida[1]);
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
        }
        
    }
    
    /**
     * comprobamos si finaliza la partida, en el casi de que si indicamos de 
     * ello por la consola asignada
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
        return texto;
    }
    
    /**
     * Termina la partida por rendicion de uno de los jugadores
     * 
     * @param nombreGanador el nombre del jugador ganador
     */
    public void conceder(String nombreGanador){
        fin = "Gana el jugador " + nombreGanador;
        vista.mostrarFinal(fin);
    }

    /**
     * Sobreescribe el actionPerformed que se utilizara para el tablero
     * @param e evento entrante
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getActionCommand().equals("BLANCO") || 
                e.getActionCommand().equals("NEGRO") ||
                e.getActionCommand().equals("")){
            CasillaSwing c = (CasillaSwing) e.getSource();
            CasillaSwing[][] cs = vista.getTableroSwing().getCasillas();
            
            vista.resaltarCasilla(c.getFila(), c.getColumna());
            if(primeraParteMovimiento == false){
                filaInicial = c.getFila();
                columnaInicial = c.getColumna();
                movimiento = new Movimiento(filaInicial, columnaInicial, 0, 0);
                primeraParteMovimiento = true;
                movimientosValidos();
            }
            else{
                movimiento = new Movimiento(filaInicial, columnaInicial, 
                        c.getFila(), c.getColumna());
                movimientoListo = true;
                vista.repintarTablero();
                primeraParteMovimiento = false;
                terminarTurno();
            }  
        } else if(e.getActionCommand().equals("Rendirse")){
            conceder( (turno % 2 == 1) ? jugador2.getNombre() : 
                    jugador1.getNombre() );
        }
       
    }
    
    /**
     * @return el tablero
     */
    public Tablero getTablero(){
        return tablero;
    }
    
    /**
     * @param v la vista
     */
    public void vista(VistaJuego v){ 
        this.vista = v; 
    }

    /**
    * Calcula cuales son los movimientos validos a los que puede moverse
    * la ficha seleccionada y envia la informacion a la vista para marcarlos
    * en el tablero
    */
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
    private ArrayList<Movimiento> movimientosPeonValidos(int fila, int columna, 
            String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>(2);
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        int avanceFila = ( color.equals(Ficha.BLANCA) ? -1 : 1 );
        int filaFinal = fila + avanceFila;
        int columnaFinal = columna - 1;
        if ( tablero.estaLaCasillaVacia(filaFinal, columnaFinal) ) {
            movimientosValidos.add(
                    new Movimiento(fila, columna, filaFinal, columnaFinal));
        }
        else if ( ! tablero.fichaDelMismoColor(filaFinal, columnaFinal, color)) {
            filaFinal += avanceFila;
            columnaFinal--;
            if ( tablero.estaLaCasillaVacia(filaFinal, columnaFinal) )
                movimientosValidos.add(
                        new Movimiento(fila, columna, filaFinal, columnaFinal));
        }
        filaFinal = fila + avanceFila;
        columnaFinal = columna + 1;
        if ( tablero.estaLaCasillaVacia(filaFinal, columnaFinal) ) {
            movimientosValidos.add(
                    new Movimiento(fila, columna, filaFinal, columnaFinal));
        }
        else if ( ! tablero.fichaDelMismoColor(filaFinal, columnaFinal, color)) {
            filaFinal += avanceFila;
            columnaFinal++;
            if ( tablero.estaLaCasillaVacia(filaFinal, columnaFinal) )
                movimientosValidos.add(
                        new Movimiento(fila, columna, filaFinal, columnaFinal));
        }      
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
    private ArrayList<Movimiento> movimientosDamaValidos(int fila, int columna, 
            String color) {
        
        ArrayList<Movimiento> movimientosValidos = new ArrayList<>();
        
        if ( color.equals(Ficha.VACIA) )
            return movimientosValidos;
        
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, 1, color, 
                movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, 1, -1, color, 
                movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, 1, color, 
                movimientosValidos);
        rellenarMovimientosDamaEnDireccion(fila, columna, -1, -1, color, 
                movimientosValidos);
        
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
     *  añadira los que encuentre
     */
    private void rellenarMovimientosDamaEnDireccion(int fila, int columna, 
            int aumentoFila, int aumentoColumna, String color, 
            ArrayList<Movimiento> movimientos) {
        
        boolean finComprobacion = false;
        boolean fichaComida = false;
        
        int filaInvestigada = fila + aumentoFila;
        int columnaInvestigada = columna + aumentoColumna;
        
        while ( ! finComprobacion  )  {
            
            if ( tablero.estaLaCasillaVacia(filaInvestigada, columnaInvestigada) )
                movimientos.add(new Movimiento(fila, columna, filaInvestigada, 
                        columnaInvestigada));
            else if ( ! tablero.fichaDelMismoColor
                        (filaInvestigada, columnaInvestigada, color) 
                    && ! fichaComida)
                fichaComida = true;
            else
                finComprobacion = true;
            
            filaInvestigada += aumentoFila;
            columnaInvestigada += aumentoColumna;
            
        }
        
    }
    

}
