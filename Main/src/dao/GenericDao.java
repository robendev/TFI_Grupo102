
package dao;

import java.util.List;
import java.sql.Connection; // Se habilita la conección a la BD 

public interface GenericDao<T> {

    // --- Metodos para Transacciones --- 
    
    // Crea una entidad nueva 
    void crear(T entidad, Connection conn) throws Exception;
    
    
    // Actualiza una entidad 
    void actualizar(T entidad, Connection conn) throws Exception;
    
    // Elimina logicamente un entidad
    
    void eliminar(Long id, Connection conn) throws Exception;   // ID genérico
    
    //--- Metodos de Lectura ----- 
    // Lee la entiedad por su ID 
    T leer(Long id) throws Exception;
    
    // Lista todas las entiedades activas (no eliminadas) 
    List<T> leerTodos() throws Exception;

}
