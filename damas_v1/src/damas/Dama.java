/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package damas;

/**
 *
 * @author Zeko
 */
public class Dama extends Ficha {

    public Dama(String color) {
        super(color);
    }
    @Override
    public boolean mover(){
        return true;
    }
    
}
