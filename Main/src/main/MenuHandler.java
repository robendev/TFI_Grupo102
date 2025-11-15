/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
                case 0 -> { /* volver */ }
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }

    private void altaMascota() {
        try {
            System.out.println("\n--- Alta de Mascota ---");
            Mascota m = new Mascota();

            System.out.print("Nombre: ");
            m.setNombre(scanner.nextLine().trim());

            System.out.print("Especie: ");
            m.setEspecie(scanner.nextLine().trim());

            System.out.print("Raza (opcional): ");
            String raza = scanner.nextLine().trim();
            if (!raza.isEmpty()) {
                m.setRaza(raza);
            }

            m.setFechaNacimiento(leerFechaOpcional("Fecha de nacimiento (yyyy-MM-dd, vacío para omitir): "));

            System.out.print("Dueño: ");
            m.setDuenio(scanner.nextLine().trim());

            // Por ahora no pedimos microchip , se puede agegar
            m.setMicrochip(null);

            mascotaService.crear(m);
            System.out.println("Mascota creada correctamente.");
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
                    System.out.println(m);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar mascotas: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void buscarMascotaPorId() {
        try {
            int id = MenuDisplay.leerEntero(scanner, "Ingrese ID de la mascota: ");
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
            int id = MenuDisplay.leerEntero(scanner, "Ingrese ID de la mascota a modificar: ");
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
            if (!nombre.isEmpty()) {
                m.setNombre(nombre);
            }

            System.out.print("Especie (" + m.getEspecie() + "): ");
            String especie = scanner.nextLine().trim();
            if (!especie.isEmpty()) {
                m.setEspecie(especie);
            }

            System.out.print("Raza (" + (m.getRaza() != null ? m.getRaza() : "-") + "): ");
            String raza = scanner.nextLine().trim();
            if (!raza.isEmpty()) {
                m.setRaza(raza);
            }

            System.out.print("¿Modificar fecha de nacimiento? (s/n): ");
            String respFecha = scanner.nextLine().trim();
            if (respFecha.equalsIgnoreCase("s")) {
                LocalDate nuevaFecha = leerFechaOpcional("Nueva fecha (yyyy-MM-dd, vacío para dejar sin fecha): ");
                m.setFechaNacimiento(nuevaFecha);
            }

            System.out.print("Dueño (" + m.getDuenio() + "): ");
            String duenio = scanner.nextLine().trim();
            if (!duenio.isEmpty()) {
                m.setDuenio(duenio);
            }

            mascotaService.actualizar(m);
            System.out.println("Mascota actualizada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al actualizar la mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private void eliminarMascota() {
        try {
            int id = MenuDisplay.leerEntero(scanner, "Ingrese ID de la mascota a eliminar: ");
            mascotaService.eliminar(id);
            System.out.println("Mascota eliminada (baja lógica) correctamente.");
        } catch (Exception e) {
            System.out.println("Error al eliminar la mascota: " + e.getMessage());
        }
        MenuDisplay.pausar(scanner);
    }

    private LocalDate leerFechaOpcional(String mensaje) {
        System.out.print(mensaje);
        String linea = scanner.nextLine().trim();
        if (linea.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(linea);
        } catch (DateTimeParseException e) {
            System.out.println("Fecha inválida. Se dejará sin fecha.");
            return null;
        }
    }
}
