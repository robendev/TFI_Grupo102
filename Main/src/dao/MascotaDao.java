package dao;

import java.sql.*;
import config.DatabaseConnection;
import entities.Mascota;


import java.util.ArrayList;
import java.util.List;

public class MascotaDao implements GenericDao<Mascota> {

    private static final String INSERT_SQL =
        "INSERT INTO mascota (nombre, especie, raza, fechaNacimiento, duenio) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
        "SELECT * FROM mascota WHERE id = ? AND eliminado = 0";

    private static final String SELECT_ALL =
        "SELECT * FROM mascota WHERE eliminado = 0";

    private static final String UPDATE_SQL =
        "UPDATE mascota SET nombre=?, especie=?, raza=?, fechaNacimiento=?, duenio=? " +
        "WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE mascota SET eliminado = 1 WHERE id=?";

//---- Metodos de la Interfaz ---- 
    
    /**
     * Se crea y guarda una Mascota.
     * Recibe la Connection del Service para la transacción.
     */
    @Override
    public void crear(Mascota m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setMascotaParameters(stmt, m);
            stmt.executeUpdate();
            setGeneratedId(stmt, m);
        }
    }
    /**
     * Actualiza una Mascota.
     * Recibe la Connection del Service.
     */
    
    @Override
    public void actualizar(Mascota m, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            setMascotaParameters(stmt, m);
            stmt.setLong(6, m.getId());
            stmt.executeUpdate();
        }
    }
    
    /**
     * Eliminado lógico de una Mascota.
     * Recibe la Connection del Service.
     */
    
    @Override
    public void eliminar(Long id, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
    /**
     * Lee una Mascota por ID. 
     */

    @Override
    public Mascota leer(Long id) throws Exception {
            try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMascota(rs);
            }
        }
        return null;
    }

    @Override
    public List<Mascota> leerTodos() throws Exception {
        List<Mascota> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapMascota(rs));
            }
        }
        return lista;
    }
    
    // ----------------------------
    // MÉTODOS AUXILIARES
    // ----------------------------

    private void setMascotaParameters(PreparedStatement stmt, Mascota m) throws SQLException {

        stmt.setString(1, m.getNombre());
        stmt.setString(2, m.getEspecie());
        stmt.setString(3, m.getRaza());

        if (m.getFechaNacimiento() != null)
            stmt.setDate(4, Date.valueOf(m.getFechaNacimiento()));
        else
            stmt.setNull(4, Types.DATE);

        stmt.setString(5, m.getDuenio());

    }

    private void setGeneratedId(PreparedStatement stmt, Mascota m) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) m.setId(rs.getLong(1));
        }
    }

    private Mascota mapMascota(ResultSet rs) throws SQLException {
        Mascota m = new Mascota();

        m.setId(rs.getLong("id"));
        m.setEliminado(rs.getBoolean("eliminado"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecie(rs.getString("especie"));
        m.setRaza(rs.getString("raza"));

        Date fecha = rs.getDate("fechaNacimiento");
        m.setFechaNacimiento(fecha != null ? fecha.toLocalDate() : null);

        m.setDuenio(rs.getString("duenio"));

        return m;
    }   

//  METODOS DE BUSQUEDA ADICIONALES 

      // Metodo de busqueda por Nombre

    public List<Mascota> buscarPorNombre(String nombre) throws Exception {
        List<Mascota> lista = new ArrayList<>();
        String SQL = "SELECT * FROM mascota WHERE nombre LIKE ? AND eliminado = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, "%" + nombre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMascota(rs));
                }
            }
        }
        return lista;
    }

    // Metodo de busqueda por Dueño(a)
    
    public List<Mascota> buscarPorDuenio(String duenio) throws Exception {
        List<Mascota> lista = new ArrayList<>();
        String SQL = "SELECT * FROM mascota WHERE duenio LIKE ? AND eliminado = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, "%" + duenio + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMascota(rs));
                }
            }
        }
        return lista;
    }
    // Metodo de busqueda por codigo de Microchip
    public Mascota buscarPorCodigoMicrochip(String codigo) throws Exception {
        String SQL = "SELECT m.* FROM mascota m " +
                     "JOIN microchip c ON m.id = c.mascota_id " +
                     "WHERE c.codigo = ? AND m.eliminado = 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL)) {
            stmt.setString(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMascota(rs);
                }
            }
        }
        return null;
    }
}
