/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.Scanner;

public class MenuDisplay {

    public static void mostrarMenuPrincipal() {
        System.out.println("\n==============================");
        System.out.println("   SISTEMA MASCOTAS - TFI");
        System.out.println("==============================");
        System.out.println("1) Gestión de Mascotas");
        // posible gestion de microchips en opción 2
        System.out.println("0) Salir");
        System.out.println("------------------------------");
    }

    public static void mostrarMenuMascotas() {
        System.out.println("\n-------- MENÚ MASCOTAS --------");
        System.out.println("1) Alta de Mascota");
        System.out.println("2) Listar Mascotas");
        System.out.println("3) Buscar Mascota por ID");
        System.out.println("4) Modificar Mascota");
        System.out.println("5) Eliminar Mascota");
        System.out.println("0) Volver al menú principal");
        System.out.println("-------------------------------");
    }

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
