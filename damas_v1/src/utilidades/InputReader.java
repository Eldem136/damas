/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author Zeko
 */
public class InputReader {
    
    
    
    Scanner scan;
    public InputReader(){
        scan = new Scanner(System.in);
    }
    
    public Movimiento leeMov(){
        int fil1;
        int fil2;
        int col1;
        int col2;
        try {
            System.out.println("Introduce la coordenada horizontal de la ficha que quieras mover");
            fil1 = scan.nextInt();
            while(0 > fil1 || fil1 >= 8){
                System.err.println("Introduce un valor del 0 al 7");
                fil1 = scan.nextInt();
            }
            
            System.out.println("Introduce la coordenada vertical de la ficha que quieras mover");
            col1 = scan.nextInt();
            while(0 > col1 || col1 >= 8){
                System.err.println("Introduce un valor del 0 al 7");
                col1 = scan.nextInt();
            }
            
            System.out.println("Introduce la coordenada horizontal donde quieras mover");
            fil2 = scan.nextInt();
            while(0 > fil2 || fil2 >= 8){
                System.err.println("Introduce un valor del 0 al 7");
                fil2 = scan.nextInt();
            }
            
            System.out.println("Introduce la coordenada vertical donde quieras mover");
            col2 = scan.nextInt();
            while(0 > col2 || col2 >= 8){
                System.err.println("Introduce un valor del 0 al 7");
                col2 = scan.nextInt();
            }
            
        } catch (InputMismatchException e){
            System.err.println("Introduce solo números del 0 al 7");
            scan.nextLine();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            scan.nextLine();
            return null;
        }
        
        return new Movimiento(fil1, col1, fil2, col2);
        
    }
    
}
