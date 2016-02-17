/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v1;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author zeko
 */


public class Partida implements Serializable{
    private Tablero tablero;
    private Jugador jugador1, jugador2;
    private int turno = 0, columna;
    private static int numTableros = 0;
    /**
     * Dados 2 jugadores, se crea una partida y se inicializa, comenzando 
     * su función de arbitrar turnos y jugadas entre los jugadores.
     * @param jugador1
     * @param jugador2 
     */
    public Partida(Jugador jugador1, Jugador jugador2){
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        //tablero = new Tablero(numTableros++);        
    }
    
    public Partida(){
        
    }
    
    public Jugador[] getJugadores(){
        Jugador lista[] = new Jugador[2];
        lista[0] = jugador1;
        lista[1] = jugador2;
        return lista;
    }
    
    public Tablero getTablero(){
        return tablero;
    }
    
    public void tablero(Tablero t){
        this.tablero = t;
    }
    
    
    
    public static Partida cargar(File partida){
        try{
            ObjectInputStream in = new ObjectInputStream(
        new FileInputStream(partida));
        Partida p1 = (Partida) in.readObject();
        
        //¡¡¡¿¿¿Esto se puede hacer???!!!
        /*this.jugador1 = p1.jugador1;
        this.jugador2 = p1.jugador2;
        this.tablero = p1.tablero;
        this.turno = p1.turno;*/
        in.close();
        return p1;
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();;
        }
        return null;
        
    }
    
    
    /**
     * guardar() guarda un objeto partida en un archivo nombrado por los 
     * identificadores de los jugadores.
     */
    /*public boolean guardar(){
        try{
            File file = new File("partidasGuardadas");
            String path = file.getCanonicalPath();
            String nombre = jugador1.getId()+"_VS_"+jugador2.getId()+".dat";
            File f = new File(path,nombre);
            f.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(new 
            FileOutputStream(f));
            out.writeObject(this);
            out.close();
            return true;
        }catch(Exception e){
            System.out.println("No se ha podido guardar la partida");
            e.printStackTrace();
        }
        return false;
    }    */
    
    public boolean guardar() throws FileNotFoundException, IOException{
        JFileChooser dialogoGuardar = new JFileChooser();
        String ruta = "";
        if(dialogoGuardar.showSaveDialog(null)== dialogoGuardar.APPROVE_OPTION){
            ruta = dialogoGuardar.getSelectedFile().getAbsolutePath();
        
            //String nombreFichero = jugador1.getId()+"_VS_"+jugador2.getId()+".dat";
            //File f = new File(ruta,nombreFichero);
            File f = new File(ruta+".dat");
            f.createNewFile();
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(f));
            out.writeObject(this);
            out.close();
            return true;
        }
        
        return false;
    }
}
