package ws;

import com.google.gson.Gson;
import java.io.IOException;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import modelo.DB;
import modelo.Pc;
import modelo.Util;

@WebService(serviceName = "WSServidor")
public class WSServidor {

    @WebMethod(operationName = "iniciarServidor")
    public String iniciarServidor() {
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
            return json;
        } catch (Exception e) {
            String json = new Gson().toJson("ERROR");
            return json;
        }
    }

    @WebMethod(operationName = "detenerServidor")
    @Oneway
    public void detenerServidor(@WebParam(name = "id") String id) {
        DB base_datos = new DB();
        base_datos.conectar();
        base_datos.eliminar(Integer.parseInt(id));

        if (Util.docker) {
            Process proceso;
            Runtime shell = Runtime.getRuntime();
            try {
                // COMANDO DOCKER
                // docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test:1.0
                proceso = shell.exec("docker stop -t 0 server" + id);
            } catch (IOException ex) {
                System.out.println("[ERROR] Problema con el id obtenido");
            }
        }
    }
}
