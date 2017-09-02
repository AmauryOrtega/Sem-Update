package logica;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorThread extends Thread {

    private Socket cliente;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private DB db;
    private Runtime shell;
    private Process proceso;

    public ServidorThread(Socket cliente) {
        this.cliente = cliente;

        shell = Runtime.getRuntime();
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
            try {
                Pc usuario = db.insertar();
                // COMANDO DOCKER
                // docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test:1.0
                proceso = shell.exec("docker run -d --rm -p " + usuario.getPuertoPHP() + ":80 -p " + usuario.getPuertoSQL() + ":3306 --name=server" + usuario.getId() + " xxdrackleroxx/test:1.0");
                proceso.waitFor();
                mensajeAMandar = usuario.getPuertoPHP() + "?" + usuario.getPuertoSQL() + "?" + usuario.getId();
                out.writeObject(mensajeAMandar);
                System.out.println("(LOG) [OK] Respuesta al cliente [" + cliente.getInetAddress().getHostAddress() + "] de " + usuario);
            } catch (UnknownHostException ex) {
                System.out.println("(LOG) [ERROR] No se pudo obtener la IP del servidor");
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("(LOG) [ERROR] No se pudo enviar mensaje al cliente");
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                System.out.println("(LOG) [ERROR] Problema ejecutando docker run");
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            if (mensajeRecibido.matches("Mata server(.*)")) {
                Integer id = Integer.parseInt(mensajeRecibido.substring(11));
                db.eliminar(id);
                try {
                    // COMANDO DOCKER
                    // docker stop -t 0 server[ID]
                    proceso = shell.exec("docker stop -t 0 server" + id);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("(LOG) [ERROR] Problema ejecutando docker stop con server" + id);
                }
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
