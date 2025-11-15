
package dao;

import config.DatabaseConnection;
import entities.Microchip;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Implementamos la nueva interfaz GenericDao 
public class MicrochipDao implements GenericDao<Microchip> {

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
        "UPDATE microchip SET codigo=?, fechaImplantacion=?, veterinaria=?, observaciones=?, mascota_id=? " +
        "WHERE id=?";

    private static final String DELETE_SQL =
        "UPDATE microchip SET eliminado = 1 WHERE id=?";

    /** Implementación del "Crear" de la interfaz 
     * Para crear un Microchip, SIEMPRE necesitamos el mascota_id 
     */
    @Override
    public void crear(Microchip chip, Connection conn) throws Exception {
        throw new UnsupportedOperationException(
            "ERROR: Use crear(chip, mascotaId, conn) para asociar el chip a una mascota."
        );
    }
    /** Este metodo se usara en Service 
     * Recibe cla Connection de la transaccion y No la cierra.
     * @param chip El objeto Microchip a guardar (sin ID)
     * @param mascotaId El ID de la Mascota dueño
     * @param conn La conexión transaccional
     */
    
    public void crear(Microchip chip, Long mascotaId, Connection conn) throws Exception {
             try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setMicrochipParameters(stmt, chip, mascotaId);
            stmt.executeUpdate();
            setGeneratedId(stmt, chip);  // Asigna el ID generado al Objeto 'chip'
        }
    }
     /** Implementa el Leer de la interfaz. Usa Long id 
     * los metodos de lectura se puede abrir su popia conexión .
     * @param id El ID (Long) del microchip a buscar.
     * @return El Microchip encontrado, o null.
     * @throws Exception Si hay un error de SQL.
     */
    
    @Override
    public Microchip leer(Long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID)) {

            stmt.setLong(1, id); // <-- 4. Corregido a setLong

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMicrochip(rs);
            }
        }
        return null;
    }
    
    /**
     * Método para que el Service pueda
     * encontrar el chip de una mascota.
     */
    public Microchip leerPorMascotaId(Long mascotaId) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_MASCOTA_ID)) {
            
            stmt.setLong(1, mascotaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapMicrochip(rs);
            }
        }
        return null;
    }
    
    // Lista todo lo de la tabla 
    
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
     /**
     * Implementa el "actualizar" de la interfaz
     * Recibe la Connection de la transacción y NO la cierra..
     */
    @Override
    public void actualizar(Microchip chip, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

            setMicrochipParameters(stmt, chip, chip.getMascotaId()); // Reutilizamos el auxiliar setMicrochipParameters

            stmt.setLong(6, chip.getId()); // El ID para el WHERE
            stmt.executeUpdate();
        }
    }
    

    @Override
    public void eliminar(Long id, Connection conn) throws Exception {
             try (PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
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
            stmt.setNull(5, Types.BIGINT);
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
