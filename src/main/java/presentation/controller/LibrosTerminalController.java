package presentation.controller;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.valueObject.DocumentoRut;
import domain.valueObject.LibroCatalogoEntry;
import presentation.services.BibliotecaApplicationService;
import presentation.services.UsuarioApplicationService;
import shared.exceptions.LibroNoEncontradoException;
import shared.exceptions.OperacionCanceladaException;
import shared.exceptions.SinReservasActivasException;
import shared.exceptions.UsuarioNoEncontradoException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;

/**
 * Clase que controla toda la lógica por front (terminal) de los libros
 * de la biblioteca
 */
public class LibrosTerminalController {
    private final BibliotecaApplicationService bibliotecaApplicationService;
    private final UsuarioApplicationService usuarioApplicationService;
    private final Scanner scanner = new Scanner(System.in);

    public LibrosTerminalController(
            BibliotecaApplicationService bibliotecaApplicationService,
            UsuarioApplicationService usuarioApplicationService
    ) {
        this.bibliotecaApplicationService = bibliotecaApplicationService;
        this.usuarioApplicationService = usuarioApplicationService;
    }

    /**
     * Método que maneja el caso de agregar un nuevo libro al sistema
     */
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

    /**
     * Método que maneja buscar un libro dentro del sistema
     */
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

    /**
     * Método que maneja el caso de prestamo de libros
     */
    public void handlePrestarLibro() {
        // Obtener RUT válido del usuario registrado
        DocumentoRut rutUsuario = obtenerRutUsuarioValido();

        // Obtener libro seleccionado
        Libro libroSeleccionado = obtenerLibroSeleccionado();

        // Realizar el préstamo
        this.bibliotecaApplicationService.prestarLibro(libroSeleccionado.getUuid(), rutUsuario);
    }

    /**
     * Método privado que se encarga de validar un usuario válido dentro del sistema
     * @return DocumentoRut del usuario válido
     */
    private DocumentoRut obtenerRutUsuarioValido() {
        while (true) {
            System.out.print("Ingrese el rut del usuario: ");
            String rutUsuarioInput = scanner.nextLine();

            try {
                DocumentoRut rutUsuario = DocumentoRut.definir(rutUsuarioInput);
                this.usuarioApplicationService.verificarUsuarioRegistrado(rutUsuario);
                return rutUsuario; // RUT válido y usuario registrado
            } catch (UsuarioNoEncontradoException e) {
                System.out.println("Usuario no encontrado para el rut: " + rutUsuarioInput);
                System.out.println("Por favor, intente nuevamente.");
            } catch (Exception e) {
                System.out.println("RUT inválido. Por favor, intente nuevamente.");
            }
        }
    }

    /**
     * Método privado que se encarga de obtener un libro seleccionado por terminal
     * @return Libro seleccionado
     */
    private Libro obtenerLibroSeleccionado() {
        System.out.print("Nombre del libro: ");
        String nombreLibro = scanner.nextLine();

        // Buscar el libro
        ArrayList<Libro> librosEncontrados = this.bibliotecaApplicationService.buscarLibro(nombreLibro);

        if (librosEncontrados.isEmpty()) {
            System.out.println("No se encontraron libros con el título '" + nombreLibro + "'.");
            throw new LibroNoEncontradoException("Libro no encontrado");
        }

        return seleccionarLibroDeListado(librosEncontrados);
    }

    /**
     * Método privado que selecciona un libro del listado
     * @param librosEncontrados Arraylist de lisbros encontrados del sistema
     * @return Libro seleccionado por terminal
     */
    private Libro seleccionarLibroDeListado(ArrayList<Libro> librosEncontrados) {
        System.out.println("Libros encontrados:");
        for (int i = 0; i < librosEncontrados.size(); i++) {
            Libro libro = librosEncontrados.get(i);
            System.out.printf("%d. Título: %s, Autor: %s, Estado: %s (UUID: %s)%n",
                    i + 1, libro.getTitulo(), libro.getAutor(), libro.getEstado(), libro.getUuid());
        }

        while (true) {
            System.out.print("Seleccione el número del libro a reservar (0 para cancelar): ");

            try {
                int seleccionNum = Integer.parseInt(scanner.nextLine());

                if (seleccionNum == 0) {
                    throw new OperacionCanceladaException("Reserva cancelada por el usuario.");
                }

                if (seleccionNum >= 1 && seleccionNum <= librosEncontrados.size()) {
                    return librosEncontrados.get(seleccionNum - 1);
                }

                System.out.println("Número de selección inválido. Intente nuevamente.");

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ingresar un número. Intente nuevamente.");
            }
        }
    }

    /**
     * Método que maneja el caso de devolver un libro del sistema
     */
    public void handleDevolverLibro() {
        ArrayList<Reserva> reservasActivas = this.bibliotecaApplicationService.verReservasActivas();

        if (reservasActivas.isEmpty()) {
            throw new SinReservasActivasException("No existen reservas activas dentro del sistema");
        }

        // Ordenar las reservas por fecha de vencimiento
        reservasActivas.sort((r1, r2) -> r1.getFechaVencimiento().compareTo(r2.getFechaVencimiento()));

        System.out.print("\n=== Mostrando reservas activas (ordenadas por fecha de vencimiento) ===\n");

        for (Reserva reserva : reservasActivas) {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), reserva.getFechaVencimiento());
            String estadoVencimiento;
            if (diasRestantes < 0) {
                estadoVencimiento = "Vencida hace " + Math.abs(diasRestantes) + " días";
            } else {
                estadoVencimiento = "Vence en " + diasRestantes + " días";
            }

            System.out.printf("ID: %d | Libro: %s | Usuario: %s | Vencimiento: %s (%s)%n",
                    reserva.getId(),
                    reserva.getLibro().getTitulo(),
                    reserva.getUsuario().getRut().getFormateado(),
                    reserva.getFechaVencimiento(),
                    estadoVencimiento
            );
        }

        System.out.print("ID de la reserva a devolver: ");
        String idReserva = scanner.nextLine();

        this.bibliotecaApplicationService.devolverLibro(Integer.parseInt(idReserva));
    }

    /**
     * Método que maneja mostrar libros registrados dentro del sistema
     */
    public void handleMostrarLibros() {
        ArrayList<Libro> libros = this.bibliotecaApplicationService.obtenerTodosLosLibros();

        if (libros.isEmpty()) {
            throw new LibroNoEncontradoException("No hay libros registrados en el sistema");
        }

        // Ordenar los libros por título y luego por autor
        libros.sort((l1, l2) -> {
            int comp = l1.getTitulo().compareTo(l2.getTitulo());
            if (comp == 0) {
                return l1.getAutor().compareTo(l2.getAutor());
            }
            return comp;
        });

        System.out.print("\n=== Libros registrados (ordenados por título) ===\n");
        for (Libro libro : libros) {
            System.out.println(libro.getUuid() + " - " + libro.getTitulo() + " - " + libro.getAutor() + " - " +  libro.getEditorial()  + " - "+ libro.getEstado());
        }
    }

    /**
     * Método que maneja mostrar un catálogo único de libros (por título y autor)
     */
    public void handleMostrarCatalogoLibros() {
        Set<LibroCatalogoEntry> catalogo = this.bibliotecaApplicationService.obtenerCatalogoDeLibros();

        if (catalogo.isEmpty()) {
            System.out.println("El catálogo de libros está vacío.");
            return;
        }

        // Convertir a lista y ordenar para una presentación consistente
        ArrayList<LibroCatalogoEntry> catalogoOrdenado = new ArrayList<>(catalogo);
        catalogoOrdenado.sort((e1, e2) -> {
            int comp = e1.titulo().compareTo(e2.titulo());
            if (comp == 0) {
                return e1.autor().compareTo(e2.autor());
            }
            return comp;
        });

        System.out.print("\n=== Catálogo de Libros (Títulos Únicos) ===\n");
        for (LibroCatalogoEntry entry : catalogoOrdenado) {
            System.out.println("Título: " + entry.titulo() + " | Autor: " + entry.autor());
        }
    }
}
