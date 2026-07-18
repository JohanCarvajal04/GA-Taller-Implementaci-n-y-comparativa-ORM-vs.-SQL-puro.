package ec.edu.uteq.taller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    private static final String URL = "jdbc:postgresql://localhost:5432/taller_db";
    private static final String USUARIO = "postgres";
    private static final String CONTRASENA = "P123";

    private Conexion() {
    }

    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}
