/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;

public interface GenericDao<T> {

    void crear(T entidad) throws Exception;

    T leer(int id) throws Exception;

    List<T> leerTodos() throws Exception;

    void actualizar(T entidad) throws Exception;

    void eliminar(int id) throws Exception;   // ID genérico

}
