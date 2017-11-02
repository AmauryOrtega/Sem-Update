package backend;

import java.util.concurrent.atomic.AtomicLong;
import java.io.IOException;
import java.lang.Runtime;
import java.lang.Process;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServidorControlador {

    @RequestMapping("/iniciar-servidor")
    public Pc greeting() {
    	DB db = new DB();
    	db.conectar(); 
        Pc usuario = db.insertar();
        db.desconectar();
        if (Util.docker) {
        	try{
        		Process proceso;
            	Runtime shell = Runtime.getRuntime();
            	// COMANDO DOCKER
            	// docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test
            	proceso = shell.exec("docker run -d --rm -p " + usuario.getPuertoPHP() + ":80 -p " + usuario.getPuertoSQL() + ":3306 --name=server" + usuario.getId() + " xxdrackleroxx/test");
            	proceso.waitFor();	
        	}catch(Exception e){
        		System.out.println("[ERROR] Problema con shell");
        	}
            
        }
        return usuario;
    }

    @RequestMapping("/detener-servidor")
    public String greeting(@RequestParam(value="id", defaultValue="-1") Integer id) {
    	if(id != -1){
    		DB db = new DB();
    		db.conectar();
        	db.eliminar(id);
        	db.desconectar();
        	if (Util.docker) {
            	Process proceso;
            	Runtime shell = Runtime.getRuntime();
            	try {
                	// COMANDO DOCKER
                	// docker run -d --rm -p [PuertoPHP]:80 -p [PuertoSQL]:3306 --name=server[ID] xxdrackleroxx/test:1.0
                	proceso = shell.exec("docker stop -t 0 server" + id);
            	} catch (Exception e) {
                	return "{\"status\":\"ERROR SHELL\"}";
            	}
        	}
        	return "{\"status\":\"OK\"}";
    	}
    	return "{\"status\":\"ERROR ID\"}";
    }
}