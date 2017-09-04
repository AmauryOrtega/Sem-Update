package logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private final int puerto = 5000;
    private DatagramSocket servidorSocket;
    private DatagramPacket clientePacket;
    
    public void iniciarServidor() {
        try {
            // Crea el DatagramSocket para escuchar nuevas conexiones
            servidorSocket = new DatagramSocket(puerto);
            System.out.println("(LOG) [OK] inicializado el DatagramSocket y escuchando...");
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se pudo inicializar el DatagramSocket");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                // Acepta una nueva conexion y crea un hilo para ella
                byte[] bufferIn = new byte[1000];
                clientePacket = new DatagramPacket(bufferIn, bufferIn.length);
                servidorSocket.receive(clientePacket);
                // Innecesario crear mensajeRecibido
                //String mensajeRecibido = new String(clientePacket.getData());
                System.out.println("(LOG) [OK] Conexion del cliente " + clientePacket.getAddress().getHostAddress());
                ((ServidorThread) new ServidorThread(clientePacket, servidorSocket)).start();
            } catch (IOException ex) {
                System.out.println("(LOG) [ERROR] No se pudo aceptar el cliente");
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void detenerServidor() {
        try {
            servidorSocket.close();
            System.out.println("(LOG) [OK] DatagramSocket detenido");
            // Detener todos los contenedores
        } catch (Exception ex) {
            System.out.println("(LOG) [ERROR] No se pudo detener el servidor");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
