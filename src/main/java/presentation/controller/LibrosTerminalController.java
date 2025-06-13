package presentation.controller;

import domain.entities.Libro;
import domain.valueObject.DocumentoRut;
import presentation.services.BibliotecaApplicationService;

import java.util.ArrayList;
import java.util.Scanner;

public class LibrosTerminalController {
    private final BibliotecaApplicationService bibliotecaApplicationService;
    private final Scanner scanner = new Scanner(System.in);

    public LibrosTerminalController(BibliotecaApplicationService bibliotecaApplicationService) {
        this.bibliotecaApplicationService = bibliotecaApplicationService;
    }

    public void handleAgregarLibro() {
        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();

        System.out.print("Autor del libro: ");
        String autorLibro = scanner.nextLine();

        System.out.print("Genero: ");
        String generoLibro = scanner.nextLine();

        System.out.print("Editorial: ");
        String editorialLibro = scanner.nextLine();

        this.bibliotecaApplicationService.agrearLibro(
                nombreLibro, autorLibro, generoLibro, editorialLibro
        );
    }

    public void handleBuscarLibro() {
        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();

        this.bibliotecaApplicationService.buscarLibro(nombreLibro);
    }

    public void handlePrestarLibro() {
        System.out.print("Ingrese el rut del usuario: ");
        String rutUsuarioInput = scanner.nextLine();

        DocumentoRut rutUsuario = DocumentoRut.definir(rutUsuarioInput);

        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();

        // Buscar el libro
        ArrayList<Libro> libros = this.bibliotecaApplicationService.buscarLibro(nombreLibro);

        for (Libro libro : libros) {
            System.out.println(libro.getUuid() + " - " + libro.getTitulo());
        }

        System.out.print("Ingrese el id del libro a reservar: ");
        String idLibro = scanner.nextLine();

        this.bibliotecaApplicationService.prestarLibro(idLibro, rutUsuario);
    }

    public void handleDevolverLibro() {
        System.out.print("ID de la reserva: ");
        String idReserva = scanner.nextLine();

        this.bibliotecaApplicationService.devolverLibro(Integer.parseInt(idReserva));
    }
}
