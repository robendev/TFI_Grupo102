package service;

import dao.MascotaDao;
import dao.MicrochipDao;
import entities.Mascota;
import entities.Microchip;
import config.TransactionManager; // Se importa para las Trasacciones 

import java.sql.Connection; 
import java.util.List;

/**
 * Servicio de negocio para Mascota.
 *
 * Esta clase SÍ maneja las transacciones manuales
 * para asegurar la integridad de la relación 1-a-1.
 * Orquesta a los DAOs (MascotaDao y MicrochipDao).
 */

public class MascotaServiceImpl implements GenericService<Mascota> {

    private final MascotaDao mascotaDao;
    private final MicrochipDao microchipDao;
    
    // Constructor
    public MascotaServiceImpl(MascotaDao mascotaDao, MicrochipDao microchipDao) {
        if (mascotaDao == null)
            throw new IllegalArgumentException("MascotaDao no puede ser null");
        if (microchipDao == null)
            throw new IllegalArgumentException("MicrochipDao no puede ser null");

        this.mascotaDao = mascotaDao;
        this.microchipDao = microchipDao;
    }
    
    /**
     * Orquesta la creación de una Mascota y su Microchip
     * dentro de una transacción segura.
     */

@Override
    public void crear(Mascota mascota) throws Exception {
        // Validacion reglas de negocio
        validateMascota(mascota);
        if (mascota.getMicrochip() == null) {
            throw new IllegalArgumentException("La Mascota debe tener un Microchip para ser creada.");
        }
        // Validar que el chip sea nuevo
        if (mascota.getMicrochip().getId() != null) {
            throw new IllegalArgumentException("Para crear, el Microchip debe ser nuevo (id=null).");
        }
        
        // Orquestar la Transacción
        TransactionManager txManager = null;
        try {
            // Obtenemos una conexión y la ponemos en modo transacción
            // Usamos try-with-resources (AutoCloseable)
            txManager = new TransactionManager();
            Connection conn = txManager.getConnection();
            txManager.startTransaction(); // -> setAutoCommit(false)

            // --- INICIO DE LA TRANSACCIÓN ---
            
            // Paso A: Crear la Mascota (A)
            // Le pasamos la conexión transaccional
            mascotaDao.crear(mascota, conn); 
          
            
            // Paso B: Crear el Microchip (B)
            Microchip chip = mascota.getMicrochip();
            Long mascotaId = mascota.getId(); // Usamos el ID de la mascota recién creada
            
            // Le pasamos la MISMA conexión transaccional
            microchipDao.crear(chip, mascotaId, conn);

            // --- FIN DE LA TRANSACCIÓN ---
            
            // Si todo salió bien (ninguna Excepción), guardamos los cambios.
            txManager.commit();
            System.out.println("Mascota creada exitosamente.");

        } catch (Exception e) {
            // Si algo falló (ej. el chip ya existía), deshacemos todo.
            System.err.println("Error en Service: " + e.getMessage());
            if (txManager != null) {
                txManager.rollback();
            }
            // Lanzamos la excepción para que el Menú la atrape
            throw e; 
        } finally {
            // Pase lo que pase, cerramos la conexión al final.
            if (txManager != null) {
                txManager.close();
            }
        }
    }
/**
     * Lee una Mascota por ID.
     * Carga también su Microchip asociado.
     */
    
@Override
    public Mascota leer(Long id) throws Exception { 
        if (id <= 0) throw new IllegalArgumentException("El ID debe ser mayor a 0");
        
        // Se lee la mascota
        Mascota mascota = mascotaDao.leer(id);
        
        // Se lee y adjunta el chip (si existe)
        if (mascota != null) {
            Microchip chip = microchipDao.leerPorMascotaId(mascota.getId());
            mascota.setMicrochip(chip); // ¡Completamos el objeto!
        }
        return mascota;
    }
    
    /**
     * Lee todas las Mascotas.
     * Carga también sus Microchips asociados.
     */

  @Override
    public List<Mascota> leerTodos() throws Exception {
        List<Mascota> mascotas = mascotaDao.leerTodos();
        
        // Cargamos los chips para cada mascota
       
        for (Mascota m : mascotas) {
            Microchip chip = microchipDao.leerPorMascotaId(m.getId());
            m.setMicrochip(chip);
        }
        return mascotas;
    }
    
    /**
     * Orquesta la actualización de una Mascota y su Microchip
     * dentro de una transacción segura.
     */
    @Override
    public void actualizar(Mascota mascota) throws Exception {
        validateMascota(mascota);

        if (mascota.getId() == null || mascota.getId() <= 0)
            throw new IllegalArgumentException("El ID debe ser mayor a 0 para actualizar");

        // Empieza la transaccion
        
        TransactionManager txManager = null;
        try {
            txManager = new TransactionManager();
            Connection conn = txManager.getConnection();
            txManager.startTransaction();

            // --- INICIO DE LA TRANSACCIÓN ---
            
            // Paso A: Actualizar Mascota
            mascotaDao.actualizar(mascota, conn);
            
            // Paso B: Actualizar Microchip (si tiene uno)
            Microchip chip = mascota.getMicrochip();
            if (chip != null) {
                if (chip.getId() == null) { // Es un chip nuevo
                    microchipDao.crear(chip, mascota.getId(), conn);
                } else { // Es un chip existente
                    microchipDao.actualizar(chip, conn);
                }
            }
            
            // --- FIN DE LA TRANSACCIÓN ---
            txManager.commit();

        } catch (Exception e) {
            System.err.println("Error en Service: " + e.getMessage());
            if (txManager != null) txManager.rollback();
            throw e;
        } finally {
            if (txManager != null) txManager.close();
        }
    }
    /**
     * Orquesta la eliminación (lógica) de una Mascota
     * dentro de una transacción segura.
     */
    
    @Override
    public void eliminar(Long id) throws Exception {
        if (id <= 0)
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        
        // Se añadira tambien para eliminación de Chip 
       TransactionManager txManager = null;
        try {
            txManager = new TransactionManager();
            Connection conn = txManager.getConnection();
            txManager.startTransaction();

            // --- INICIO DE LA TRANSACCIÓN ---
           
            // Eliminación microchip
            Microchip chip = microchipDao.leerPorMascotaId(id);
            if (chip != null) {
             microchipDao.eliminar(chip.getId(), conn);
            }

            // Eliminación la mascota
            mascotaDao.eliminar(id, conn);
            
            // --- FIN DE LA TRANSACCIÓN ---
            txManager.commit();

        } catch (Exception e) {
            System.err.println("Error en Service: " + e.getMessage());
            if (txManager != null) txManager.rollback();
            throw e;
        } finally {
            if (txManager != null) txManager.close();
        }
       
    
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
     
     // --- MÉTODOS DE BÚSQUEDA ADICIONALES ---

    /**
     * Busca mascotas por nombre (coincidencia parcial).
     */
    public List<Mascota> buscarPorNombre(String nombre) throws Exception {
        return mascotaDao.buscarPorNombre(nombre);
    }

    /**
     * Busca mascotas por dueño (coincidencia parcial).
     */
    public List<Mascota> buscarPorDuenio(String duenio) throws Exception {
        return mascotaDao.buscarPorDuenio(duenio);
    }

    /**
     * Busca una mascota por el código de su microchip.
     */
    public Mascota buscarPorCodigoMicrochip(String codigo) throws Exception {
        return mascotaDao.buscarPorCodigoMicrochip(codigo);
    }
}

