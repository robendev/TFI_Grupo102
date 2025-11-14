package service;

import dao.MicrochipDao;
import entities.Microchip;
import java.util.List;

/**
 * Servicio de negocio para Microchip.
 * Basado en UML y en la arquitectura de PersonaServiceImpl/DomicilioServiceImpl.
 *
 * Reglas:
 * - Código obligatorio
 * - Código único
 * - Eliminación lógica
 * - Un microchip pertenece a una sola mascota (1:1)
 */
public class MicrochipServiceImpl implements GenericService<Microchip> {

    private final MicrochipDao microchipDao;

    public MicrochipServiceImpl(MicrochipDao microchipDao) {
        if (microchipDao == null) {
            throw new IllegalArgumentException("MicrochipDao no puede ser null");
        }
        this.microchipDao = microchipDao;
    }

    @Override
    public void crear(Microchip chip) throws Exception {
        validateMicrochip(chip);
        validateCodigoUnico(chip.getCodigo(), null);
        microchipDao.crear(chip);
    }

    @Override
    public Microchip leer(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser mayor a 0");
        return microchipDao.leer(id);
    }

    @Override
    public List<Microchip> leerTodos() throws Exception {
        return microchipDao.leerTodos();
    }

    @Override
    public void actualizar(Microchip chip) throws Exception {
        validateMicrochip(chip);

        if (chip.getId() <= 0)
            throw new IllegalArgumentException("El ID debe ser mayor a 0 para actualizar");

        validateCodigoUnico(chip.getCodigo(), chip.getId());

        microchipDao.actualizar(chip);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("ID debe ser mayor a 0");
        microchipDao.eliminar(id);
    }

    // ------------------------------
    // VALIDACIONES DE NEGOCIO
    // ------------------------------

    private void validateMicrochip(Microchip chip) {
        if (chip == null)
            throw new IllegalArgumentException("El microchip no puede ser null");

        if (chip.getCodigo() == null || chip.getCodigo().trim().isEmpty())
            throw new IllegalArgumentException("El código del microchip es obligatorio");
    }

    private void validateCodigoUnico(String codigo, Integer chipId) throws Exception {
        List<Microchip> chips = microchipDao.leerTodos();

        for (Microchip c : chips) {
            if (c.getCodigo().equalsIgnoreCase(codigo)) {
                if (chipId == null || !c.getId().equals(chipId)) {
                    throw new IllegalArgumentException("El código de microchip ya existe: " + codigo);
                }
            }
        }
    }
}
