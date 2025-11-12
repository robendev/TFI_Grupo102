/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public abstract class Base {
    private int id; // Identificado unico 
    private Boolean eliminado; // Marca en nuestra base de datos un elemento fue eliminado
    
    // Se realiza el constructor sobrecargado
    public Base(int id, Boolean eliminado){
        this.id = id;
        this.eliminado = eliminado;
       
    }
    
    public Base (){}  // constructor vacio
    
    
    // Se hace el getters y setters 
    public int getId(){
        return id;
    }
    
    public void setId (int id){
        this.id = id;
    }
    
    // Confirmación si esta eliminado el dato en la BD
    public Boolean isEliminado(){
        return eliminado;
    }
    
    public void setEliminado (Boolean eliminado){
        this.eliminado = eliminado ;
    } 
    
}
