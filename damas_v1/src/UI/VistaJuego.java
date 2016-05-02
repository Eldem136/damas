/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Observable;

/**
 *
 * @author Zeko
 */
public class VistaJuego extends JFrame implements java.util.Observer {
    JLabel contador; JButton botonInc, botonDec, botonInit;
    TableroSwing tableroSwing;
    static final String DEC = "Dec", INC = "Inc", INICIO = "Inicio";
     
    public VistaJuego(String titulo) {
        super(titulo);
        addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) { System.exit(0); }
        });  
        
        getContentPane().setLayout(new BorderLayout());
        
        
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        getContentPane().add(panel1,BorderLayout.CENTER);
        tableroSwing = new TableroSwing(10,10);
        panel1.add(tableroSwing);
        

        setBounds(0, 0, tableroSwing.getAnchura(),
                tableroSwing.getAltura()); setVisible(true);  
        setResizable(false);
    }

    public void update(Observable obs, Object obj) {
      contador.setText("" + ((Integer)obj).intValue());
    }

    public void addControlador(ActionListener controlador){
        botonDec.addActionListener(controlador);
        botonInc.addActionListener(controlador);	
        botonInit.addActionListener(controlador);		
    }
}
