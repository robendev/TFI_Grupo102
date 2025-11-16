/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import config.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {

    public static void main(String[] args) {
        System.out.println("Probando conexión a la base de datos...");

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("Conexión establecida correctamente.");
            } else {
                System.out.println("La conexión no pudo ser establecida.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar conectar: " + e.getMessage());
        }
    }
}
