package presentation.controller;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import presentation.services.UsuarioApplicationService;

import java.util.ArrayList;
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

    /**
     * Método que maneja el caso de mostrar usuarios registrados
     */
    public void handleMostrarUsuarios() {
        ArrayList<Usuario> usuarios = this.usuarioApplicationService.obtenerTodosLosUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados en el sistema.");
            return;
        }

        // Ordenar los usuarios por nombre
        usuarios.sort((u1, u2) -> u1.getNombre().compareTo(u2.getNombre()));

        System.out.println("\n=== Usuarios registrados (ordenados por nombre) ===");
        for (Usuario usuario : usuarios) {
            System.out.println("RUT: " + usuario.getRut().getFormateado() + " - Nombre: " + usuario.getNombre());
        }
    }
}
