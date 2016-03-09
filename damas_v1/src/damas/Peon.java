/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;
import utilidades.Movimiento;

/**
 *
 * @author Ezequiel Barbudo, Diego Malo
 */
public class Peon extends Ficha{

    public Peon(String color) {
        super(color);
    }
    
    @Override
    public boolean mover(Movimiento mov){
        int avanceFila = mov.getFilaFinal() - mov.getFilaInicial();
        int avanceCol = mov.getColFinal() - mov.getColInicial();
        if(this.getColor().equals(NEGRO)){
            //mover hacia adelante las fichas negras (arriba)
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
            //mover hacia adelante las fichas blancas (abajo)
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
