/**
 * Consola.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package utilidades;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Consola implements Serializable {

    private final transient Scanner scan;
    
    /**
     * crea un nuevo lector de texto desde System.in
     */
    public Consola(){
        scan = new Scanner(System.in);
    }
    
    
    /**
     * imprime una pregunta al usuario y regoge su respuesta que debe ser un numero
     * 
     * @param preguntaUsuario la pregunta que le realiza al usuario
     * @return el numero que ha leido como respuesta
     */
    public int leerNumero(String preguntaUsuario) {
        
        System.out.println(preguntaUsuario);
        
        try {
            return scan.nextInt();
        } catch (InputMismatchException ex) {
            System.err.println("No ha sido introducido un numero, "
                    + "por favor introduce un numero");
            scan.next(); //descartamos el tokken siguiente porque no es un numero
            return leerNumero(preguntaUsuario);
        }
        
    }
    
    /**
     * imprime una pregunta al usuario y regoge su respuesta
     * 
     * @param preguntaUsuario la pregunta que le realiza al usuario
     * @return la respuesta del usuario
     */
    public String leerLinea(String preguntaUsuario){
        return scan.nextLine();
    }
    
    /**
     * imprime un texto por la consola sin retorno de carro
     * @param texto el texto que imprimira
     */
    public void imprimir(String texto) {
        
        System.out.print(texto);
        
    }
    
    /**
     * imprime un texto por la consola con retorno de carro
     * @param texto el texto que imprimira
     */
    public void imprimirLinea(String texto) {
        
        System.out.println(texto);
        
    }
    
    /**
     * imprime un texto por la salida estandard de error
     * @param texto el texto que imprimira
     */
    public void imprimirError(String texto) {
        
        System.err.println(texto);
        
    }
    
}
