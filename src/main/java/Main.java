import domain.entities.Libro;
import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.services.ServicioGenerador;
import domain.services.ServicioLibros;
import domain.services.ServicioPrestamos;
import domain.services.ServicioUsuarios;
import domain.valueObject.DocumentoRut;
import infraestructure.repositories.RepositorioLibros;
import infraestructure.repositories.RepositorioReservas;
import infraestructure.repositories.RepositorioUsuarios;
import presentation.controller.LibrosTerminalController;
import presentation.controller.UsuarioTerminalController;
import presentation.services.BibliotecaApplicationService;
import presentation.services.UsuarioApplicationService;
import presentation.view.TerminalApplication;

public class Main {
    public static void main(String[] args) {
        RepositorioLibros repositorioLibros = new RepositorioLibros();
        RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();
        RepositorioReservas repositorioReservas = new RepositorioReservas();

        ServicioLibros servicioLibros = new ServicioLibros(repositorioLibros);
        ServicioUsuarios servicioUsuarios = new ServicioUsuarios(repositorioUsuarios);
        ServicioPrestamos servicioPrestamos = new ServicioPrestamos(repositorioReservas, servicioLibros, servicioUsuarios);
        ServicioGenerador servicioGenerador = new ServicioGenerador();

        BibliotecaApplicationService bibliotecaApplicationService = new BibliotecaApplicationService(
                repositorioLibros, servicioLibros, servicioPrestamos, servicioGenerador
        );

        UsuarioApplicationService usuarioApplicationService = new UsuarioApplicationService(servicioUsuarios);

        // Registro de usuario
        Usuario usuario = usuarioApplicationService.registrarUsuario("Kevin Castillo", DocumentoRut.definir("20274916K"));

        // Agregar un libro
        Libro libro = bibliotecaApplicationService.agrearLibro("Libro test", "Alex", "Ciencia ficcion", "Uno");

        // Agregar una reserva
        Reserva reserva = bibliotecaApplicationService.prestarLibro(libro.getUuid(), usuario.getRut());

        // devolver un libro
        bibliotecaApplicationService.devolverLibro(reserva.getId());

        LibrosTerminalController librosTerminalController = new LibrosTerminalController(bibliotecaApplicationService);
        UsuarioTerminalController usuarioTerminalController = new UsuarioTerminalController(usuarioApplicationService);

        TerminalApplication terminalApplication = new TerminalApplication(
                librosTerminalController,
                usuarioTerminalController
        );

        terminalApplication.start();
    }
}
