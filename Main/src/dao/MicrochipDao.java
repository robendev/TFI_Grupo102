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

/**
 *
 * @author iRb18
 */
public class MicrochipDao implements GenericDao<Microchip>{
    
    private static final String INSERT_SQL =
        "INSERT INTO microchip (codigo, fechaImplantacion, veterinaria, observaciones, mascota_id) " +
        "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ID =
        "SELECT * FROM microchip WHERE id = ? AND eliminado = 0";

    private static final String SELECT_BY_MASCOTA_ID =
        "SELECT * FROM microchip WHERE mascota_id = ? AND eliminado = 0";

    private static final String SELECT_ALL =
        "SELECT * FROM microchip WHERE eliminado = 0";

    private static final String UPDATE_SQL =
        "UPDATE microchip SET codigo=?, fechaImplantacion=?, veterinaria=?, observaciones=?, mascota_id=? WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE microchip SET eliminado = 1 WHERE id=?";

    @Override
    public void crear(Microchip entidad) throws Exception {
        throw new UnsupportedOperationException("Use crear(Microchip entidad, Long mascotaId) para asociar a una mascota");
    }
    
    public void crear(Microchip entidad, Long mascotaId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            
            setMicrochipParameters(stmt, entidad, mascotaId);
            stmt.executeUpdate();
            
            setGeneratedId(stmt, entidad);
        }
    }

    @Override
    public Microchip leer(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMicrochip(rs);
                }
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
        List<Microchip> chipsList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                chipsList.add(mapMicrochip(rs));
            }
        }
        return chipsList;
    }
    
    @Override
    public void actualizar(Microchip entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setMicrochipParameters(stmt, entidad, entidad.getMascotaId());
            stmt.setLong(6, entidad.getId());
            
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
        }
    }

    private Microchip mapMicrochip(ResultSet rs) throws SQLException {

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
    }
}
