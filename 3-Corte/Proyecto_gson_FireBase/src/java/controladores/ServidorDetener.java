package controladores;

import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.DB;
import modelo.Util;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ServidorDetener extends HttpServlet {

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
        Integer id = Integer.parseInt(request.getParameter("id"));

        DB base_datos = new DB();
        base_datos.conectar();
        base_datos.eliminar(id);

        if (Util.docker) {
            Process proceso;
            Runtime shell = Runtime.getRuntime();
            // COMANDO DOCKER
            // docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test:1.0
            proceso = shell.exec("docker stop -t 0 server" + id);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://fcm.googleapis.com/fcm/send");
        // Headers
        httppost.addHeader("Authorization", " key=AAAAZ3CLFVs:APA91bHg6SSZ0Xq6hzcPX9Q7g37sIRNH4HP4BW_eFQblN__2jax9ZjsaptJYfAugyVo12sfnRFTk4o57XwiDpV_1-DEBnPYmH-ETTB-nc3DqlOBqV98AtsW99sujS04HhMhzRzE9xIfQ");
        httppost.addHeader("Content-Type", "application/json");

        JsonObject mensaje = new JsonObject();
        mensaje.addProperty("to", "c8Yt-yQouZI:APA91bEkZ0QLt1jESKmLwkDHD1gu7s-VJ8ThRv8JsHFhpgk3RbfwooiDnvHvhYpdWkvIqLveEHD_tAkoQvm5EyYVE3AumkF_cQwDO27M_rTSTQslpGySbDbY2N9S50gKUAa2ADRS-GPH");
        mensaje.addProperty("priority", "high");

        JsonObject notificacion = new JsonObject();
        notificacion.addProperty("title", "Contenedor destruido");
        notificacion.addProperty("body", "Servidor" + id + " destruido");

        mensaje.add("notification", notificacion);

        httppost.setEntity(new StringEntity(mensaje.toString(), "UTF-8"));
        System.out.println("[LOG] Mensaje: " + mensaje);
        HttpResponse respuesta = httpclient.execute(httppost);
        System.out.println(respuesta.getStatusLine());

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
