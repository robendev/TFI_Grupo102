package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class DatabaseConnection {

    /** URL de conexión a la base de datos */
    private static final String URL = System.getProperty(
            "db.url",
            "jdbc:mysql://localhost:3306/dbtpi3?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires"
    );

    /** Usuario de la base de datos */
    private static final String USER = System.getProperty("db.user", "root");

    /** Contraseña del usuario */
    private static final String PASSWORD = System.getProperty("db.password", "");

    
    static {
        try {
            // Cargar driver JDBC (necesario en algunas versiones de Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Validar configuración
            validateConfiguration();

        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(
                    "No se encontró el driver JDBC de MySQL: " + e.getMessage()
            );
        } catch (IllegalStateException e) {
            throw new ExceptionInInitializerError(
                    "Error en configuración de la base de datos: " + e.getMessage()
            );
        }
    }

    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo expone métodos estáticos.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria; no debe instanciarse.");
    }

    /**
     * Obtiene una conexión JDBC a la base de datos.
     * @return Conexión JDBC activa
     * @throws SQLException si no se puede establecer conexión
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Valida la configuración cargada. Se ejecuta una sola vez.
     *
     * @throws IllegalStateException si algún parámetro es inválido
     */
    private static void validateConfiguration() {
        if (URL == null || URL.trim().isEmpty()) {
            throw new IllegalStateException("La URL de conexión está vacía o no configurada");
        }
        if (USER == null || USER.trim().isEmpty()) {
            throw new IllegalStateException("El usuario de la base de datos no está configurado");
        }
        if (PASSWORD == null) {
            throw new IllegalStateException("La contraseña de la base de datos no puede ser null");
        }
    }
}
