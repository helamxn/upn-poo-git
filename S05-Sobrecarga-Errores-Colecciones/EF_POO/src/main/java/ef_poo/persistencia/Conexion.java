package ef_poo.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static String SERVIDOR = "localhost";
    private static String BASE_DE_DATOS = "LibreriaBazarDB";
    private static String USUARIO = "efpoo";
    private static String CONTRASENA = "Efpoo123!";

    private static String URL = "jdbc:sqlserver://" + SERVIDOR
            + ";databaseName=" + BASE_DE_DATOS
            + ";encrypt=false;trustServerCertificate=true;";

    private static Connection conexion;

    private Conexion() {
    }

    public static Connection obtener() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        }
        return conexion;
    }

    public static void cerrar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}
