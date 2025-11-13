/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.time.LocalDate;
/**
 *
 * @author Federico
 */
public class Mascota {

    private Long id;
    private Boolean eliminado;
    private String nombre;
    private String especie;
    private String raza;
    private java.time.LocalDate fechaNacimiento;
    private String duenio;
    private Microchip microchip;

    public Mascota() {
    }
}