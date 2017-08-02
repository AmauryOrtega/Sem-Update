package sockets;

import java.io.*;
import java.net.*;
import java.util.Random;

/**
 *
 * @author aroc
 */
public class Servidor {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Numero a adivinar
        int numero = new Random().nextInt(50) + 1;
        // Numero dicho por el usuario
        int i = 0;
        // Numero de vidas
        int vidas = 10;
        // Puerto donde escucha el socket.
        int numeroPuerto = 666;

        // ServerSocket que escuchara como el servidor,
        ServerSocket socketServidor = new ServerSocket(numeroPuerto);
        // Log
        System.out.println("Socket funcionando en el puerto: " + numeroPuerto + "\nEl numero es: " + numero + "\nVidas: " + vidas);

        // Acepta la conexion de un cliente cuando hay una
        Socket socketCliente = socketServidor.accept();

        // Stream para e/s, se crea primero la salida
        ObjectOutputStream salida = new ObjectOutputStream(socketCliente.getOutputStream());
        ObjectInputStream entrada = new ObjectInputStream(socketCliente.getInputStream());

        // Comienza juego
        while (vidas > 0 && i != numero) {
            // Obteniendo un entero de la entrada
            i = (Integer) entrada.readObject();

            // Preparando respuesta
            String respuesta = "Numero no adivinado.";
            if (i == numero) {
                respuesta = "Correcto, el numero era " + numero;
            } else {
                vidas--;
                if (vidas == 0) {
                    respuesta = "Perdiste";
                } else {
                    respuesta += " Quedan " + vidas + " vidas.";
                }
            }
            // Responde
            salida.writeObject(respuesta);
            // Log
            System.out.println("Numero recibido: " + i + ". Vidas: " + vidas);
        }
        //Cierra
        salida.close();
        entrada.close();
        socketCliente.close();
    }
}
