package logica;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorThread extends Thread {

    private Socket cliente;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DB db;

    public ServidorThread(Socket cliente) {
        this.cliente = cliente;

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
        } catch (IOException ex) {
            System.out.println("(LOG) [ERROR] No se pudo recibir del cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            System.out.println("(LOG) [ERROR] No se casteo bien el mensaje recibido del cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (mensajeRecibido.equals("Dame server")) {
            // COMANDO DOCKER
            // docker run -d xxdrackleroxx/imagen --name=[id] -p [PuertoPHP]:80 -p [PuertoSQL]:3306
            try {
                Pc usuario = db.insertar();
                mensajeAMandar = InetAddress.getLocalHost().getHostAddress() + ":" + usuario.getPuertoPHP() + "/phpmyadmin ?" + usuario.getPuertoSQL() + "?" + usuario.getId();
                out.writeObject(mensajeAMandar);
                System.out.println("(LOG) [OK] Respuesta al cliente " + cliente.getInetAddress().getHostAddress() + " de " + usuario);
            } catch (UnknownHostException ex) {
                System.out.println("(LOG) [ERROR] No se pudo obtener la IP del servidor");
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("(LOG) [ERROR] No se pudo enviar mensaje al cliente");
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (mensajeRecibido.matches("Mata server(.*)")) {
                // COMANDO DOCKER
                // docker kill [id] & docker rm [id]
                Integer id = Integer.parseInt(mensajeRecibido.substring(11));
                db.eliminar(id);
                System.out.println("(LOG) [OK] App de cliente [" + cliente.getInetAddress().getHostAddress() + "] id:" + id + " eliminada");
            }
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
