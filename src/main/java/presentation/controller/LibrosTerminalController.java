package presentation.controller;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.valueObject.DocumentoRut;
import presentation.services.BibliotecaApplicationService;
import shared.exceptions.LibroNoEncontradoException;
import shared.exceptions.SinReservasActivasException;

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
        System.out.print("Ingrese término de búsqueda (título, autor, editorial): ");
        String criterioBusqueda = scanner.nextLine();

        ArrayList<Libro> librosEncontrados = this.bibliotecaApplicationService.buscarLibro(criterioBusqueda);

        if (librosEncontrados.isEmpty()) {
            System.out.println("No se encontraron libros que coincidan con el término de búsqueda.");
        } else {
            System.out.println("Libros encontrados:");
            for (Libro libro : librosEncontrados) {
                System.out.printf("  ID: %s, Título: %s, Autor: %s, Estado: %s%n",
                        libro.getUuid(), libro.getTitulo(), libro.getAutor(), libro.getEstado());
            }
        }
    }

    public void handlePrestarLibro() {
        System.out.print("Ingrese el rut del usuario: ");
        String rutUsuarioInput = scanner.nextLine();

        DocumentoRut rutUsuario = DocumentoRut.definir(rutUsuarioInput);

        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();

        // Buscar el libro
        ArrayList<Libro> librosEncontrados = this.bibliotecaApplicationService.buscarLibro(nombreLibro);

        if (librosEncontrados.isEmpty()) {
            System.out.println("No se encontraron libros con el título '" + nombreLibro + "'.");
            throw new LibroNoEncontradoException("Libro no encontrado");
        }

        System.out.println("Libros encontrados:");
        for (int i = 0; i < librosEncontrados.size(); i++) {
            Libro libro = librosEncontrados.get(i);
            System.out.printf("%d. Título: %s, Autor: %s, Estado: %s (UUID: %s)%n",
                    i + 1, libro.getTitulo(), libro.getAutor(), libro.getEstado(), libro.getUuid());
        }

        System.out.print("Seleccione el número del libro a reservar (0 para cancelar): ");
        int seleccionNum;
        try {
            seleccionNum = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Debe ingresar un número.");
            return;
        }

        if (seleccionNum == 0) {
            System.out.println("Reserva cancelada.");
            return;
        }

        if (seleccionNum < 1 || seleccionNum > librosEncontrados.size()) {
            System.out.println("Número de selección inválido.");
            return;
        }

        Libro libroSeleccionado = librosEncontrados.get(seleccionNum - 1);
        this.bibliotecaApplicationService.prestarLibro(libroSeleccionado.getUuid(), rutUsuario);
    }

    public void handleDevolverLibro() {
        ArrayList<Reserva> reservasActivas = this.bibliotecaApplicationService.verReservasActivas();

        if (reservasActivas.isEmpty()) {
            throw new SinReservasActivasException("No existen reservas activas dentro del sistema");
        }

        System.out.print("\n=== Mostrando reservas activas ===\n");

        for (Reserva reserva : reservasActivas) {
            System.out.println("ID: " + reserva.getId() + " | " + "Nombre: " + reserva.getLibro().getTitulo() + " | " + "Rut cliente: " + reserva.getUsuario().getRut().getFormateado());
        }

        System.out.print("ID de la reserva: ");
        String idReserva = scanner.nextLine();

        this.bibliotecaApplicationService.devolverLibro(Integer.parseInt(idReserva));
    }

    public void handleMostrarLibros() {
        ArrayList<Libro> libros = this.bibliotecaApplicationService.obtenerTodosLosLibros();

        if (libros.isEmpty()) {
            throw new LibroNoEncontradoException("No hay libros registrados en el sistema");
        }

        System.out.print("\n=== Libros registrados ===\n");
        for (Libro libro : libros) {
            System.out.println(libro.getUuid() + " - " + libro.getTitulo() + " - " + libro.getAutor() + " - " +  libro.getEditorial()  + " - "+ libro.getEstado());
        }
    }
}
