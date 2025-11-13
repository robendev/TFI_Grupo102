/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.time.LocalDate;

/**
 *
 * @author Federico
 * @editor Victormanuel.bar
 */

public class Mascota extends Base {

    private String nombre;
    private String especie;
    private String raza;
    private java.time.LocalDate fechaNacimiento;
    private String duenio;
    private Microchip microchip;

    public Mascota() {
    }
    // Se realiza el constructor sobrecargado  
    
    public Mascota (Long id, Boolean eliminado, String nombre, String especie,String raza, LocalDate fechaNacimiento, String duenio, Microchip microchip){
        super(id, eliminado);
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this. fechaNacimiento = fechaNacimiento; 
        this.duenio = duenio;
        this.microchip = microchip;
    
    }
    // Sealizan todos los getters y setters de todos los atributos
    
    public String getNombre() {
     return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getEspecie() {
     return especie;
    }
    
    public void setEspecie(String especie){
        this.especie = especie;
    }    
    
    public String getRaza() {
     return raza;
    }
    
    public void setRaza(String raza){
        this.raza = raza;
    }      
    
     public java.time.LocalDate getFechaNacimiento() {
     return fechaNacimiento;
    }
    
    public void setFechaNacimiento (java.time.LocalDate fechaNacimiento){
        this.fechaNacimiento = fechaNacimiento;
    }   
  
    public String getDuenio() {
     return duenio;
    }
    
    public void setDuenio(String duenio){
        this.duenio = duenio;
    }
    
    public Microchip getMicrochip() {
        return microchip;
    }
    
    public void setMicrochip(Microchip microchip){
        this.microchip = microchip;
    }
    
   @Override
   public String toString(){
       return "Mascota{" +
               "id=" + super.getId() + 
               ", eliminado'" + super.getEliminado() + 
               ", nombre='" + nombre + '\'' + 
               ", especie='" + especie + '\'' +
               ", raza='" + raza + '\'' + 
               ", duenio='" + duenio +'\'' +
               ", microchip=" + (microchip != null ? microchip.getCodigo() : "Sin Chip") + 
               '}';        
   }
    
}
