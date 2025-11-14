package service;

import dao.MascotaDao;
import dao.MicrochipDao;
import entities.Mascota;
import entities.Microchip;

import java.util.List;

/**
 * Servicio de negocio para Mascota.
 * Basado en UML y en la arquitectura PersonaServiceImpl/DomicilioServiceImpl.
 *
 * Reglas:
 * - Nombre, especie y dueño son obligatorios
 * - Microchip opcional
 * - Relación 1:1 con Microchip
 * - Si el microchip es nuevo, se crea primero
 * - Si existe, se actualiza antes de actualizar la mascota
 */
public class MascotaServiceImpl implements GenericService<Mascota> {

    private final MascotaDao mascotaDao;
    private final MicrochipDao microchipDao;

    public MascotaServiceImpl(MascotaDao mascotaDao, MicrochipDao microchipDao) {
        if (mascotaDao == null)
            throw new IllegalArgumentException("MascotaDao no puede ser null");
        if (microchipDao == null)
            throw new IllegalArgumentException("MicrochipDao no puede ser null");

        this.mascotaDao = mascotaDao;
        this.microchipDao = microchipDao;
    }

    @Override
    public void crear(Mascota mascota) throws Exception {
        validateMascota(mascota);

        Microchip chip = mascota.getMicrochip();

        // Regla: Microchip primero
        if (chip != null) {
            if (chip.getId() == 0) {
                microchipDao.crear(chip);
            } else {
                microchipDao.actualizar(chip);
            }
        }

        mascotaDao.crear(mascota);
    }

    @Override
    public Mascota leer(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser mayor a 0");
        return mascotaDao.leer(id);
    }

    @Override
    public List<Mascota> leerTodos() throws Exception {
        return mascotaDao.leerTodos();
    }

    @Override
    public void actualizar(Mascota mascota) throws Exception {
        validateMascota(mascota);

        if (mascota.getId() <= 0)
            throw new IllegalArgumentException("El ID debe ser mayor a 0 para actualizar");

        Microchip chip = mascota.getMicrochip();

        if (chip != null) {
            if (chip.getId() == 0) {
                microchipDao.crear(chip);
            } else {
                microchipDao.actualizar(chip);
            }
        }

        mascotaDao.actualizar(mascota);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("ID debe ser mayor a 0");

        mascotaDao.eliminar(id);
    }

    // --------------------------
    // VALIDACIONES DE NEGOCIO
    // --------------------------

    private void validateMascota(Mascota m) {
        if (m == null)
            throw new IllegalArgumentException("La mascota no puede ser null");

        if (m.getNombre() == null || m.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio");

        if (m.getEspecie() == null || m.getEspecie().trim().isEmpty())
            throw new IllegalArgumentException("La especie de la mascota es obligatoria");

        if (m.getDuenio() == null || m.getDuenio().trim().isEmpty())
            throw new IllegalArgumentException("El dueño de la mascota es obligatorio");
    }
}
