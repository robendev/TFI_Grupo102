package service;

import dao.MascotaDao;
import dao.MicrochipDao;
import entities.Mascota;
import entities.Microchip;

import java.util.List;

/**
 * Servicio de negocio para Mascota.
 *
 * Reglas de negocio derivadas del UML y TFI:
 * - Mascota puede tener 0..1 Microchip
 * - Si trae un microchip nuevo (id = 0) → se crea primero
 * - Si trae uno existente → se actualiza antes de actualizar la mascota
 * - Eliminación lógica
 * - Validaciones de nombre, especie y dueño
 *
 * No se utilizan transacciones manuales porque los DAOs
 * manejan su propia conexión, igual que PersonaServiceImpl.
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

        //  Microchip primero (si existe)
        if (chip != null) {
            if (chip.getId() == 0) {
                // Insertar microchip asociado a la mascota
                microchipDao.crear(chip, null);  // mascota aún no tiene ID
            } else {
                microchipDao.actualizar(chip);
            }
        }

        // Crear mascota
        mascotaDao.crear(mascota);

        // Si el microchip es nuevo, actualizar mascota con FK correcta
        if (chip != null && chip.getId() != 0) {
            mascotaDao.actualizar(mascota);
        }
    }

    @Override
    public Mascota leer(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
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

        // Microchip primero
        if (chip != null) {
            if (chip.getId() == 0) {
                
                microchipDao.crear(chip, mascota.getId());
            } else {
                microchipDao.actualizar(chip);
            }
        }

        // Actualizar mascota
        mascotaDao.actualizar(mascota);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El ID debe ser mayor a 0");

        mascotaDao.eliminar(id);
    }

     private void validateMascota(Mascota m) {
        if (m == null)
            throw new IllegalArgumentException("La mascota no puede ser null");

        if (m.getNombre() == null || m.getNombre().trim().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (m.getEspecie() == null || m.getEspecie().trim().isEmpty())
            throw new IllegalArgumentException("La especie es obligatoria");

        if (m.getDuenio() == null || m.getDuenio().trim().isEmpty())
            throw new IllegalArgumentException("El dueño es obligatorio");
    }
}
