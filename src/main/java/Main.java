import domain.services.ServicioPersistencia;
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
import shared.exceptions.UsuarioYaRegistradoException;

import java.util.InputMismatchException;

public class Main {
  private static final String RUTA_ARCHIVO_LIBROS_CSV = "libros.csv";
  private static final String RUTA_ARCHIVO_USUARIOS_CSV = "usuarios.csv";
  private static final String RUTA_ARCHIVO_RESERVAS_CSV = "reservas.csv";

  public static void main(String[] args) {
    // Repositorios
    RepositorioLibros repositorioLibros = new RepositorioLibros();
    RepositorioUsuarios repositorioUsuarios = new RepositorioUsuarios();
    RepositorioReservas repositorioReservas = new RepositorioReservas();

    // Servicios de dominio
    ServicioLibros servicioLibros = new ServicioLibros(repositorioLibros);
    ServicioUsuarios servicioUsuarios = new ServicioUsuarios(repositorioUsuarios);
    ServicioPrestamos servicioPrestamos = new ServicioPrestamos(repositorioReservas, servicioLibros, servicioUsuarios);
    ServicioGenerador servicioGenerador = new ServicioGenerador();
    ServicioPersistencia servicioPersistencia = new ServicioPersistencia();

    // 1. Intentar cargar datos desde archivos CSV (datos iniciales/backup)
    System.out.println("Intentando cargar datos desde CSV...");
    servicioPersistencia.cargarUsuariosDesdeCSV(RUTA_ARCHIVO_USUARIOS_CSV, repositorioUsuarios);
    servicioPersistencia.cargarLibrosDesdeCSV(RUTA_ARCHIVO_LIBROS_CSV, repositorioLibros);
    // Cargar reservas DESPUÉS de libros y usuarios, ya que las reservas dependen de ellos
    servicioPersistencia.cargarReservasDesdeCSV(RUTA_ARCHIVO_RESERVAS_CSV, repositorioReservas, repositorioUsuarios, repositorioLibros);

    // Application Services
    // Se inyecta ServicioPersistencia y las rutas de los archivos para guardado inmediato
    BibliotecaApplicationService bibliotecaApplicationService = new BibliotecaApplicationService(
        repositorioLibros, servicioLibros, servicioPrestamos, servicioGenerador,
        servicioPersistencia, repositorioReservas,
        RUTA_ARCHIVO_LIBROS_CSV, RUTA_ARCHIVO_RESERVAS_CSV);
    UsuarioApplicationService usuarioApplicationService = new UsuarioApplicationService(
        servicioUsuarios, servicioPersistencia, repositorioUsuarios, RUTA_ARCHIVO_USUARIOS_CSV);

    // 2. Si aún no hay datos cargados desde CSV, agregar datos de ejemplo
    if (repositorioUsuarios.obtenerTodosLosUsuarios().isEmpty()) {
      try {
        System.out.println("No se cargaron usuarios desde CSV. Registrando usuario de ejemplo...");
        usuarioApplicationService.registrarUsuario("Kevin Ejemplo", DocumentoRut.definir("11111111-1"));
      } catch (IllegalArgumentException | UsuarioYaRegistradoException e) {
        System.err.println("Error al registrar usuario de ejemplo: " + e.getMessage());
      } catch (RuntimeException e) {
        System.err.println("Error inesperado al registrar usuario de ejemplo: " + e.getMessage());
        e.printStackTrace();
      }
    }
    if (repositorioLibros.obtenerTodosLosLibros().isEmpty()) {
      try {
        System.out.println("No se cargaron libros desde CSV. Agregando libro de ejemplo...");

        String tituloEjemplo = shared.utils.Formateador.normalizarString("El Quijote");
        String autorEjemplo = shared.utils.Formateador.normalizarString("Miguel de Cervantes");
        String generoEjemplo = shared.utils.Formateador.normalizarString("Clásico");
        String editorialEjemplo = shared.utils.Formateador.normalizarString("Editorial Sol");

        bibliotecaApplicationService.agrearLibro(tituloEjemplo, autorEjemplo, generoEjemplo, editorialEjemplo);
      } catch (InputMismatchException | IllegalArgumentException e) {
        System.err.println("Error al agregar libro de ejemplo '"
            + shared.utils.Formateador.normalizarString("El Quijote") + "': " + e.getMessage());
      } catch (RuntimeException e) {
        System.err.println("Error inesperado al agregar libro de ejemplo '"
            + shared.utils.Formateador.normalizarString("El Quijote") + "': " + e.getMessage());
        e.printStackTrace();
      }
    }

    // Controladores de Terminal
    LibrosTerminalController librosTerminalController = new LibrosTerminalController(bibliotecaApplicationService);
    UsuarioTerminalController usuarioTerminalController = new UsuarioTerminalController(usuarioApplicationService);

    // Aplicación de Terminal
    TerminalApplication terminalApplication = new TerminalApplication(
        librosTerminalController,
        usuarioTerminalController);

    terminalApplication.start();

    // Guardar datos al salir de la aplicación en archivos CSV
    System.out.println("Guardando datos antes de salir...");
    servicioPersistencia.guardarLibrosEnCSV(RUTA_ARCHIVO_LIBROS_CSV,
        repositorioLibros.obtenerTodosLosLibros());
    servicioPersistencia.guardarUsuariosEnCSV(RUTA_ARCHIVO_USUARIOS_CSV,
        repositorioUsuarios.obtenerTodosLosUsuarios());
    servicioPersistencia.guardarReservasEnCSV(RUTA_ARCHIVO_RESERVAS_CSV,
        repositorioReservas.obtenerTodasLasReservas());

    System.out.println("Datos guardados. Programa finalizado.");
  }
}
