/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v1;

/**
 *
 * @author zeko
 */
public enum CodigoError {
    COLUMNA_LLENA("Esa columna está llena, elige otra."),
    COLUMNA_NO_VALIDA("Introduce un número del 1 al 10."),
    ENTRADA_NO_NUMERICA("Introduce un número entero."),
    ERROR_GUARDAR("No se pudo guardar la partida."),
    ERROR_CARGAR("No se pudo cargar la partida."),
    NO_HAY_FICHEROS_GUARDADOS("No hay partidas guardadas."),
    NUMERO_NO_LISTADO("Introduce un numero de la lista."),
    ERROR_AL_ABRIR_ARCHIVO("Ha habido un error al abrir el archivo."),
    NO_ERROR("");
    
    private final String mensaje;
    
    
    CodigoError(String mensaje){
        this.mensaje = mensaje;
    }
    
    public String getMensaje(){
        return mensaje;
    }
    
}
