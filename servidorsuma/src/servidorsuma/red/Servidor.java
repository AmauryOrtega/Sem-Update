/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidorsuma.red;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidorsuma.ventanas.VentanaPrincipal;

/**
 *
 * @author Estudiante
 */
public class Servidor extends Thread{

    private ServerSocket conectorHembra;
    private boolean estado;
    VentanaPrincipal ventana;
    
    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        iniciar();
    }
    
    public void iniciar(){
        try {
            conectorHembra = new ServerSocket(9001);
            estado = true;
            while (estado) {
                Socket conectorMacho = conectorHembra.accept();
                HiloEntrada entrada = new HiloEntrada(this, conectorMacho);
                entrada.start();
                
            }
        } catch (IOException ex) {
           ventana.mostrarLog("Error al enviar el resultado al cliente ");
        }
    }
    
}

class HiloEntrada extends Thread{
    Servidor server;
    Socket conectorCliente;
    float resultado;

    public HiloEntrada(Servidor server, Socket conectorCliente) {
        this.server = server;
        this.conectorCliente = conectorCliente;
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        leer();
    }
    
    public void leer(){
        DataInputStream ios = null;
        try {
            ios = new DataInputStream(conectorCliente.getInputStream());
            float n1 = ios.readFloat();
            float n2 = ios.readFloat();
            resultado = n1+n2;
            HiloSalida salida = new HiloSalida(server, conectorCliente);
            salida.resultado = resultado;
            salida.start();
        } catch (IOException ex) {
            server.ventana.mostrarLog("Error al enviar el resultado al cliente ");
        } finally {
            try {
                ios.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloEntrada.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConectorCliente(Socket conectorCliente) {
        this.conectorCliente = conectorCliente;
    }

    public void setServer(Servidor server) {
        this.server = server;
    }

    public Socket getConectorCliente() {
        return conectorCliente;
    }

    public Servidor getServer() {
        return server;
    }
    
    
}

class HiloSalida extends Thread{
    Servidor server;
    Socket conectorCliente;
    float resultado;

    public HiloSalida(Servidor server, Socket conectorCliente) {
        this.server = server;
        this.conectorCliente = conectorCliente;
   
    }

    public void escribir(){
        try {
            DataOutputStream dos = new DataOutputStream(conectorCliente.getOutputStream());
            dos.writeFloat(resultado);
            dos.flush();
        } catch (IOException ex) {
            InetAddress direccionIp = conectorCliente.getInetAddress();
            String ip = direccionIp.getHostAddress();
            server.ventana.mostrarLog("Error al enviar el resultado al cliente "+direccionIp+":"+conectorCliente.getChannel());
        }
    }

    @Override
    public void run() {
        super.run(); //To change body of generated methods, choose Tools | Templates.
        escribir();
    }
    
    
    public Servidor getServer() {
        return server;
    }

    public Socket getConectorCliente() {
        return conectorCliente;
    }

    public float getResultado() {
        return resultado;
    }
    
    
}