/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import UI.TableroSwing;
import UI.VistaJuego;
import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
import javax.swing.JFrame;
import oracle.jrockit.jfr.tools.ConCatRepository;
import reglas.*;
import utilidades.Movimiento;
/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class damas_v2 {
    private static String[] ficheros;
    private static int eleccion;
    private static Scanner scan2 = new Scanner(System.in);

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        
        
        
        Partida partida = null;
        VistaJuego vista;
        Tablero tablero;
        Controlador controlador;
        Reglas reglas = new ReglasDamas();
        vista = new VistaJuego("damas");
        
        
        System.out.println("Damas_V2");
        //System.out.println("1 - Nueva partida.\n2 - Cargar Partida");
       // System.out.println("-------------------------------");
       
       // int opcion = scan.nextInt();
        
        File directorio = new File("partidasGuardadas");
        if(!directorio.isDirectory()){
            directorio.mkdir();
        } 
        
//        if(opcion ==1){
//            partida = new Partida("jugador1", "jugador2", reglas);
//            tablero = partida.tablero;
//            tablero.addObserver(vista);
//            partida.vista(vista);
//            
//            
//        }
//        else if(opcion == 2){
//            partida = Partida.cargar();
//            tablero = partida.tablero;
//            tablero.addObserver(vista);
//            partida.vista(vista);
////            Tablero t2 = new Tablero();
////            t2.colocarFichas();
////            
////        vista.crearTableroSwing(t2.getFilaMaxima()+1, t2.getColumnaMaxima()+1);
////        vista.actualizarTableroSwing(t2);
//        }
//            partida = new Partida("jugador1", "jugador2", reglas);
//            System.err.println("creada");
//            tablero = partida.tablero;
//            tablero.addObserver(vista);
//            partida.vista(vista);

        

//            partida = new Partida("jugador1", "jugador2", reglas); 
//            tablero = partida.tablero;
//            tablero.addObserver(vista);
//            partida.vista(vista);
//            partida.jugar();
        
            controlador = new Controlador(reglas);
            tablero = new Tablero();
            tablero.addObserver(vista);
            controlador.vista(vista);
            vista.addControlador(controlador);
            
        }
    
        
        
}
