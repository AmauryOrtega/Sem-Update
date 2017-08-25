package logica;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorThread extends Thread {

    private Socket cliente;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DB db;
    private Calendar fecha;
    private DateFormat formato;

    public ServidorThread(Socket cliente) {
        this.cliente = cliente;

        fecha = Calendar.getInstance();
        formato = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        db = new DB();
        db.conectar();

        try {
            in = new ObjectInputStream(cliente.getInputStream());
            out = new ObjectOutputStream(cliente.getOutputStream());
            System.out.println("(LOG) [OK] Socket cliente [" + cliente.getInetAddress().getHostAddress() + "] capturado");
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se pudo comunicar con el cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        super.run();
        String mensajeRecibido = "";
        String mensajeAMandar = "";
        try {
            mensajeRecibido = (String) in.readObject();
            // Aqui se puede separar el string para obtener la IP o ID del cliente
            // O se puede recibir la clase Cliente (Suponiendo que exista en un jar)
            // Y asi recibir un objeto
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se pudo recibir del cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("(LOG) [ERROR] No se casteo bien el mensaje recibido del cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (mensajeRecibido.equals("Dame server")) {
            // Aqui hay que guardar el ID del cliente
            db.insertar(formato.format(fecha.getTime()),
                    cliente.getInetAddress().getHostAddress());
            if (true) {
                // Obtiene el siguiente par de puertos disponibles
                // Ejecuta el comando docker
                // http://[IP]:[Puerto]
                // Tambien puedo mandar un objeto de regreso
                mensajeAMandar = "localhost:49161/phpmyadmin";
                try {
                    out.writeObject(mensajeAMandar);
                } catch (IOException ex) {
                    System.out.println("(LOG) [ERROR] No se pudo enviar mensaje al cliente");
                    Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // Mensaje negativo
        }

        // Desconectar todo
        db.desconectar();
        try {
            out.close();
            in.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se cerro bien la comunicacion con el cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
