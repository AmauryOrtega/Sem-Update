package controladores;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.DB;
import modelo.Pc;
import modelo.Util;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ServidorIniciar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            DB base_datos = new DB();
            base_datos.conectar();
            Pc usuario = base_datos.insertar();
            base_datos.desconectar();

            if (Util.docker) {
                Process proceso;
                Runtime shell = Runtime.getRuntime();
                // COMANDO DOCKER
                // docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test
                proceso = shell.exec("docker run -d --rm -p " + usuario.getPuertoPHP() + ":80 -p " + usuario.getPuertoSQL() + ":3306 --name=server" + usuario.getId() + " xxdrackleroxx/test");
                proceso.waitFor();
            }

            String json = new Gson().toJson(usuario);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Writer salida = null;
            salida = response.getWriter();
            salida.write(json);
            salida.close();

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("https://fcm.googleapis.com/fcm/send");
            // Headers
            httppost.addHeader("Authorization", "key=<your legacy server key>");
            httppost.addHeader("Content-Type", "application/json");

            JsonObject mensaje = new JsonObject();
            mensaje.addProperty("to", "<your fcm token>");
            mensaje.addProperty("priority", "high");

            JsonObject notificacion = new JsonObject();
            notificacion.addProperty("title", "Nuevo contenedor");
            notificacion.addProperty("body", usuario.toString());

            mensaje.add("notification", notificacion);

            httppost.setEntity(new StringEntity(mensaje.toString(), "UTF-8"));
            System.out.println("[LOG] Mensaje: " + mensaje);
            HttpResponse respuesta = httpclient.execute(httppost);
            System.out.println(respuesta.getStatusLine());

        } catch (Exception e) {
            String json = new Gson().toJson("ERROR BD");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Writer salida = null;
            salida = response.getWriter();
            salida.write(json);
            salida.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
