/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import dao.MascotaDao;
import java.util.Scanner;
import entities.Base;
import entities.Mascota;

import service.MascotaService;
import service.MicrochipService;
import dao.MascotaDao;
import dao.MicrochipDao;

import java.util.Scanner;
import java.util.List;


/**
 *
 * @author iRb18
 */
public class AppMenu {


        
public class AppMenu {
     public static void main(String[] args) {
         Scanner sc = new Scanner(System.in);
         
         MascotaService mascotaService = new MascotaService(new MascotaDao());
        // MicrochipService microchipService = new MicrochipService(new MicrochipDao(),mascota);
          int opcion = -1;
          do {
              try {
                  menu();
                  opcion = Integer.parseInt(sc.nextLine());
                  
                  switch(opcion){
                      case 1 -> {
                          try {
                              Mascota mascota = new Mascota();
                              System.out.print( "")
                          }
                      }
              }
          }
     }
}

}
