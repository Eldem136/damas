/**
 * damas_v3.java
 * @author Ezequiel Barbudo     (zeko3991@gmail.com)
 * @author Diego Malo           (d.malo136@gmail.com)
 */
package damas;

import reglas.*;

public class damas_v3 {
    

    /**
     * @param args the command line arguments
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws ClassNotFoundException{
        
        Reglas reglamento = new ReglasDamas();
        new Cliente(reglamento);
        
    }
    
        
        
}
