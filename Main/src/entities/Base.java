/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author Victormanuel.bar
 */
// Este atributo Base es para heredar id y eliminados para todas las clases presentes del dominio, que son atributos comunes
public abstract class Base {
    private Long id; // Identificado unico 
    private Boolean eliminado; // Marca en nuestra base de datos un elemento fue eliminado
    
    // Se realiza el constructor sobrecargado
    public Base(Long id, Boolean eliminado){
        this.id = id;
        this.eliminado = eliminado;
       
    }
    
    public Base (){}  // constructor vacio
    
    
    // Se hace el getters y setters 
    public Long getId(){
        return id;
    }
    
    public void setId (Long id){
        this.id = id;
    }
    
    // Confirmación si esta eliminado el dato en la BD
    public Boolean getEliminado(){
        return eliminado;
    }
    
    public void setEliminado (Boolean eliminado){
        this.eliminado = eliminado;
   }
    
}
    

