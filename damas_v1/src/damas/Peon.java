/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import utilidades.Movimiento;

/**
 *
 * @author Zeko
 */
public class Peon extends Ficha{

    public Peon(String color) {
        super(color);
    }
    
    @Override
    public boolean mover(Movimiento mov){
        
        int avanceFila = mov.getFilaFinal() - mov.getFilaInicial();
        int avanceCol = mov.getColFinal() - mov.getColInicial();
        System.err.println(" - "+avanceFila+":"+avanceCol);
        if(this.getColor().equals(NEGRO)){
            //mover hacia delante las fichas de abajo
            if(avanceFila==1 && avanceCol==1){
                return true;
            }
            else if(avanceFila==1 && avanceCol==-1){
                return true;
            }
            else if(avanceFila==2 && avanceCol==2){
                return true;
            }
            else if(avanceFila==2 && avanceCol==-2){
                return true;
            }
        }
        else if(this.getColor().equals(BLANCO)){
            if(avanceFila==-1 && avanceCol==1){
                return true;
            }
            else if(avanceFila==-1 && avanceCol==-1){
                return true;
            }
            else if(avanceFila==-2 && avanceCol==2){
                return true;
            }
            else if(avanceFila==-2 && avanceCol==-2){
                return true;
            }
        }
        
        
        
        return false;
    }
    
}
