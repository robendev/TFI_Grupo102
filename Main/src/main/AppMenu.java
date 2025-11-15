/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import dao.MascotaDao;
import java.util.Scanner;
import entities.Base;
import entities.Mascota;

import service.MascotaServiceImpl;
import service.MicrochipServiceImpl;
import dao.MascotaDao;
import dao.MicrochipDao;
import entities.Microchip;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.util.Scanner;
import java.util.List;


/**
 *
 * @author iRb18
 */
public class AppMenu {
private static void menu() {
        System.out.println("----------------------------------------");
        System.out.println("             Registro de Mascotas             ");
        System.out.println("----------------------------------------");
        System.out.println("1. ➕ Crear Mascota");
        System.out.println("2. 📋 Ver todas las Mascotas");
        System.out.println("3. 🔍 Ver Mascota por ID"); // Mejor nombre ? Join con nombre de due;o ? 
        System.out.println("4. ✏️  Actualizar Mascota");
        System.out.println("5. 🗑️  Eliminar Mascota (por ID)");
        System.out.println("0. 🚪 Salir");
        System.out.print("Ingrese una opción: ");
    }

        
 
     public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         // Instanciar modulos 
       
    MascotaServiceImpl mascotaService = new MascotaServiceImpl(new MascotaDao());
    MicrochipServiceImpl microchipService = new MicrochipServiceImpl(new MicrochipDao);
        
          int opcion = -1;
        do {
        try {
            // Llama a la función que muestra el menú
            menu(); 
            
            // El usuario elije la opcion ( numero entero)
            String input = sc.nextLine(); 
            opcion = Integer.parseInt(input);
            
            // --- Lógica del Menú ---
            switch(opcion){
                
              
         case 1 -> { // Opcion 1 crear mascota
    try {
        System.out.println("\n--- Creando Nueva Mascota ---");
        Mascota nuevaMascota = new Mascota();
        
        // 1. Captura de atributos String
        System.out.print("Ingrese el nombre: ");
        nuevaMascota.setNombre(sc.nextLine());
        
        System.out.print("Ingrese la especie: ");
        nuevaMascota.setEspecie(sc.nextLine()); 
        
        System.out.print("Ingrese la raza: ");
        nuevaMascota.setRaza(sc.nextLine());
        
        System.out.print("Ingrese el nombre del dueño: ");
        nuevaMascota.setDuenio(sc.nextLine());
        
        // 2. Captura de fecha (LocalDate) y manejo de formato
        LocalDate fechaNacimiento = null;
        boolean fechaValida = false;
        while (!fechaValida) {
            System.out.print("Ingrese la fecha de nacimiento (YYYY-MM-DD): ");
            try {
                fechaNacimiento = LocalDate.parse(sc.nextLine());
                nuevaMascota.setFechaNacimiento(fechaNacimiento);
                fechaValida = true;
            } catch (DateTimeParseException e) {
                System.err.println("❌ Formato de fecha incorrecto. Use el formato YYYY-MM-DD.");
            }
        }
        
        // 3. Captura del Microchip
        System.out.println("\n--- Datos del Microchip ---");
        System.out.print("Ingrese el código del microchip: ");
        String codigoMicrochip = sc.nextLine();
        
        // Asumiendo que existe una clase Microchip que acepta un código
        Microchip microchip = new Microchip();
        microchip.setCodigo(codigoMicrochip); 
        
        nuevaMascota.setMicrochip(microchip);
        
        // 4. Llama al servicio para guardar
        Mascota mascotaGuardada = mascotaService.save(nuevaMascota);
        System.out.println("✅ Mascota guardada exitosamente con ID: " + mascotaGuardada.getId());
        
    } catch (Exception e) {
        System.err.println("❌ Error al intentar crear la mascota: " + e.getMessage());
    }
}
                case 2 -> { // 📋 VER TODAS LAS MASCOTAS
                    try {
                        List<Mascota> mascotas = mascotaService.findAll();
                        if (mascotas.isEmpty()) {
                            System.out.println("📝 No hay mascotas registradas.");
                        } else {
                            System.out.println("\n--- Lista de Mascotas registradas ---");
                            for (Mascota m : mascotas) {
                                System.out.println("ID: " + m.getId() + ", Nombre: " + m.getNombre() + ", Especie: " + m.getEspecie());
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("❌ Error al listar las mascotas: " + e.getMessage());
                    }
                }
                
                case 3 -> { // 🔍 BUSCAR POR ID
                    try {
                        System.out.print("Ingrese el ID de la mascota a buscar: ");
                        Long id = Long.parseLong(sc.nextLine());
                        Mascota mascotaEncontrada = mascotaService.findById(id);
                        
                        if (mascotaEncontrada != null) {
                            System.out.println("✅ Mascota encontrada: ID: " + mascotaEncontrada.getId() + ", Nombre: " + mascotaEncontrada.getNombre() + ", Especie: " + mascotaEncontrada.getEspecie());
                        } else {
                            System.out.println("⚠️ Mascota con ID " + id + " no encontrada.");
                        }
                    } catch (NumberFormatException e) {
                         System.err.println("❌ Error de formato: El ID debe ser un número entero.");
                    } catch (Exception e) {
                        System.err.println("❌ Error al buscar la mascota: " + e.getMessage());
                    }
                }
                
                case 4 -> { // ✏️ ACTUALIZAR MASCOTA
                    try {
                        System.out.print("Ingrese el ID de la mascota a actualizar: ");
                        Long id = Long.parseLong(sc.nextLine());
                        
                        Mascota mascotaExistente = mascotaService.findById(id);
                        
                        if (mascotaExistente != null) {
                            System.out.println("Mascota actual: Nombre: " + mascotaExistente.getNombre());
                            System.out.print("Ingrese el nuevo nombre (o presione Enter para mantener el actual: " + mascotaExistente.getNombre() + "): ");
                            String nuevoNombre = sc.nextLine();
                            
                            if (!nuevoNombre.trim().isEmpty()) {
                                mascotaExistente.setNombre(nuevoNombre);
                            }
                            
                            // Llama al servicio para actualizar
                            Mascota mascotaActualizada = mascotaService.update(id, mascotaExistente);
                            System.out.println("✅ Mascota actualizada. Nuevo Nombre: " + mascotaActualizada.getNombre());
                            
                        } else {
                            System.out.println("⚠️ Mascota con ID " + id + " no encontrada. No se pudo actualizar.");
                        }

                    } catch (NumberFormatException e) {
                         System.err.println("❌ Error de formato: El ID debe ser un número entero.");
                    } catch (Exception e) {
                        System.err.println("❌ Error al actualizar la mascota: " + e.getMessage());
                    }
                }
                
                case 5 -> { // 🗑️ ELIMINAR MASCOTA
                    try {
                        System.out.print("Ingrese el ID de la mascota a eliminar: ");
                        Long id = Long.parseLong(sc.nextLine());
                        
                        mascotaService.delete(id);
                        System.out.println("✅ Mascota con ID " + id + " eliminada exitosamente.");
                        
                    } catch (NumberFormatException e) {
                         System.err.println("❌ Error de formato: El ID debe ser un número entero.");
                    } catch (Exception e) {
                        System.err.println("❌ Error al eliminar la mascota: " + e.getMessage());
                    }
                }
                
                case 0 -> { // 🚪 SALIR
                    System.out.println("👋 Saliendo del Sistema de Gestión Zootécnica...");
                }
                
                default -> {
                    System.out.println("⚠️ Opción no válida. Por favor, ingrese un número del 0 al 5.");
                }
            }
            
            // Pausar para que el usuario pueda ver el resultado antes de que aparezca el menú de nuevo
            if (opcion != 0) {
                System.out.println("\nPresione Enter para continuar...");
                sc.nextLine(); 
            }

        } catch (NumberFormatException e) {
            // Captura el error de entrada si el usuario no ingresa un número para la opción principal
            System.err.println("❌ Entrada no válida para el menú. Por favor, ingrese un número.");
            opcion = -1; // Asegura que el ciclo continúe
        } catch (Exception e) {
            System.err.println("❌ Ocurrió un error inesperado en el menú: " + e.getMessage());
            opcion = -1;
        }
    } while (opcion != 0);
    
    sc.close();
}
}
