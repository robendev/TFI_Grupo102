package main;

import java.util.Scanner;

public class MenuDisplay {

    public static void mostrarMenuPrincipal() {
        System.out.println("\n==============================");
        System.out.println("   SISTEMA MASCOTAS - TFI");
        System.out.println("==============================");
        System.out.println("1) Gestión de Mascotas");
        System.out.println("2) Gestión de Microchips (Solo Lectura)");
        System.out.println("0) Salir");
        System.out.println("------------------------------");
    }
    // Mostras Menu de Mascotas
    public static void mostrarMenuMascotas() {
        System.out.println("\n-------- MENÚ MASCOTAS --------");
        System.out.println("1) Alta de Mascota (con Microchip)"); // Aclaramos
        System.out.println("2) Listar Mascotas");
        System.out.println("3) Buscar Mascota por ID");
        System.out.println("4) Modificar Mascota");
        System.out.println("5) Eliminar Mascota");
        System.out.println("--- Búsquedas Adicionales ---");
        System.out.println("6) Buscar por Nombre");
        System.out.println("7) Buscar por Dueño");
        System.out.println("8) Buscar por Código de Microchip");
        System.out.println("-------------------------------");
        System.out.println("0) Volver al menú principal");
        System.out.println("-------------------------------");
    }
    // Mostras Menu de Microchips (solo lectura) 
    public static void mostrarMenuMicrochips() {
        System.out.println("\n----- MENÚ MICROCHIPS (Lectura) -----");
        System.out.println("1) Listar Todos los Microchips");
        System.out.println("2) Buscar Microchip por ID");
        System.out.println("-----------------------------------");
        System.out.println("0) Volver al menú principal");
        System.out.println("-----------------------------------");
    }

    /**
     * Lee un Long de forma segura.
     * Reemplaza al 'leerEntero' donde necesitemos IDs.
     */
    public static long leerLong(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Long.parseLong(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero (Long).");
            }
        }
    }
    
    // Tu leerEntero original sigue siendo útil para opciones de menú
    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine();
            try {
                return Integer.parseInt(linea.trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero.");
            }
        }
    }

    public static void pausar(Scanner scanner) {
        System.out.println("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }
}
