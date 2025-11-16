package main;

import entities.Mascota;
import entities.Microchip;
import service.MascotaServiceImpl;
import service.MicrochipServiceImpl; 

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuHandler {

    private final Scanner scanner;
    private final MascotaServiceImpl mascotaService;
    private final MicrochipServiceImpl microchipService;

    public MenuHandler(Scanner scanner,
                       MascotaServiceImpl mascotaService,
                       MicrochipServiceImpl microchipService) {
        this.scanner = scanner;
        this.mascotaService = mascotaService;
        this.microchipService = microchipService;
    }

    //MENU MASCOTAS
    
    public void menuMascotas() {
        int opcion;
        do {
            MenuDisplay.mostrarMenuMascotas();
            opcion = MenuDisplay.leerEntero(scanner, "Ingrese una opción: ");
            switch (opcion) {
                case 1 -> altaMascota();
                case 2 -> listarMascotas();
                case 3 -> buscarMascotaPorId();
                case 4 -> modificarMascota();
                case 5 -> eliminarMascota();
                // --- Nuevas Búsquedas ---
                case 6 -> buscarMascotaPorNombre();
                case 7 -> buscarMascotaPorDuenio();
                case 8 -> buscarMascotaPorCodigoChip();
                case 0 -> { /* volver */ }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
    /**Se pide los datos de la Mascota y su Microchip
     * para enviarlos a la transacción del Service.
     */

 private void altaMascota() {
        try {
            System.out.println("\n--- Alta de Mascota (con Microchip) ---");
            System.out.println("Paso 1: Datos de la Mascota");
            Mascota m = new Mascota();

            System.out.print("Nombre: ");
            m.setNombre(scanner.nextLine().trim());
            System.out.print("Especie: ");
            m.setEspecie(scanner.nextLine().trim());
            System.out.print("Raza (opcional): ");
            String raza = scanner.nextLine().trim();
            if (!raza.isEmpty()) m.setRaza(raza);
            m.setFechaNacimiento(leerFechaOpcional("Fecha de nacimiento (yyyy-MM-dd, opcional): "));
            System.out.print("Dueño: ");
            m.setDuenio(scanner.nextLine().trim());

            System.out.println("\nPaso 2: Datos del Microchip (Obligatorio)");
            Microchip chip = new Microchip();
            
            // Bucle para validar el código del chip
            while (true) {
                System.out.print("Código del Microchip: ");
                String codigo = scanner.nextLine().trim();
                try {
                    // Usamos el validador del MicrochipService
                    microchipService.validateCodigoUnico(codigo, null);
                    chip.setCodigo(codigo); // Si no lanza excepción, el código es válido
                    break; 
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    System.out.println("Por favor, ingrese un código diferente.");
                }
            }
            
            System.out.print("Veterinaria (opcional): ");
            String vet = scanner.nextLine().trim();
            if (!vet.isEmpty()) chip.setVeterinaria(vet);
            chip.setFechaImplantacion(leerFechaOpcional("Fecha implantación (yyyy-MM-dd, opcional): "));

            // ¡Enlazamos el chip a la mascota (A -> B)!
            m.setMicrochip(chip);

            // Llamamos al Service para la transacción
            mascotaService.crear(m);
            System.out.println("¡Mascota y Microchip creados correctamente!");
        } catch (Exception e) {
            System.out.println("Error al crear la mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

   private void listarMascotas() {
        try {
            System.out.println("\n--- Listado de Mascotas ---");
            List<Mascota> lista = mascotaService.leerTodos(); 
            if (lista == null || lista.isEmpty()) {
                System.out.println("No hay mascotas registradas.");
            } else {
                for (Mascota m : lista) {
                    System.out.println(m); // El toString() de Mascota mostrará el chip
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar mascotas: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void buscarMascotaPorId() {
        try {
            long id = MenuDisplay.leerLong(scanner, "Ingrese ID de la mascota: ");
            Mascota m = mascotaService.leer(id); 
            if (m == null) {
                System.out.println("No se encontró una mascota con ID " + id);
            } else {
                System.out.println("Mascota encontrada:");
                System.out.println(m);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void modificarMascota() {
        try {
            
            long id = MenuDisplay.leerLong(scanner, "Ingrese ID de la mascota a modificar: ");
            Mascota m = mascotaService.leer(id); 
            if (m == null) {
                System.out.println("No se encontró una mascota con ID " + id);
                MenuDisplay.pausar(scanner);
                return;
            }

            System.out.println("Mascota actual:");
            System.out.println(m);
            System.out.println("\nDeje el campo vacío para mantener el valor actual.");

            System.out.print("Nombre (" + m.getNombre() + "): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) m.setNombre(nombre);

            System.out.print("Especie (" + m.getEspecie() + "): ");
            String especie = scanner.nextLine().trim();
            if (!especie.isEmpty()) m.setEspecie(especie);

            System.out.print("Raza (" + (m.getRaza() != null ? m.getRaza() : "-") + "): ");
            String raza = scanner.nextLine().trim();
            if (!raza.isEmpty()) m.setRaza(raza);

            System.out.print("¿Modificar fecha de nacimiento? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                m.setFechaNacimiento(leerFechaOpcional("Nueva fecha (yyyy-MM-dd, vacío para null): "));
            }

            System.out.print("Dueño (" + m.getDuenio() + "): ");
            String duenio = scanner.nextLine().trim();
            if (!duenio.isEmpty()) m.setDuenio(duenio);
          

            mascotaService.actualizar(m);
            System.out.println("Mascota actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar la mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void eliminarMascota() {
        try {

            long id = MenuDisplay.leerLong(scanner, "Ingrese ID de la mascota a eliminar: ");
            mascotaService.eliminar(id); // El Service espera Long
            System.out.println("Mascota y microchip asociado (baja lógica) eliminados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar la mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private LocalDate leerFechaOpcional(String mensaje) {
        while(true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(linea); // Formato YYYY-MM-DD
            } catch (DateTimeParseException e) {
                System.out.println("Fecha inválida. Formato debe ser YYYY-MM-DD. Intente de nuevo.");
            }
        }
    }
    
    // --- NUEVOS MÉTODOS DE BÚSQUEDA ---

    private void buscarMascotaPorNombre() {
        try {
            System.out.print("Ingrese nombre (o parte) a buscar: ");
            String nombre = scanner.nextLine().trim();
            List<Mascota> lista = mascotaService.buscarPorNombre(nombre);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron mascotas con ese nombre.");
            } else {
                System.out.println("Mascotas encontradas:");
                lista.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void buscarMascotaPorDuenio() {
        try {
            System.out.print("Ingrese dueño (o parte) a buscar: ");
            String duenio = scanner.nextLine().trim();
            List<Mascota> lista = mascotaService.buscarPorDuenio(duenio);
            if (lista.isEmpty()) {
                System.out.println("No se encontraron mascotas con ese dueño.");
            } else {
                System.out.println("Mascotas encontradas:");
                lista.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void buscarMascotaPorCodigoChip() {
        try {
            System.out.print("Ingrese código EXACTO del microchip a buscar: ");
            String codigo = scanner.nextLine().trim();
            Mascota m = mascotaService.buscarPorCodigoMicrochip(codigo);
            if (m == null) {
                System.out.println("No se encontró ninguna mascota con ese código de microchip.");
            } else {
                System.out.println("Mascota encontrada:");
                System.out.println(m);
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }
}
