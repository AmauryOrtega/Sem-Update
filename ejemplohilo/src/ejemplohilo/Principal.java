/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplohilo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Estudiante
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         hiloCiclo1.setPriority(Thread.MIN_PRIORITY);
        hiloCiclo1.start();
        hiloCiclo2.setPriority(Thread.MAX_PRIORITY);
        hiloCiclo2.start();
 
    }

    static Thread hiloCiclo1 = new Thread() {

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            int i2 = 0;
            for (i2 = 0; i2 < 1000; i2++) {
                try {
                    Thread.sleep(200);
                    System.out.println("Cliclo 1: " + i2);
                } catch (InterruptedException ex) {
                    System.err.println("Error de espera");
                }
            }
        }

    };
    static Thread hiloCiclo2 = new Thread() {

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            int i2 = 0;
            for (i2 = 0; i2 < 1000; i2++) {
                try {
                    Thread.sleep(200);
                    System.out.println("Cliclo 2: " + i2);
                } catch (InterruptedException ex) {
                    System.err.println("Error de espera");
                }
            }
        }

    };
}
