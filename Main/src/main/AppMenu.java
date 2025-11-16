package main;

import dao.MascotaDao;
import dao.MicrochipDao;
import service.MascotaServiceImpl;
import service.MicrochipServiceImpl;

import java.util.Scanner;

public class AppMenu {

    private final Scanner scanner;
    private final MascotaServiceImpl mascotaService;
    private final MicrochipServiceImpl microchipService;
    private final MenuHandler handler;

    public AppMenu() {
        this.scanner = new Scanner(System.in);

        MascotaDao mascotaDao = new MascotaDao();
        MicrochipDao microchipDao = new MicrochipDao();

        this.microchipService = new MicrochipServiceImpl(microchipDao);
        this.mascotaService = new MascotaServiceImpl(mascotaDao, microchipDao);

        this.handler = new MenuHandler(scanner, mascotaService, microchipService);
    }

    public void run() {
        int opcion;
        do {
            MenuDisplay.mostrarMenuPrincipal();
            opcion = MenuDisplay.leerEntero(scanner, "Ingrese una opción: ");
            switch (opcion) {
                case 1 -> handler.menuMascotas();
                case 2 -> handler.menuMicrochips();
                case 0 -> System.out.println("Saliendo de la aplicación...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 0);
    }
}
