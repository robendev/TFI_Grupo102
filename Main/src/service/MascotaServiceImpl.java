/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.List;
import java.sql.Connection;
import java.sql.SQLException;

import config.TransactionManager;
import config.DatabaseConnection;
import dao.MascotaDAO;
import entities.Mascota;
import service.GenericService;

public class MascotaServiceImpl implements GenericService<Mascota> {

    private final MascotaDAO mascotaDAO;
    private final TransactionManager TransactionManager;

    public MascotaServiceImpl() {
        this.mascotaDAO = new MascotaDAO();
        this.TransactionManager = new TransactionManager();
    }

    @Override
    public void insertar(Mascota mascota) throws Exception {
        validarMascota(mascota);
    
        Connection conn= TransactionManager.getConnection();
        try {
            