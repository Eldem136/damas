/**
 * Partida.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import reglas.Reglas;
import utilidades.Consola;
import utilidades.Movimiento;

public class Partida implements Serializable {
    
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
    
    /**
     * Guarda la partida en un nuevo fichero
     * 
     * @param partida la partida que se guarda
     * @throws IOException 
     */
    public static void guardar(Partida partida) throws IOException{        
        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("partidasGuardadas/j1_VS_j2.dat"));
        out.writeObject(partida);
        out.close();
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
        Movimiento movimiento;
        boolean movimientoValido;
        iniciarConsola();
        do{
            this.guardar(this);
            consola.imprimirLinea("PARTIDA GUARDADA");
            
            turno++;
            consola.imprimirLinea(tablero.toString());
            
            consola.imprimir("TURNO DEL JUGADOR: ");
            consola.imprimirLinea( ( turno % 2 == 1 ) ? "BLANCO" : "NEGRO" );
            do {
                movimiento = leerMovimiento(( turno % 2 == 1 ) ? Ficha.BLANCA : Ficha.NEGRA );
                movimientoValido = reglas.movimientoValido(movimiento, tablero);
                if(!movimientoValido){
                    consola.imprimirError("No es un movimiento valido");
                }
            } while ( ! movimientoValido );
            
            tablero.moverFicha(movimiento);
            
            comerFicha(movimiento);
            
            hacerDama(movimiento);
            
            tablero.limpiarFichasMuertas();
            
            fin = finalizaLaPartida();
            
        } while ( ! fin );
    }
    
    /**
     * Lee un nuevo movimiento que sea del color del jugador indicado
     * 
     * @param color color que debe tener la ficha que moveremos
     * @return el movimiento, que debe pertenecer al jugador actual
     */
    private Movimiento leerMovimiento(String color) {
        int[] coordenadasMovimiento;
        coordenadasMovimiento = new int[ Movimiento.NUMERO_COORDENADAS_EN_MOVIMIENTO * 2 ];
        int posicionEnVectorCoordenadas = 0;
        
        for ( int i = 0; i < Movimiento.NUMERO_COORDENADAS_EN_MOVIMIENTO; i++ ) {
            int filaAuxiliar = consola.leerNumero("Introduzca la coordenada de fila");
            while ( filaAuxiliar < tablero.getFilaMinima() || 
                    filaAuxiliar > tablero.getFilaMaxima() ) {
                
                consola.imprimirLinea("Error, la fila debe estar comprendida entre " 
                        + tablero.getFilaMinima() + " y " 
                        + tablero.getFilaMaxima() + "\n");
                filaAuxiliar = consola.leerNumero("Introduzca la coordenada de fila");
            }
            coordenadasMovimiento[posicionEnVectorCoordenadas++] = filaAuxiliar;
            
            int columnaAuxiliar = consola.leerNumero("Introduzca la coordenada de columna");
            while ( columnaAuxiliar < tablero.getColumnaMinima()|| 
                    columnaAuxiliar > tablero.getColumnaMaxima()) {
                
                consola.imprimirLinea("Error, la columna debe estar comprendida entre " 
                        + tablero.getColumnaMinima()+ " y " 
                        + tablero.getColumnaMaxima()+ "\n");
                columnaAuxiliar = consola.leerNumero("Introduzca la coordenada de columna");
            }
            coordenadasMovimiento[posicionEnVectorCoordenadas++] = columnaAuxiliar;
        }
        if ( ! tablero.fichaDelMismoColor(coordenadasMovimiento[0], coordenadasMovimiento[1], color) ) {
            consola.imprimirError("Esa ficha no es de tu color");
            return leerMovimiento(color);
        }
        return new Movimiento(
            coordenadasMovimiento[0], // fila inicial
            coordenadasMovimiento[1], // columna inicial
            coordenadasMovimiento[2], // fila final
            coordenadasMovimiento[3]);// columna final
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
        
        
        if ( reglas.seTransforma(ficha, fila, tablero) )
            tablero.cambiarADama(fila, columna);
        
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

}
