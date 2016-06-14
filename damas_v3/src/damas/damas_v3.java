/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

import UI.TableroSwing;
import UI.VistaJuego;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import oracle.jrockit.jfr.tools.ConCatRepository;
import reglas.*;
import utilidades.Movimiento;
/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class damas_v3 {
    private static String[] ficheros;
    private static int eleccion;
    private static Scanner scan2 = new Scanner(System.in);
    

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException{
        try {
            Socket socket = new Socket("localhost", 10000);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            boolean fin = false;
            
            
            System.out.println(entrada.readLine());
            System.out.println("pido movimiento");
            salida.println("movimiento");
            System.out.println("mando movimiento");
            salida.println("2 0 5 9");
            //System.out.println(entrada.readLine());
            //entrada.readLine();
            System.out.println("he leido");
            
            salida.println("que es esto?");
            salida.println("rendicion");
        
            entrada.close();
            salida.close();
            socket.close();
            
            
            
//        Partida partida = null;
//        VistaJuego vista;
//        Tablero tablero;
//        Controlador controlador;
//        Reglas reglas = new ReglasDamas();
//        vista = new VistaJuego("damas");
//
//        
//        System.out.println("Damas_V2");
//
//        
//        File directorio = new File("partidasGuardadas");
//        if(!directorio.isDirectory()){
//            directorio.mkdir();
//        } 
//
//        
//            controlador = new Controlador(reglas);
//            tablero = new Tablero();
//            tablero.addObserver(vista);
//            controlador.vista(vista);
//            vista.addControlador(controlador);
//            
        } catch (IOException ex) {
            System.err.println("OOOPS");
        }
        }
    
        
        
}
