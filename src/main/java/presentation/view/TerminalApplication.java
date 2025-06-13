package presentation.view;

import presentation.controller.LibrosTerminalController;
import presentation.controller.UsuarioTerminalController;
import shared.exceptions.UsuarioException;
import shared.exceptions.UsuarioYaRegistradoException;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TerminalApplication {
    private final LibrosTerminalController librosTerminalController;
    private final UsuarioTerminalController usuarioTerminalController;
    private final Scanner scanner = new Scanner(System.in);

    public TerminalApplication(
        LibrosTerminalController librosTerminalController,
        UsuarioTerminalController usuarioTerminalController
    ) {
        this.librosTerminalController = librosTerminalController;
        this.usuarioTerminalController = usuarioTerminalController;
    }

    public void start() {
        mostrarBanner();

        boolean running = true;
        while (running) {
            mostrarMenu();

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    try {
                        this.usuarioTerminalController.handleRegistrarUsuario();
                    } catch (InputMismatchException e) {
                        System.out.println("Input inválido");
                    } catch (UsuarioYaRegistradoException e) {
                        System.out.println("Usuario ya registrado");
                    }
                    break;
                default:
                    running = false;
            }
        }
    }

    private void mostrarBanner() {
        System.out.println("=== Sistema de biblioteca ===");
    }

    private void mostrarMenu() {
        System.out.println("1. Registrar usuario");
        System.out.println("2. Agregar libro");
        System.out.println("3. Buscar libro");
        System.out.println("4. Prestar libro");
        System.out.println("5. Devolver libro");
    }
}
