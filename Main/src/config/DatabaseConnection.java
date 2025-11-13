/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    // Metodo estatico que devuelve una nueva conexion
    public static Connection getConnection() throws SQLException {
        try {
            Properties prop = new Properties();

            // Carga el archivo de configuración desde resources/
            InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo " + PROPERTIES_FILE);
            }

            prop.load(input);

            String url = prop.getProperty("db.url");
            String user = prop.getProperty("db.user");
            String pass = prop.getProperty("db.password");

            return DriverManager.getConnection(url, user, pass);

        } catch (IOException ex) {
            throw new RuntimeException("Error cargando propiedades de BD", ex);
        }
    }
}/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author iRb18
 */
public class DatabaseConnection {
    
}
