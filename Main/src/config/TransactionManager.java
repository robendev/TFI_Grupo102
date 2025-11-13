/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private Connection connection;

    /**
     * Inicia una transaccion:
     * - Obtiene una conexion
     * - Desactiva el auto-commit
     */
    public void begin() throws SQLException {
        if (connection == null || connection.isClosed()) {
            DatabaseConnection db = new DatabaseConnection();
            connection = db.getConnection();
        }
        connection.setAutoCommit(false);
    }

    /**
     * Devuelve la conexion activa de la transaccion.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Hace commit de la transaccion.
     */
    public void commit() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hace rollback de la transacción.
     */
    public void rollback() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cierra la conexión y restaura el auto-commit.
     */
    public void close() {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

