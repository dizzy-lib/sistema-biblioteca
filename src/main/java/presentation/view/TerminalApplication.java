package presentation.view;

import presentation.controller.LibrosTerminalController;
import presentation.controller.UsuarioTerminalController;
import shared.exceptions.*;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TerminalApplication {
  private final LibrosTerminalController librosTerminalController;
  private final UsuarioTerminalController usuarioTerminalController;
  private final Scanner scanner = new Scanner(System.in);

  public TerminalApplication(
      LibrosTerminalController librosTerminalController,
      UsuarioTerminalController usuarioTerminalController) {
    this.librosTerminalController = librosTerminalController;
    this.usuarioTerminalController = usuarioTerminalController;
  }

  public void start() {
    mostrarBanner();

    boolean running = true;
    while (running) {
      mostrarMenu();
      try {
        System.out.print("Seleccione una opción: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        switch (opcion) {
          case 1:
            handleRegistrarUsuario();
            break;
          case 2:
            handleMostrarListaLibros();
            break;
          case 3:
            handleAgregarLibro();
            break;
          case 4:
            handleBuscarLibro();
            break;
          case 5:
            handlePrestarLibro();
            break;
          case 6:
            handleDevolverLibro();
            break;
          case 0:
            running = false;
            System.out.println("Saliendo del sistema...");
            break;
          default:
            System.out.println("Opción no válida. Por favor, intente de nuevo.");
        }
      } catch (InputMismatchException e) {
        System.out.println("Error: Entrada no válida. Debe ingresar un número para la opción.");
        scanner.nextLine();
      } catch (NoSuchElementException e) {
        System.out.println("Error: No se detectó entrada. Saliendo.");
        running = false;
      } catch (RuntimeException e) {
        System.out.println("Ha ocurrido un error inesperado en el menú: " + e.getMessage());
        e.printStackTrace();
      }
      if (running) {
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
      }
    }
  }

  private void handleMostrarListaLibros() {
    try {
      this.librosTerminalController.handleMostrarLibros();
    } catch (LibroNoEncontradoException e) {
      System.out.println("Error: Libros no encontrados");
    }
  }

  private void handleRegistrarUsuario() {
    try {
      this.usuarioTerminalController.handleRegistrarUsuario();
      System.out.println("Usuario registrado exitosamente.");
    } catch (InputMismatchException e) {
      System.out.println("Error de entrada: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Error en los datos ingresados: " + e.getMessage());
    } catch (UsuarioYaRegistradoException e) {
      System.out.println("Error al registrar usuario: " + e.getMessage());
    } catch (RuntimeException e) {
      System.out.println("Error inesperado al registrar usuario: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void handleAgregarLibro() {
    try {
      this.librosTerminalController.handleAgregarLibro();
      System.out.println("Libro agregado exitosamente.");
    } catch (InputMismatchException e) {
      System.out.println("Error de entrada: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Error en los datos ingresados: " + e.getMessage());
    } catch (RuntimeException e) {
      System.out.println("Error inesperado al agregar libro: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void handleBuscarLibro() {
    try {
      this.librosTerminalController.handleBuscarLibro();
    } catch (InputMismatchException e) {
      System.out.println("Error de entrada: " + e.getMessage());
    } catch (RuntimeException e) {
      System.out.println("Error inesperado al buscar libro: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void handlePrestarLibro() {
    try {
      this.librosTerminalController.handlePrestarLibro();
      System.out.println("Libro prestado exitosamente.");
    } catch (InputMismatchException e) {
      System.out.println("Error de entrada: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Error en los datos ingresados: " + e.getMessage());
    } catch (LibroNoEncontradoException | UsuarioException | LibroYaPrestadoException e) {
      System.out.println("Error al prestar libro: " + e.getMessage());
    } catch (RuntimeException e) {
      System.out.println("Error inesperado al prestar libro: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void handleDevolverLibro() {
    try {
      this.librosTerminalController.handleDevolverLibro();
      System.out.println("Libro devuelto exitosamente.");
    } catch (NumberFormatException e) {
      System.out.println("Error: El ID de la reserva debe ser un número.");
    } catch (ReservaNoEncontradaException | LibroNoEncontradoException e) {
      System.out.println("Error al devolver libro: " + e.getMessage());
    }  catch (SinReservasActivasException e) {
      System.out.println("No existen reservas activas dentro del sistema");
    } catch (RuntimeException e) { // Changed from Exception
      System.out.println("Error inesperado al devolver libro: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private void mostrarBanner() {
    System.out.println("=============================");
    System.out.println("=== Sistema de biblioteca ===");
    System.out.println("=============================");
  }

  private void mostrarMenu() {
    System.out.println("\n--- Menú Principal ---");
    System.out.println("1. Registrar usuario");
    System.out.println("2. Mostrar libros registrados");
    System.out.println("3. Agregar libro");
    System.out.println("4. Buscar libro");
    System.out.println("5. Prestar libro");
    System.out.println("6. Devolver libro");
    System.out.println("0. Salir");
    System.out.println("----------------------");
  }
}
