/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.time.LocalDate;

public class Microchip extends Base {
    private String codigo;
    private LocalDate fechaImplantacion;
    private String veterinaria;
    private String observaciones;

    public Microchip() {}
    
    public Microchip(Long id, Boolean eliminado, String codigo, LocalDate fechaImplantacion, String veterinaria, String observaciones) {
        super(id, eliminado);
        this.codigo = codigo;
        this.fechaImplantacion = fechaImplantacion;
        this.veterinaria = veterinaria;
        this.observaciones = observaciones;
    }
    
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaImplantacion() {
        return fechaImplantacion;
    }
    public void setFechaImplantacion(LocalDate fechaImplantacion) {
        this.fechaImplantacion = fechaImplantacion;
    }

    public String getVeterinaria() {
        return veterinaria;
    }
    public void setVeterinaria(String veterinaria) {
        this.veterinaria = veterinaria;
    }

    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Microchip{" +
                "id=" + super.getId() +
                ", eliminado=" + super.getEliminado() +
                ", codigo='" + codigo + '\'' +
                ", fechaImplantacion=" + fechaImplantacion +
                ", veterinaria='" + veterinaria + '\'' +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}

