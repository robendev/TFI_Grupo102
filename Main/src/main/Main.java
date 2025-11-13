/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import entities.Mascota;
import entities.Microchip;
import java.time.LocalDate;
        
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
    System.out.println("--- Probando entidades ---");

        // 1. Creamos el objeto Microchip
        Microchip miChip = new Microchip(
                100L,                           // id
                "CHIP-A1B2C3",                  // codigo
                LocalDate.of(2023, 5, 10),      // fechaImplantacion
                "Veterinaria 'Patitas Sanas'",  // veterinaria
                "Implantado en lomo"            // observaciones
        );
        
        // 2. Creamos el objeto Mascota y le "enlazamos" el chip
        Mascota miMascota = new Mascota(
                1L,                             // id                          // eliminado
                "Bobby",                        // nombre
                "Canino",                       // especie
                "Golden Retriever",             // raza
                LocalDate.of(2022, 1, 15),      // fechaNacimiento
                "Ana Gomez",                    // duenio
                miChip                          // Enlaza con Microchip
        );

        // 3. Imprimimos el toString() de la mascota y de Microchips
        System.out.println("--- toString() de Mascota: ---");
        System.out.println(miMascota);
        
        System.out.println("--- toString() de Microchip: ---");
        System.out.println(miChip);

        // 4. Probamos "navegar" la relación A -> B
        System.out.println("\n--- Navegando la relación: ---");
        System.out.println("Nombre de la mascota: " + miMascota.getNombre());
        System.out.println("Código de su chip: " + miMascota.getMicrochip().getCodigo());
        System.out.println("Veterinaria del chip: " + miMascota.getMicrochip().getVeterinaria());
    }
}
