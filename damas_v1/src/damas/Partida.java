/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import reglas.Reglas;
import reglas.ReglasDamas;
import utilidades.InputReader;
import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public class Partida {
    private Jugador jugador1;
    private Jugador jugador2;
    public Tablero tablero;
    private Reglas reglas;
    private int turno;
    private boolean fin;
    
    private InputReader lectorConsola;
    
    public Partida(String n1, String n2, Reglas reglas){
        this.tablero = new Tablero();
        this.jugador1 = new Jugador(n1, Ficha.BLANCO);
        this.jugador2 = new Jugador(n2, Ficha.NEGRO);
        this.reglas = reglas;
        this.turno = 0;
        this.fin = false;
        this.lectorConsola = new InputReader();
        
        this.tablero.colocarFichas();
        
    }
    
    public boolean guardar(){
        return true;
    }
    
    public boolean cargar(){
        return true;
    }
    
    public void jugar(){
        Movimiento mov;
        boolean movValido;
        System.out.println( tablero.toString());
        
        do{
            turno++;
            if(turno%2==1){  //turno del jugador blanco.
                System.out.println("TURNO DEL JUGADOR BLANCO");
                do {
                    mov = leerMovimiento(Ficha.BLANCO);
                    movValido =reglas.mover(mov, tablero);
                    if(!movValido){
                        System.err.println("No es un movimiento valido");
                    }
                }while(!movValido);
                
            }
            else{
                System.out.println("TURNO DEL JUGADOR NEGRO");
                do {
                    mov = leerMovimiento(Ficha.NEGRO);
                    movValido =reglas.mover(mov, tablero);
                    if(!movValido){
                        System.err.println("No es un movimiento valido");
                    }
                }while(!movValido);
                
            }
            System.out.println(tablero.toString());
        }while(!fin);
        
    }
    
    
    private Movimiento leerMovimiento(String color) {
        Movimiento mov;
        boolean fichaNoValida;
        do{
            mov = lectorConsola.leeMov();
            fichaNoValida = mov == null ||
                !tablero.getFicha(mov.getFilaInicial(), mov.getColInicial()).mismoColor(color);
            if(fichaNoValida) System.err.println("La ficha seleccionada no es válida");
        }while(fichaNoValida);
        
        return mov;
    }

}
