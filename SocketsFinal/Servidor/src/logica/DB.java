package logica;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

class DB {

    private Connection conexion;
    private String db_ip = "localhost";
    private String db_port = "3306";
    private String db_name = "registro";
    private String user = "root";
    private String pass = "";
    private String table_registros = "registros";
    private String table_puertos = "puertos";

    void conectar() {
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

    void insertar(String format, String hostAddress) {
        try {
            String Query = "INSERT INTO " + table_registros + " VALUES()";
            Statement st = (Statement) conexion.createStatement();
            st.executeUpdate(Query);
        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] No se pudo insertar la informacion");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void desconectar() {
        try {
            conexion.close();
            System.out.println("(LOG) [OK] DB Cerrada");
        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] DB No se pudo cerrar");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void consulta() {
        try {
            String Query = "SELECT * FROM " + table_registros;
            Statement st = (Statement) conexion.createStatement();

            java.sql.ResultSet resultSet;
            resultSet = st.executeQuery(Query);

            while (resultSet.next()) {
                System.out.println("Columna1: " + resultSet.getString("columna1") + " "
                        + "Columna2: " + resultSet.getString("columna2") + " "
                        + "Columna3: " + resultSet.getString("columna3"));
            }

        } catch (SQLException ex) {
            System.out.println("(LOG) [ERROR] DB No se leer");
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
