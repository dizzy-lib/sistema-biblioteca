package presentation.controller;

import domain.valueObject.DocumentoRut;
import presentation.services.UsuarioApplicationService;

import java.util.Scanner;

/**
 * Clase controladora que se encarga de manejar las entradas de los casos
 * de uso realizados por terminal de acciones relacionadas al usuario
 */
public class UsuarioTerminalController {
    private final UsuarioApplicationService usuarioApplicationService;
    private final Scanner scanner = new Scanner(System.in);

    public UsuarioTerminalController(UsuarioApplicationService usuarioApplicationService) {
        this.usuarioApplicationService = usuarioApplicationService;
    }

    /**
     * Método que maneja el caso de registro de usuarios
     */
    public void handleRegistrarUsuario() {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el rut: ");
        String rutInput = scanner.nextLine();

        DocumentoRut rutInstance = DocumentoRut.definir(rutInput);

        usuarioApplicationService.registrarUsuario(nombre, rutInstance);
    }
}
