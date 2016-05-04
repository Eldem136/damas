/**
 * damas_v1.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import reglas.*;

public class damas_v1 {

    /**
     * main
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Reglas reglas = new ReglasDamas();
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Damas_V1 \n-------------------------------");
        System.out.println("1 - Nueva partida.\n2 - Cargar Partida");
        System.out.println("-------------------------------");
        
        int opcion = scan.nextInt();
        
        File directorio = new File("partidasGuardadas");
        if ( ! directorio.isDirectory() ) {
            directorio.mkdir();
        }
        
        try {

            if ( opcion == 1 ) {
                new Partida("jugador1", "jugador2", reglas).jugar();
            }
            else if ( opcion == 2 ) {
                Partida.cargar().jugar();
            }
        } catch ( IOException ex) {
            System.err.println("Ha ocurrido una excepcion de entrada/salida");
            
        } catch ( ClassNotFoundException ex ) {
            System.err.println("Ha ocurrido una excepcion de clase no encontrada");
        }
    }
}
