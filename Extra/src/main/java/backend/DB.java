package backend;

import backend.Util;
import backend.Pc;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

// mysql -h 127.0.0.1 -P 3306 -u root --password=lxit
public class DB {

    private Connection conexion;
    private String db_ip = Util.ip_db;
    private String db_port = "3306";
    private String db_name = "registro";
    private String user = "root";
    private String pass = "";

    public void conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = (Connection) DriverManager.getConnection("jdbc:mysql://" + db_ip + ":" + db_port + "/" + db_name, user, pass);
            System.out.println("(LOG) [OK] Conexion correcta a db");
        } catch (ClassNotFoundException ex) {
            System.out.println("(LOG) [ERROR] La clase com.mysql.jdbc.Driver hace falta");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] Revise la DB");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Pc insertar() {
        Pc usuario = new Pc();
        String Query;
        Statement st;
        java.sql.ResultSet resultSet;
        try {
            // Saca el ultimo valor de la BD para tener los puertos
            Query = "SELECT * FROM `registros`ORDER BY idUsuario DESC LIMIT 1";
            st = (Statement) conexion.createStatement();
            resultSet = st.executeQuery(Query);
            while (resultSet.next()) {
                usuario.setPuertoPHP(Integer.parseInt(resultSet.getString("puertoPHP")));
                usuario.setPuertoSQL(Integer.parseInt(resultSet.getString("puertoSQL")));
            }

            usuario.setPuertoPHP(usuario.getPuertoPHP() + 1);
            usuario.setPuertoSQL(usuario.getPuertoSQL() + 1);

            // Inserta al cliente para dichos puertos
            Query = "INSERT INTO `registros`(`puertoPHP`, `puertoSQL`) VALUES (" + usuario.getPuertoPHP() + "," + usuario.getPuertoSQL() + ")";
            st = (Statement) conexion.createStatement();
            st.executeUpdate(Query);

            // Saca el ultimo valor de la BD para tener el id del usuario
            Query = "SELECT * FROM `registros`ORDER BY idUsuario DESC LIMIT 1";
            st = (Statement) conexion.createStatement();
            resultSet = st.executeQuery(Query);
            while (resultSet.next()) {
                usuario.setId(Integer.parseInt(resultSet.getString("idUsuario")));
            }

            return usuario;
        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] No se pudo insertar la informacion");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void desconectar() {
        try {
            conexion.close();
            System.out.println("(LOG) [OK] DB Cerrada");
        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] DB No se pudo cerrar");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminar(Integer id) {
        try {
            String Query = "DELETE FROM `registros` WHERE idUsuario=" + id;
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(Query);
            System.out.println("(LOG) [OK] DB Eliminando usuario " + id);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
