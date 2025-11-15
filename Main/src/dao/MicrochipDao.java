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

<<<<<<< HEAD
/**
 *
 * @author iRb18
 */
public class MicrochipDao implements GenericDao<Microchip>{
    
=======
public class MicrochipDao implements GenericDao<Microchip> {

>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
    private static final String INSERT_SQL =
        "INSERT INTO microchip (codigo, fechaImplantacion, veterinaria, observaciones, mascota_id) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
        "SELECT * FROM microchip WHERE id = ? AND eliminado = 0";

<<<<<<< HEAD
    private static final String SELECT_BY_MASCOTA_ID =
        "SELECT * FROM microchip WHERE mascota_id = ? AND eliminado = 0";

=======
>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
    private static final String SELECT_ALL =
        "SELECT * FROM microchip WHERE eliminado = 0";

    private static final String UPDATE_SQL =
<<<<<<< HEAD
        "UPDATE microchip SET codigo=?, fechaImplantacion=?, veterinaria=?, observaciones=?, mascota_id=? WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE microchip SET eliminado = 1 WHERE id=?";
=======
        "UPDATE microchip SET codigo=?, fechaImplantacion=?, veterinaria=?, observaciones=?, mascota_id=? " +
        "WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE microchip SET eliminado = 1 WHERE id=?";

>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f

    @Override
    public void crear(Microchip chip) throws Exception {
        throw new UnsupportedOperationException(
            "Use crear(chip, mascotaId) para asociar correctamente a mascota"
        );
    }

    public void crear(Microchip chip, Long mascotaId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
<<<<<<< HEAD
            
            setMicrochipParameters(stmt, entidad, mascotaId);
            stmt.executeUpdate();
            
            setGeneratedId(stmt, entidad);
=======

            setMicrochipParameters(stmt, chip, mascotaId);
            stmt.executeUpdate();
            setGeneratedId(stmt, chip);
>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
        }
    }

    @Override
    public Microchip leer(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
<<<<<<< HEAD
            
            stmt.setLong(1, id);
            
=======

            stmt.setLong(1, id);

>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMicrochip(rs);
            }
        }
        return null;
    }

    public Microchip leerPorMascotaId(Long mascotaId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MASCOTA_ID)) {
            
            stmt.setLong(1, mascotaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMicrochip(rs);
                }
            }
        } 
        return null;
    }
    
    @Override
    public List<Microchip> leerTodos() throws Exception {
<<<<<<< HEAD
        List<Microchip> chipsList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
=======
        List<Microchip> chips = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
            while (rs.next()) {
                chipsList.add(mapMicrochip(rs));
            }
        }
        return chipsList;
    }

    @Override
    public void actualizar(Microchip chip) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
<<<<<<< HEAD
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setMicrochipParameters(stmt, entidad, entidad.getMascotaId());
            stmt.setLong(6, entidad.getId());
            
=======
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
>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws Exception {

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

<<<<<<< HEAD
    private void setMicrochipParameters(PreparedStatement stmt, Microchip entidad, Long mascotaId) throws SQLException {
        
        stmt.setString(1, entidad.getCodigo());
        stmt.setDate(2, entidad.getFechaImplantacion() != null ?
                          Date.valueOf(entidad.getFechaImplantacion()) : null);
        stmt.setString(3, entidad.getVeterinaria());
        stmt.setString(4, entidad.getObservaciones());
        stmt.setLong(5, mascotaId);
    }

    private void setGeneratedId(PreparedStatement stmt, Microchip chip) throws SQLException {

        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                chip.setId(keys.getLong(1));
            }
=======
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
>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
        }
    }

    private Microchip mapMicrochip(ResultSet rs) throws SQLException {
<<<<<<< HEAD

        Microchip chip = new Microchip();

        chip.setId(rs.getLong("id"));
        chip.setEliminado(rs.getBoolean("eliminado"));
        chip.setCodigo(rs.getString("codigo"));

        Date fecha = rs.getDate("fechaImplantacion");
        chip.setFechaImplantacion(fecha != null ? fecha.toLocalDate() : null);

        chip.setVeterinaria(rs.getString("veterinaria"));
        chip.setObservaciones(rs.getString("observaciones"));
        chip.setMascotaId(rs.getLong("mascota_id"));

        return chip;
=======
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
>>>>>>> b1e43cd37acb78c22eb6775a6170cde46915353f
    }
}
