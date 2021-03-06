package logica;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorThread extends Thread {

    private DatagramPacket clientePacket;
    private DatagramSocket servidorSocket;
    private DB db;
    private Runtime shell;
    private Process proceso;

    public ServidorThread(DatagramPacket cliente, DatagramSocket servidor) {
        this.clientePacket = cliente;
        this.servidorSocket = servidor;

        shell = Runtime.getRuntime();
        db = new DB();
        db.conectar();
        try {
            System.out.println("(LOG) [OK] Packet cliente [" + clientePacket.getAddress().getHostAddress() + "] capturado");
        } catch (Exception ex) {
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
            mensajeRecibido = new String(clientePacket.getData());
        } catch (Exception ex) {
            System.out.println("(LOG) [ERROR] No se pudo recibir el mesaje del cliente");
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        if ("Dame server                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ".equals(mensajeRecibido)) {
            try {
                Pc usuario = db.insertar();
                // COMANDO DOCKER
                // docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test:1.0
                proceso = shell.exec("docker run -d --rm -p " + usuario.getPuertoPHP() + ":80 -p " + usuario.getPuertoSQL() + ":3306 --name=server" + usuario.getId() + " xxdrackleroxx/test");
                proceso.waitFor();
                mensajeAMandar = usuario.getPuertoPHP() + "?" + usuario.getPuertoSQL() + "?" + usuario.getId();
                
                byte[] bufferOut = new byte[1000];
                bufferOut = mensajeAMandar.toUpperCase().getBytes();
                DatagramPacket sendPacket = new DatagramPacket(bufferOut, bufferOut.length, clientePacket.getAddress(), clientePacket.getPort());
                servidorSocket.send(sendPacket);

                System.out.println("(LOG) [OK] Respuesta al cliente [" + clientePacket.getAddress().getHostAddress() + "] de " + usuario);

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

                Integer id = Integer.parseInt(mensajeRecibido.substring(11).trim());
                db.eliminar(id);
                try {
                    // COMANDO DOCKER
                    // docker stop -t 0 server[ID]
                    proceso = shell.exec("docker stop -t 0 server" + id);
                } catch (Exception ex) {
                    Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("(LOG) [ERROR] Problema ejecutando docker stop con server" + id);
                }
                System.out.println("(LOG) [OK] App de cliente [" + clientePacket.getAddress().getHostAddress() + "] id:" + id + " eliminada");
            }
        }

        // Desconectar todo
        db.desconectar();
    }

}
