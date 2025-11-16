package service;

import dao.MicrochipDao;
import entities.Microchip;
import java.util.List;

/**
 * Servicio de negocio para Microchip.
 * En la arquitectura A->B, este servicio NO DEBE manejar
 * la creación o eliminación de chips, ya que eso se
 * orquesta desde MascotaServiceImpl.
 *
 * Este servicio solo se usa para LECTURAS específicas de Microchip.
 */

public class MicrochipServiceImpl implements GenericService<Microchip> {

    private final MicrochipDao microchipDao;

    public MicrochipServiceImpl(MicrochipDao microchipDao) {
        if (microchipDao == null) {
            throw new IllegalArgumentException("MicrochipDao no puede ser null");
        }
        this.microchipDao = microchipDao;
    }
    // --- MÉTODOS CUD (Crear, Actualizar, Eliminar) ---
    // Estos métodos NO tienen sentido aquí. La lógica de negocio
    // dicta que un Chip solo se crea/actualiza/elimina
    // JUNTO con su Mascota, a través de MascotaServiceImpl.
    
    @Override
    public void crear(Microchip chip) throws Exception {
        throw new UnsupportedOperationException(
            "Error: Un Microchip no se puede crear solo. Use MascotaService.crear(mascota)."
        );
    }

    @Override
    public void actualizar(Microchip chip) throws Exception {
        throw new UnsupportedOperationException(
            "Error: Un Microchip se actualiza via MascotaService.actualizar(mascota)."
        );
    }
    
    @Override
    public void eliminar(Long id) throws Exception { // <-- CORREGIDO A Long
        throw new UnsupportedOperationException(
            "Error: Un Microchip se elimina via MascotaService.eliminar(mascotaId)."
        );
    }
    
    // --- MÉTODOS DE LECTURA ---
    
    @Override
    public Microchip leer(Long id) throws Exception { 
        if (id == null || id <= 0) throw new IllegalArgumentException("ID debe ser válido");
        return microchipDao.leer(id);
    }
    
    @Override
    public List<Microchip> leerTodos() throws Exception {
        return microchipDao.leerTodos();
    }

    
    // VALIDACIONES DE NEGOCIO

   public void validateCodigoUnico(String codigo, Long chipId) throws Exception {
        
        // ¡Validación eficiente! Usamos el nuevo método del DAO.
        Microchip chipExistente = microchipDao.buscarPorCodigo(codigo);
        
        if (chipExistente != null) {
            // Si el chipId es null (estamos creando) O
            // si el ID encontrado es diferente al que estamos actualizando...
            // ¡entonces es un duplicado!
            if (chipId == null || !chipExistente.getId().equals(chipId)) {
                throw new IllegalArgumentException(
                    "El código de microchip ya existe: " + codigo
                );
            }
        }
    } 
}
