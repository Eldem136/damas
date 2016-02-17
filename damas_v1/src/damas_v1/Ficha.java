/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas_v1;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author zeko
 */
class Ficha implements Serializable{
    private String color;
    private ImageIcon icono;
    private int posicion;
    
    
    
    public Ficha(String color){
        this.color = color;
    }
    
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
    }
    
    public ImageIcon getIcono(){
        return icono;
    }
    
    public void setPosicion(int a){
        this.posicion = a;
    }
    public int getPosicion(){
        return posicion;
    }
    
    @Override
    public boolean equals(Object obj){
        Ficha tmp;
        if(obj instanceof Ficha){
             tmp = (Ficha) obj;
             if(this.getColor().equals(tmp.getColor())){
                 return true;
             }
        }
        return false;
    }
    
    
    
}
