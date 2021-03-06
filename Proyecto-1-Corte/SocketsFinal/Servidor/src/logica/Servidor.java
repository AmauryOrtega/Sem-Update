package logica;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private final int puerto = 5000;
    private ServerSocket servidor;
    private Socket cliente; // Se asignara uno por hilo

    public void iniciarServidor() {
        try {
            // Crea el ServerSocket para escuchar nuevas conexiones
            servidor = new ServerSocket(puerto);
            System.out.println("(LOG) [OK] ServerSocket creado bien y escuchando");
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se crear ServerSocket");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                // Acepta una nueva conexion y crea un hilo para ella
                cliente = servidor.accept();
                System.out.println("(LOG) [OK] Conexion del cliente " + cliente.getInetAddress().getHostAddress());
                ((ServidorThread) new ServidorThread(cliente)).start();
            } catch (IOException ex) {
                System.out.println("(LOG) [ERROR] No se pudo aceptar el cliente");
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void detenerServidor(){
        try {
            servidor.close();
            System.out.println("(LOG) [OK] ServerSocket detenido");
            // Detener todos los contenedores
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se pudo detener el servidor");
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
