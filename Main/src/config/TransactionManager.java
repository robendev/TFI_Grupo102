package config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase para manejar transacciones JDBC.
 * Implementa AutoCloseable para asegurar el cierre de la conexión.
 *
 * Uso típico:
 * <pre>
 * try (TransactionManager tm = new TransactionManager()) {
 *     tm.startTransaction();
 *     Connection conn = tm.getConnection();
 *     // usar DAOs con conn
 *     tm.commit();
 * } catch (Exception e) {
 *     // opcional: tm.rollback();  // close() ya hace rollback si queda activa
 *     throw e;
 * }
 * </pre>
 */
public class TransactionManager implements AutoCloseable {

    private Connection conn;
    private boolean transactionActive;

    /**
     * Constructor que crea una nueva conexión usando DatabaseConnection.
     */
    public TransactionManager() throws SQLException {
        this(DatabaseConnection.getConnection());
    }

    /**
     * Constructor que recibe una conexión existente.
     *
     * @param conn conexión JDBC válida y abierta
     * @throws SQLException si la conexión está cerrada
     */
    public TransactionManager(Connection conn) throws SQLException {
        if (conn == null) {
            throw new IllegalArgumentException("La conexión no puede ser null");
        }
        if (conn.isClosed()) {
            throw new SQLException("No se puede usar una conexión cerrada en TransactionManager");
        }
        this.conn = conn;
        this.transactionActive = false;
    }

    public Connection getConnection() {
        return conn;
    }

    public void startTransaction() throws SQLException {
        if (conn == null) {
            throw new SQLException("No se puede iniciar la transacción: conexión no disponible");
        }
        if (conn.isClosed()) {
            throw new SQLException("No se puede iniciar la transacción: conexión cerrada");
        }
        conn.setAutoCommit(false);
        transactionActive = true;
    }

    public void commit() throws SQLException {
        if (conn == null) {
            throw new SQLException("Error al hacer commit: no hay conexión establecida");
        }
        if (!transactionActive) {
            throw new SQLException("No hay una transacción activa para hacer commit");
        }
        conn.commit();
        transactionActive = false;
    }

    public void rollback() {
        if (conn != null && transactionActive) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                System.err.println("Error durante el rollback: " + e.getMessage());
            } finally {
                transactionActive = false;
            }
        }
    }

    @Override
    public void close() {
        if (conn != null) {
            try {
                if (transactionActive) {
                    rollback();
                }
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ignored) {
                    // Si falla acá, igual intentamos cerrar
                }
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            } finally {
                conn = null;
                transactionActive = false;
            }
        }
    }

    public boolean isTransactionActive() {
        return transactionActive;
    }
}
