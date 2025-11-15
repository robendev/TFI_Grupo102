
package main;

import config.DatabaseConnection;
import dao.MascotaDao;
import dao.MicrochipDao;
import entities.Mascota;
import entities.Microchip;

import java.sql.Connection;
import java.time.LocalDate;

public class PruabaDAO {


    public static void main(String[] args) {
        // 1. Instanciamos los DAOs que queremos probar
        MascotaDao mascotaDao = new MascotaDao();
        MicrochipDao microchipDao = new MicrochipDao();

        // 2. Preparamos los objetos que queremos guardar
        Microchip chipPrueba = new Microchip();
        chipPrueba.setCodigo("PRUEBA-DAO-001");
        chipPrueba.setFechaImplantacion(LocalDate.now());
        chipPrueba.setVeterinaria("Clínica de Pruebas");
        
        Mascota mascotaPrueba = new Mascota();
        mascotaPrueba.setNombre("Mascota de Prueba");
        mascotaPrueba.setEspecie("Canino");
        mascotaPrueba.setDuenio("Equipo de Desarrollo");
        mascotaPrueba.setFechaNacimiento(LocalDate.of(2024, 1, 1));
        
        // ¡Enlazamos el chip (A -> B)!
        mascotaPrueba.setMicrochip(chipPrueba);

        
        // --- 3. ¡LA PRUEBA DE TRANSACCIÓN! ---
        // Esto es lo que hará tu 'Service' (y tu 'TransactionManager')
        
        Connection conn = null;
        try {
            // Obtenemos la conexión
            conn = DatabaseConnection.getConnection();
            
            // ¡Clave! Desactivamos el auto-commit para manejar la transacción
            conn.setAutoCommit(false);

            System.out.println("Iniciando transacción...");

            // --- INICIO DE OPERACIONES ---

            // A. Creamos la Mascota (A)
            System.out.println("Intentando crear Mascota...");
            mascotaDao.crear(mascotaPrueba, conn); // Le pasamos la conexión
            System.out.println("...Mascota creada con ID: " + mascotaPrueba.getId());

            // B. Creamos el Microchip (B)
            System.out.println("Intentando crear Microchip...");
            microchipDao.crear(chipPrueba, mascotaPrueba.getId(), conn); // Le pasamos la MISMA conexión
            System.out.println("...Microchip creado con ID: " + chipPrueba.getId());

            // --- FIN DE OPERACIONES ---

            // C. Si todo salió bien, confirmamos la transacción
            System.out.println("Haciendo COMMIT...");
            conn.commit();
            System.out.println("✅ ¡ÉXITO! Transacción confirmada. DAO funciona.");

        } catch (Exception e) {
            // D. Si algo falló (ej. SQL mal, código duplicado, etc.)
            System.err.println("❌ ¡FALLO! Algo salió mal: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Haciendo ROLLBACK...");
            try {
                if (conn != null) {
                    conn.rollback(); // Deshacemos TODOS los cambios
                }
            } catch (Exception sqlEx) {
                System.err.println("Error al hacer rollback: " + sqlEx.getMessage());
            }

        } finally {
            // E. Pase lo que pase, cerramos la conexión
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Devolvemos la conexión a su estado normal
                    conn.close();
                }
            } catch (Exception finalEx) {
                System.err.println("Error al cerrar conexión: " + finalEx.getMessage());
            }
        }
    }
    }
    

