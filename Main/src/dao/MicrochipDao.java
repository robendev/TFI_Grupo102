/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConnection;
import entities.Microchip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MicrochipDao implements GenericDao<Microchip> {

    private static final String INSERT_SQL =
        "INSERT INTO microchip (codigo, fechaImplantacion, veterinaria, observaciones, mascota_id) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
        "SELECT * FROM microchip WHERE id = ? AND eliminado = 0";

    private static final String SELECT_ALL =
        "SELECT * FROM microchip WHERE eliminado = 0";

    private static final String UPDATE_SQL =
        "UPDATE microchip SET codigo=?, fechaImplantacion=?, veterinaria=?, observaciones=?, mascota_id=? " +
        "WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE microchip SET eliminado = 1 WHERE id=?";


    @Override
    public void crear(Microchip chip) throws Exception {
        throw new UnsupportedOperationException(
            "Use crear(chip, mascotaId) para asociar correctamente a mascota"
        );
    }

    public void crear(Microchip chip, Long mascotaId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            setMicrochipParameters(stmt, chip, mascotaId);
            stmt.executeUpdate();
            setGeneratedId(stmt, chip);
        }
    }

    @Override
    public Microchip leer(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMicrochip(rs);
            }
        }
        return null;
    }

    @Override
    public List<Microchip> leerTodos() throws Exception {
        List<Microchip> chips = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                chips.add(mapMicrochip(rs));
            }
        }
        return chips;
    }

    @Override
    public void actualizar(Microchip chip) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            stmt.setString(1, chip.getCodigo());

            if (chip.getFechaImplantacion() != null)
                stmt.setDate(2, Date.valueOf(chip.getFechaImplantacion()));
            else
                stmt.setNull(2, Types.DATE);

            stmt.setString(3, chip.getVeterinaria());
            stmt.setString(4, chip.getObservaciones());

            if (chip.getMascotaId() != null)
                stmt.setLong(5, chip.getMascotaId());
            else
                stmt.setNull(5, Types.INTEGER);

            stmt.setLong(6, chip.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // AUXILIARES

    private void setMicrochipParameters(PreparedStatement stmt, Microchip chip, Long mascotaId) throws Exception {

        stmt.setString(1, chip.getCodigo());

        if (chip.getFechaImplantacion() != null)
            stmt.setDate(2, Date.valueOf(chip.getFechaImplantacion()));
        else
            stmt.setNull(2, Types.DATE);

        stmt.setString(3, chip.getVeterinaria());
        stmt.setString(4, chip.getObservaciones());

        if (mascotaId != null)
            stmt.setLong(5, mascotaId);
        else
            stmt.setNull(5, Types.INTEGER);
    }

    private void setGeneratedId(PreparedStatement stmt, Microchip chip) throws SQLException {
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) chip.setId(rs.getLong(1));
        }
    }

    private Microchip mapMicrochip(ResultSet rs) throws SQLException {
        Microchip m = new Microchip();

        m.setId(rs.getLong("id"));
        m.setEliminado(rs.getBoolean("eliminado"));
        m.setCodigo(rs.getString("codigo"));

        Date fecha = rs.getDate("fechaImplantacion");
        m.setFechaImplantacion(fecha != null ? fecha.toLocalDate() : null);

        m.setVeterinaria(rs.getString("veterinaria"));
        m.setObservaciones(rs.getString("observaciones"));
        m.setMascotaId(rs.getLong("mascota_id"));

        return m;
    }
}
