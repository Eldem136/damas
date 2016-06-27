/**
 * Reglas.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package reglas;

import damas.Ficha;
import utilidades.Movimiento;
import damas.Tablero;

public interface Reglas {
    
    public static int[] COORDENADAS_NO_COMER = {-1, -1};
    
    public static final int SIN_GANADOR = 0;
    public static final int GANADOR_JUGADOR_1 = 1;
    public static final int GANADOR_JUGADOR_2 = 2;
    public static final int EMPATE = 3;
    
    
    /**
     * comprueba si un movimiento es valido en el tablero
     * 
     * @param movimiento el movimiento
     * @param tablero  el tablero
     * @return true si el movimiento es valido
     */
    public boolean movimientoValido(Movimiento movimiento, Tablero tablero);
    /**
     * retorna si hay ganador en el tablero, tomando el id del jugador 1 para 
     * identificar al jugador 1 e indicar cual de los jugadores es el ganador
     * 
     * @param tablero sobre el que comprueba si hay ganador
     * @param idJugador1 identificador para el jugador 1
     * @return 
     * Reglas.GANADOR_JUGADOR_1 si gana el jugador 1
     * Reglas.GANADOR_JUGADOR_2 si gana el jugador 2
     * Reglas.SIN_GANADOR si no gana ningun jugador
     */
    public int hayGanador(Tablero tablero, String idJugador1);
    /**
     * si un movimiento come una ficha la indica
     * @param movimiento movimiento realizado
     * @param tablero tablero donde se ejecuta el movimiento
     * @param colorQueCome color de la ficha que esta comiendo
     * @return la fila y columna de la ficha que se come y un array que 
     *  contiene una posicion fuera si no come ninguna ficha
     */
    /**
     * comprueba si tras realizar un movimiento se "come" una ficha enemiga e indica 
     * en tal caso cual es la ficha comida
     * 
     * @param movimiento movimiento realizado
     * @param tablero tablero donde se ejecuta el movimiento
     * @return 
     * un array de dimension 2 con la fila en el tablero de la ficha comida en la 
     * posicion 0 
     *  y la columna en la posicion 1
     * Reglas.COORDENADAS_NO_COMER en el caso de que no se "come" ninguna ficha
     */
    public int[] comeFicha(Movimiento movimiento, Tablero tablero);
    /**
     * comprueba si la ficha en dada en la posicion indicada del tablero indicado 
     * debe transformarse en otra
     * 
     * @param ficha la ficha que comprobamos
     * @param fila la fila en el tablero donde comprobamos la transformacion
     * @param tablero el tablero donde comprobamos la transformacion
     * @return 
     * true si la ficha se transforma
     * false si la ficha no se transforma
     */
    public boolean seTransforma(Ficha ficha, int fila, Tablero tablero);
}
