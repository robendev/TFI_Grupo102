/*
 * Servicio de negocio para Microchip
 */
package service;

import dao.GenericDao;
import entities.Microchip;

import java.util.List;

/**
 * Implementación del servicio de negocio para la entidad Microchip.
 * 
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio.
 *
 * Responsabilidades:
 * - Validar que los datos del microchip sean correctos ANTES de persistir
 * - Garantizar unicidad del código de microchip en el sistema
 * - Delegar operaciones de BD al DAO
 *
 * Patrón: Service Layer con inyección de dependencias
 */
public class MicrochipServiceImpl implements GenericService<Microchip> {

    /**
     * DAO para acceso a datos de microchips.
     * Inyectado en el constructor (Dependency Injection).
     */
    private final GenericDao<Microchip> microchipDao;

    /**
     * Constructor con inyección de dependencias.
     * Valida que el DAO no sea null (fail-fast).
     *
     * @param microchipDao DAO de microchips (normalmente MicrochipDao)
     */
    public MicrochipServiceImpl(GenericDao<Microchip> microchipDao) {
        if (microchipDao == null) {
            throw new IllegalArgumentException("MicrochipDao no puede ser null");
        }
        this.microchipDao = microchipDao;
    }

    /**
     * Inserta un nuevo microchip en la base de datos.
     *
     * Flujo:
     * 1. Valida campos obligatorios del microchip
     * 2. Valida que el código sea único en el sistema
     * 3. Delega al DAO para crear el registro
     *
     * @param microchip Microchip a insertar
     * @throws Exception Si la validación falla o hay error de BD
     */
    @Override
    public void insertar(Microchip microchip) throws Exception {
        validateMicrochip(microchip);
        validateCodigoUnico(microchip.getCodigo(), null);
        microchipDao.crear(microchip);
    }

    /**
     * Actualiza un microchip existente en la base de datos.
     *
     * Validaciones:
     * - El microchip debe tener datos válidos
     * - El ID debe ser > 0
     * - El código debe ser único (se permite el mismo código para el mismo ID)
     *
     * @param microchip Microchip con los datos actualizados
     * @throws Exception Si la validación falla o el microchip no existe
     */
    @Override
    public void actualizar(Microchip microchip) throws Exception {
        validateMicrochip(microchip);
        if (microchip.getId() <= 0) {
            throw new IllegalArgumentException("El ID del microchip debe ser mayor a 0 para actualizar");
        }
        validateCodigoUnico(microchip.getCodigo(), (int) microchip.getId());
        microchipDao.actualizar(microchip);
    }

    /**
     * Elimina lógicamente un microchip (soft delete).
     * Marca el microchip como eliminado=TRUE sin borrarlo físicamente.
     *
     * @param id ID del microchip a eliminar
     * @throws Exception Si id <= 0 o no existe el microchip
     */
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        microchipDao.eliminar(id);
    }

    /**
     * Obtiene un microchip por su ID.
     *
     * @param id ID del microchip a buscar
     * @return Microchip encontrado, o null si no existe o está eliminado
     * @throws Exception Si id <= 0 o hay error de BD
     */
    @Override
    public Microchip getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return microchipDao.leer(id);
    }

    /**
     * Obtiene todos los microchips activos (eliminado=FALSE).
     *
     * @return Lista de microchips activos (puede estar vacía)
     * @throws Exception Si hay error de BD
     */
    @Override
    public List<Microchip> getAll() throws Exception {
        return microchipDao.leerTodos();
    }


    // VALIDACIONES DE NEGOCIO

    /**
     * Valida que un microchip tenga datos correctos.
     *
     * Reglas:
     * - Código es obligatorio
     * - Se hace trim() para evitar strings vacíos con espacios
     *
     * @param microchip Microchip a validar
     * @throws IllegalArgumentException Si alguna validación falla
     */
    private void validateMicrochip(Microchip microchip) {
        if (microchip == null) {
            throw new IllegalArgumentException("El microchip no puede ser null");
        }
        if (microchip.getCodigo() == null || microchip.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del microchip no puede estar vacío");
        }
    }

    /**
     * Valida que el código de microchip sea único.
     *
     * Lógica:
     * - Recorre todos los microchips no eliminados
     * - Si encuentra otro microchip con el mismo código:
     *   - Si es INSERT (microchipId == null) → error
     *   - Si es UPDATE y el ID es distinto → error
     *
     * @param codigo Código a validar
     * @param microchipId ID del microchip actual (null en INSERT)
     * @throws Exception Si el código ya está usado por otro microchip
     */
    private void validateCodigoUnico(String codigo, Integer microchipId) throws Exception {
        List<Microchip> chips = microchipDao.leerTodos();
        for (Microchip existente : chips) {
            if (existente.getCodigo() != null &&
                existente.getCodigo().equalsIgnoreCase(codigo)) {

                if (microchipId == null || existente.getId() != microchipId) {
                    throw new IllegalArgumentException(
                        "Ya existe un microchip con el código: " + codigo
                    );
                }
            }
        }
    }
}
