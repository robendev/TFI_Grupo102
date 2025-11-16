/*
 * Interfaz genérica de servicios de negocio.
 * Define las operaciones CRUD básicas que debe ofrecer cualquier Service.
 */
package service;

import java.util.List;

public interface GenericService<T> {
// insterta una nueva entidad en el sistema
    void crear(T entidad) throws Exception;
    
//Identificador de la entidad por su ID
    T leer(Long id) throws Exception;
    
//Retorna todas las entidades del sistema
    List<T> leerTodos() throws Exception;
    
//Actualiza una entidad existente
    void actualizar(T entidad) throws Exception;
    
//Elimina una entidad por su ID
    void eliminar(Long id) throws Exception;
}
