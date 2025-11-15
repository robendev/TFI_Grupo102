package dao;

import java.sql.*;
import config.DatabaseConnection;
import entities.Mascota;
import entities.Microchip;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author iRb18
 */
public class MascotaDao implements GenericDao<Mascota>{
    
    private static final String INSERT_SQL = "INSERT INTO mascota (nombre, especie, raza, fechaNacimiento, duenio) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = "SELECT * FROM mascota WHERE id = ? AND eliminado = 0";
    
    private static final String SELECT_ALL = "SELECT * FROM mascota WHERE eliminado = 0";
    
    private static final String UPDATE_SQL = "UPDATE mascota SET nombre=?, especie=?, raza=?, fechaNacimiento=?, duenio=? WHERE id=?";
    
    private static final String DELETE_SQL = "UPDATE mascota SET eliminado = 1 WHERE id=?";

    // Necesitamos MicrochipDao para:
    // - crear microchip de mascota
    // - leer microchip asociado
    private final MicrochipDao microchipDao = new MicrochipDao();
    
    @Override
    public void crear(Mascota entidad) throws Exception {
       try (Connection conn = DatabaseConnection.getConnection();
               PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
           
           setMascotaParameters(stmt, entidad);
           stmt.executeUpdate();
           
           setGeneratedId(stmt, entidad);
       }
       
       // Si mascota tiene un microchip, recien ahora lo puedo crear
        if (entidad.getMicrochip() != null) {
            
            Microchip chip = entidad.getMicrochip();
            
            microchipDao.crear(chip, entidad.getId());
        }
    }

    @Override
    public Mascota leer(Long id) throws Exception {
        Mascota mascota = null;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    mapMascota(rs);
                }
            }
        }
        
        if (mascota != null) {
            Microchip chip = microchipDao.leerPorMascotaId(id);
            mascota.setMicrochip(chip);
        }
        
        return mascota;
    }

    @Override
    public List<Mascota> leerTodos() throws Exception {
        List<Mascota> mascotasList = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Mascota m = mapMascota(rs);
                
                Microchip chip = microchipDao.leerPorMascotaId(m.getId());
                m.setMicrochip(chip);
                
                mascotasList.add(m);
            }
        }
        return mascotasList;
    }

    @Override
    public void actualizar(Mascota m) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            setMascotaParameters(stmt, entidad);
            stmt.setLong(6, entidad.getId());
            stmt.executeUpdate();
        }
        
        if (entidad.getMicrochip() != null) {
            Microchip chip = entidad.getMicrochip();
            
            if (chip.getId() == null) {
                microchipDao.crear(chip, entidad.getId());
            } else {
                microchipDao.actualizar(chip);
            }
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
    
    // Parametros del INSERT y UPDATE
    private void setMascotaParameters(PreparedStatement stmt, Mascota entidad) throws SQLException{
        stmt.setString(1, entidad.getNombre());
        stmt.setString(2, entidad.getEspecie());
        stmt.setString(3, entidad.getRaza());
        stmt.setDate(4, entidad.getFechaNacimiento() != null ? Date.valueOf(entidad.getFechaNacimiento()) : null);
        stmt.setString(5, entidad.getDuenio());
    }
    
    private void setGeneratedId(PreparedStatement stmt, Mascota entidad) throws SQLException {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            
            if (keys.next()) {
                entidad.setId(keys.getLong(1));
            } else {
                throw new SQLException("No se obtuvo ID generado.");
            }
        }
    }
    
    // Convertir un ResultSet en un Obj 
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
}
