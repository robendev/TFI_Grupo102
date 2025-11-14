package dao;

import java.sql.*;
import config.DatabaseConnection;
import entities.Mascota;
import entities.Microchip;

import java.util.ArrayList;
import java.util.List;

public class MascotaDao implements GenericDao<Mascota> {

    private static final String INSERT_SQL =
        "INSERT INTO mascota (nombre, especie, raza, fechaNacimiento, duenio, microchip_id) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
        "SELECT * FROM mascota WHERE id = ? AND eliminado = 0";

    private static final String SELECT_ALL =
        "SELECT * FROM mascota WHERE eliminado = 0";

    private static final String UPDATE_SQL =
        "UPDATE mascota SET nombre=?, especie=?, raza=?, fechaNacimiento=?, duenio=?, microchip_id=? " +
        "WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE mascota SET eliminado = 1 WHERE id=?";

    private final MicrochipDao microchipDao = new MicrochipDao();


    @Override
    public void crear(Mascota m) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setMascotaParameters(stmt, m);
            stmt.executeUpdate();
            setGeneratedId(stmt, m);
        }
    }

    @Override
    public Mascota leer(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);

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

    @Override
    public void actualizar(Mascota m) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            setMascotaParameters(stmt, m);
            stmt.setLong(7, m.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ----------------------------
    // MÉTODOS AUXILIARES
    // ----------------------------

    private void setMascotaParameters(PreparedStatement stmt, Mascota m) throws Exception {

        stmt.setString(1, m.getNombre());
        stmt.setString(2, m.getEspecie());
        stmt.setString(3, m.getRaza());

        if (m.getFechaNacimiento() != null)
            stmt.setDate(4, Date.valueOf(m.getFechaNacimiento()));
        else
            stmt.setNull(4, Types.DATE);

        stmt.setString(5, m.getDuenio());

        if (m.getMicrochip() != null)
            stmt.setLong(6, m.getMicrochip().getId());
        else
            stmt.setNull(6, Types.INTEGER);
    }

    private void setGeneratedId(PreparedStatement stmt, Mascota m) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) m.setId(rs.getLong(1));
        }
    }

    private Mascota mapMascota(ResultSet rs) throws Exception {
        Mascota m = new Mascota();

        m.setId(rs.getLong("id"));
        m.setEliminado(rs.getBoolean("eliminado"));
        m.setNombre(rs.getString("nombre"));
        m.setEspecie(rs.getString("especie"));
        m.setRaza(rs.getString("raza"));

        Date fecha = rs.getDate("fechaNacimiento");
        m.setFechaNacimiento(fecha != null ? fecha.toLocalDate() : null);

        m.setDuenio(rs.getString("duenio"));

        long microchipId = rs.getLong("microchip_id");
        if (!rs.wasNull()) {
            Microchip chip = microchipDao.leer((int) microchipId);
            m.setMicrochip(chip);
        }

        return m;
    }
}
