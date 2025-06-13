package presentation.controller;

import domain.valueObject.DocumentoRut;
import presentation.services.UsuarioApplicationService;

import java.util.Scanner;

public class UsuarioTerminalController {
    private final UsuarioApplicationService usuarioApplicationService;
    private final Scanner scanner = new Scanner(System.in);

    public UsuarioTerminalController(UsuarioApplicationService usuarioApplicationService) {
        this.usuarioApplicationService = usuarioApplicationService;
    }

    public void handleRegistrarUsuario() {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Ingrese el rut: ");
        String rutInput = scanner.nextLine();

        DocumentoRut rutInstance = DocumentoRut.definir(rutInput);

        usuarioApplicationService.registrarUsuario(nombre, rutInstance);
    }
}
