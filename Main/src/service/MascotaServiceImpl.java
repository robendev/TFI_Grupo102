package service;

import dao.GenericDao;
import entities.Mascota;
import entities.Microchip;

import java.util.List;

/**
 * Implementación del servicio de negocio para la entidad Mascota.
 * Capa intermedia entre la UI y el DAO que aplica validaciones de negocio.
 *
 * Responsabilidades:
 * - Validar que los datos de la mascota sean correctos ANTES de persistir
 * - Coordinar la creación/actualización del Microchip asociado (si existe)
 * - Delegar operaciones de BD al DAO de Mascota
 *
 * Patrón: Service Layer con inyección de dependencias
 */
public class MascotaServiceImpl implements GenericService<Mascota> {

    /**
     * DAO para acceso a datos de mascotas.
     * Inyectado en el constructor (Dependency Injection).
     */
    private final GenericDao<Mascota> mascotaDao;

    /**
     * Servicio de microchips para coordinar operaciones sobre la relación 1:1.
     * Permite:
     * - Insertar o actualizar el microchip antes de persistir la mascota.
     */
    private final MicrochipServiceImpl microchipService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param mascotaDao      DAO de mascotas (normalmente MascotaDao)
     * @param microchipService Servicio de microchips para operaciones coordinadas
     */
    public MascotaServiceImpl(GenericDao<Mascota> mascotaDao,
                              MicrochipServiceImpl microchipService) {
        if (mascotaDao == null) {
            throw new IllegalArgumentException("MascotaDao no puede ser null");
        }
        if (microchipService == null) {
            throw new IllegalArgumentException("MicrochipServiceImpl no puede ser null");
        }
        this.mascotaDao = mascotaDao;
        this.microchipService = microchipService;
    }

    /**
     * Inserta una nueva mascota en la base de datos.
     *
     * Flujo:
     * 1. Valida los datos de la mascota (nombre, especie, dueño)
     * 2. Si la mascota tiene microchip asociado:
     *    - Si microchip.id == 0 → se inserta el microchip
     *    - Si microchip.id > 0 → se actualiza el microchip
     * 3. Inserta la mascota en la BD
     *
     * @param mascota Mascota a insertar
     * @throws Exception Si la validación falla o hay error de BD
     */
    @Override
    public void insertar(Mascota mascota) throws Exception {
        validateMascota(mascota);

        Microchip chip = mascota.getMicrochip();
        if (chip != null) {
            if (chip.getId() == 0) {
                // Microchip nuevo: insertar
                microchipService.insertar(chip);
            } else {
                // Microchip existente: actualizar
                microchipService.actualizar(chip);
            }
        }

        mascotaDao.crear(mascota);
    }

    /**
     * Actualiza una mascota existente en la base de datos.
     *
     * Validaciones:
     * - La mascota debe tener datos válidos
     * - El ID debe ser > 0
     * - Si tiene microchip:
     *   - Si id == 0 → se inserta
     *   - Si id > 0 → se actualiza
     *
     * @param mascota Mascota con datos actualizados
     * @throws Exception Si la validación falla o la mascota no existe
     */
    @Override
    public void actualizar(Mascota mascota) throws Exception {
        validateMascota(mascota);

        if (mascota.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la mascota debe ser mayor a 0 para actualizar");
        }

        Microchip chip = mascota.getMicrochip();
        if (chip != null) {
            if (chip.getId() == 0) {
                microchipService.insertar(chip);
            } else {
                microchipService.actualizar(chip);
            }
        }

        mascotaDao.actualizar(mascota);
    }

    /**
     * Elimina lógicamente una mascota (soft delete).
     *
     * @param id ID de la mascota a eliminar
     * @throws Exception Si id <= 0 o no existe la mascota
     */
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        mascotaDao.eliminar(id);
    }

    /**
     * Obtiene una mascota por su ID.
     *
     * @param id ID de la mascota a buscar
     * @return Mascota encontrada o null si no existe o está eliminada
     * @throws Exception Si id <= 0 o hay error de BD
     */
    @Override
    public Mascota getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return mascotaDao.leer(id);
    }

    /**
     * Obtiene todas las mascotas activas (eliminado=FALSE).
     *
     * @return Lista de mascotas activas (puede estar vacía)
     * @throws Exception Si hay error de BD
     */
    @Override
    public List<Mascota> getAll() throws Exception {
        return mascotaDao.leerTodos();
    }

    // VALIDACIONES DE NEGOCIO

    /**
     * Valida que una mascota tenga datos correctos.
     *
     * Reglas:
     * - Nombre obligatorio
     * - Especie obligatoria
     * - Dueño obligatorio
     *
     * @param mascota Mascota a validar
     * @throws IllegalArgumentException Si alguna validación falla
     */
    private void validateMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser null");
        }
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la mascota no puede estar vacío");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new IllegalArgumentException("La especie de la mascota no puede estar vacía");
        }
        if (mascota.getDuenio() == null || mascota.getDuenio().trim().isEmpty()) {
            throw new IllegalArgumentException("El dueño de la mascota no puede estar vacío");
        }
    }
}
