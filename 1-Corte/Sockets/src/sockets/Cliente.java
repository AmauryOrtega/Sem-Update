package sockets;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;

/**
 *
 * @author aroc
 */
public class Cliente {

    public static void main(String arg[]) {
        // Numero a mandar.
        Integer num = 0;
        int numeroPuerto = 666;

        try {
            // Se intenta conectar, retorna IOException en caso que no pueda
            Socket socket = new Socket("localhost", numeroPuerto);

            // Stream para e/s, se crea primero la salida
            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            while (true) {
                num = new Integer(JOptionPane.showInputDialog("Ingrese un numero entre 1 y 50"));

                salida.writeObject(num);
                String response = (String) entrada.readObject();

                JOptionPane.showMessageDialog(null, "SERVIDOR: " + response);
                if (response.contains("Correcto") || response.contains("Perdiste")) {
                    break;
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Problemas de conexion");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Problemas parseando");
        }
    }
}
