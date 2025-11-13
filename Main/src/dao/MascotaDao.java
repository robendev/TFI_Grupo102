/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import config.DatabaseConnection;
import entities.Mascota;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iRb18
 */
public class MascotaDao implements GenericDao<Mascota>{
    
    private static final String INSERT_SQL = "INSERT INTO mascota (nombre, especie, raza, fechaNacimiento, duenio) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = "SELECT * FROM mascota WHERE id ? = AND eliminado = 0";
    
    private static final String SELECT_ALL = "SELECT * FROM mascota WHERE eliminado = 0";
    
    private static final String UPDATE_SQL = "UPDATE mascota SET nombre=?, especie=?, raza=?, fechaNacimiento=?, duenio=? WHERE id=?";
    
    private static final String DELETE_SQL = "UPDATE mascota SET eliminado = 1 WHERE id=?";

    
    @Override
    public void crear(Mascota entidad) throws Exception {
       try (Connection conn = DatabaseConnection.getConnection();
               PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
           setMascotaParameters(stmt, entidad);
           stmt.executeUpdate();
           setGeneratedId(stmt, entidad);
       }
    }

    @Override
    public Mascota leer(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapMascota(rs);
                }
            }
        }
        return null;
    }

    
@Override
    public List<Mascota> leerTodos() throws Exception {
        List<Mascota> mascotas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                mascotas.add(mapMascota(rs));
            }
        }
        return mascotas;
    }

    @Override
    public void actualizar(Mascota entidad) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            setMascotaParameters(stmt, entidad);
            stmt.setLong(6, entidad.getId());
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

    
    private void setMascotaParameters(PreparedStatement stmt, Mascota entidad) throws SQLException{
        stmt.setString(1, entidad.getNombre());
        stmt.setString(2, entidad.getEspecie());
        stmt.setString(3, entidad.getRaza());
        stmt.setDate(4, java.sql.Date.valueOf(entidad.getFechaNacimiento()));
        stmt.setString(5, entidad.getDuenio());
    }
    
    private void setGeneratedId(PreparedStatement stmt, Mascota entidad) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                entidad.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("La inserción de la persona falló, no se obtuvo ID generado");
            }
        }
    }
    
    private Mascota mapMascota(ResultSet rs) throws SQLException {
        Mascota mascota = new Mascota();
        mascota.setId(rs.getLong("id"));
        mascota.setEliminado(rs.getBoolean("eliminado"));
        mascota.setNombre(rs.getString("nombre"));
        mascota.setEspecie(rs.getString("especie"));
        mascota.setRaza(rs.getString("raza"));
        Date fecha = rs.getDate("fechaNacimiento");
        mascota.setFechaNacimiento(fecha != null ? fecha.toLocalDate() : null);
        mascota.setDuenio(rs.getString("duenio"));
        return mascota;
    }
}
