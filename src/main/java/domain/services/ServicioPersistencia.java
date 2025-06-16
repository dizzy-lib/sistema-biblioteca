package domain.services;

import domain.entities.Libro;
import domain.entities.Usuario;
import domain.enums.EstadoLibro;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioLibros;
import interfaces.infraestructure.IRepositorioUsuarios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Optional;
import domain.entities.Reserva;
import interfaces.infraestructure.IRepositorioReservas;

public class ServicioPersistencia {

  private static final String SEPARADOR_CSV = ";";

  /**
   * Carga libros desde un archivo CSV.
   * Formato CSV esperado: uuid,titulo,autor,genero,editorial,estado
   * ADVERTENCIA: Debido al diseño actual de la entidad Libro (UUID final y
   * autogenerado),
   * el UUID original del CSV no puede ser restaurado. Se creará un nuevo Libro
   * con
   * un nuevo UUID, y se intentará restaurar el estado.
   */
  public void cargarLibrosDesdeCSV(String rutaArchivoCsv, IRepositorioLibros repoLibros) {
    File archivo = new File(rutaArchivoCsv);
    if (!archivo.exists()) {
      System.out.println(
          "Archivo de libros no encontrado: " + rutaArchivoCsv + ". Se iniciará sin datos de libros preexistentes.");
      return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCsv))) {
      String linea;
      while ((linea = br.readLine()) != null) {
        if (linea.trim().isEmpty() || linea.startsWith("uuid"))
          continue; // Saltar vacías o cabecera

        String[] datos = linea.split(SEPARADOR_CSV);
        if (datos.length >= 6) {
          try {
            String titulo = datos[1];
            String autor = datos[2];
            String genero = datos[3];
            String editorial = datos[4];
            EstadoLibro estado = EstadoLibro.valueOf(datos[5].trim().toUpperCase());

            Libro libro = new Libro(titulo, autor, genero, editorial);
            if (estado == EstadoLibro.RESERVADO) {
              libro.marcarComoReservado();
            } else {
              libro.marcarComoDiponible();
            }
            repoLibros.agregarLibro(libro);
          } catch (IllegalArgumentException e) {
            System.err
                .println("Error al parsear estado o datos del libro en línea CSV: " + linea + " - " + e.getMessage());
          } catch (RuntimeException e) {
            System.err.println("Error procesando línea de libro CSV: " + linea + " - " + e.getMessage());
          }
        } else {
          System.err.println("Línea CSV de libro con formato incorrecto (datos insuficientes): " + linea);
        }
      }
      System.out.println("Libros cargados desde: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al leer el archivo de libros CSV: " + e.getMessage());
    }
  }

  /**
   * Carga usuarios desde un archivo CSV.
   * Formato CSV esperado: rutConFormato,nombre (e.g., "12.345.678-K,Nombre
   * Apellido")
   */
  public void cargarUsuariosDesdeCSV(String rutaArchivoCsv, IRepositorioUsuarios repoUsuarios) {
    File archivo = new File(rutaArchivoCsv);
    if (!archivo.exists()) {
      System.out.println("Archivo de usuarios no encontrado: " + rutaArchivoCsv
          + ". Se iniciará sin datos de usuarios preexistentes.");
      return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCsv))) {
      String linea;
      while ((linea = br.readLine()) != null) {
        if (linea.trim().isEmpty() || linea.toLowerCase().startsWith("rut"))
          continue; // Saltar vacías o cabecera

        String[] datos = linea.split(SEPARADOR_CSV);
        if (datos.length >= 2) {
          try {
            String rutCompleto = datos[0].trim();
            String nombre = datos[1].trim();

            DocumentoRut rut = DocumentoRut.definir(rutCompleto);
            Usuario usuario = new Usuario(nombre, rut);
            if (repoUsuarios.obtenerUsuario(rut).isEmpty()) {
              repoUsuarios.agregarUsuario(usuario);
            } else {
              System.err.println("Usuario con RUT " + rut.getFormateado()
                  + " ya existe, no se cargó desde CSV para evitar duplicados.");
            }
          } catch (IllegalArgumentException e) {
            System.err
                .println("Error al parsear RUT o datos del usuario en línea CSV: " + linea + " - " + e.getMessage());
          } catch (RuntimeException e) {
            System.err.println("Error procesando línea de usuario CSV: " + linea + " - " + e.getMessage());
          }
        } else {
          System.err.println("Línea CSV de usuario con formato incorrecto (datos insuficientes): " + linea);
        }
      }
      System.out.println("Usuarios cargados desde: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al leer el archivo de usuarios CSV: " + e.getMessage());
    }
  }

  /**
   * Guarda la lista de libros en un archivo CSV.
   * Formato CSV: uuid,titulo,autor,genero,editorial,estado
   */
  public void guardarLibrosEnCSV(String rutaArchivoCsv, List<Libro> libros) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoCsv))) {
      // Escribir la cabecera
      bw.write("uuid" + SEPARADOR_CSV + "titulo" + SEPARADOR_CSV + "autor" + SEPARADOR_CSV + "genero" + SEPARADOR_CSV
          + "editorial" + SEPARADOR_CSV + "estado");
      bw.newLine();

      // Escribir cada libro
      for (Libro libro : libros) {
        String linea = String.join(SEPARADOR_CSV,
            libro.getUuid(),
            libro.getTitulo(),
            libro.getAutor(),
            libro.getGenero(),
            libro.getEditorial(),
            libro.getEstado().toString());
        bw.write(linea);
        bw.newLine();
      }
      System.out.println("Libros guardados en: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al escribir el archivo de libros CSV: " + e.getMessage());
    }
  }

  /**
   * Guarda la lista de usuarios en un archivo CSV.
   * Formato CSV: rut_formateado,nombre
   */
  public void guardarUsuariosEnCSV(String rutaArchivoCsv, List<Usuario> usuarios) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoCsv))) {
      // Escribir la cabecera
      bw.write("rut_formateado" + SEPARADOR_CSV + "nombre");
      bw.newLine();

      // Escribir cada usuario
      for (Usuario usuario : usuarios) {
        String linea = String.join(SEPARADOR_CSV,
            usuario.getRut().getFormateado(),
            usuario.getNombre());
        bw.write(linea);
        bw.newLine();
      }
      System.out.println("Usuarios guardados en: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al escribir el archivo de usuarios CSV: " + e.getMessage());
    }
  }

  /**
   * Carga reservas desde un archivo CSV.
   * Formato CSV esperado: id_reserva;rut_usuario;uuid_libro
   */
  public void cargarReservasDesdeCSV(String rutaArchivoCsv, IRepositorioReservas repoReservas,
      IRepositorioUsuarios repoUsuarios, IRepositorioLibros repoLibros) {
    File archivo = new File(rutaArchivoCsv);
    if (!archivo.exists()) {
      System.out.println(
          "Archivo de reservas no encontrado: " + rutaArchivoCsv
              + ". Se iniciará sin datos de reservas preexistentes.");
      return;
    }

    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCsv))) {
      String linea;
      while ((linea = br.readLine()) != null) {
        if (linea.trim().isEmpty() || linea.toLowerCase().startsWith("id_reserva")) {
          continue; // Saltar vacías o cabecera
        }

        String[] datos = linea.split(SEPARADOR_CSV);
        if (datos.length >= 3) {
          try {
            int idReserva = Integer.parseInt(datos[0].trim());
            String rutUsuarioStr = datos[1].trim();
            String uuidLibro = datos[2].trim();

            DocumentoRut rutUsuario = DocumentoRut.definir(rutUsuarioStr);
            Optional<Usuario> usuarioOpt = repoUsuarios.obtenerUsuario(rutUsuario);
            Optional<Libro> libroOpt = repoLibros.buscarLibroPorId(uuidLibro);

            if (usuarioOpt.isPresent() && libroOpt.isPresent()) {
              Usuario usuario = usuarioOpt.get();
              Libro libro = libroOpt.get();
              // Validar si el libro ya está marcado como RESERVADO, si no, marcarlo.
              // Esto es importante si la carga de libros y reservas no está sincronizada
              // o si el estado del libro en libros.csv no es la fuente de verdad final
              // para libros reservados.
              if (libro.getEstado() != EstadoLibro.RESERVADO) {
                System.out.println(
                    "Info: Libro " + libro.getUuid() + " marcado como RESERVADO debido a reserva activa " + idReserva);
                libro.marcarComoReservado();
              }
              Reserva reserva = new Reserva(idReserva, usuario, libro);
              repoReservas.agregarReserva(reserva);
            } else {
              if (usuarioOpt.isEmpty()) {
                System.err.println("Usuario con RUT " + rutUsuarioStr + " no encontrado para reserva " + idReserva
                    + ". Reserva no cargada.");
              }
              if (libroOpt.isEmpty()) {
                System.err.println(
                    "Libro con UUID " + uuidLibro + " no encontrado para reserva " + idReserva
                        + ". Reserva no cargada.");
              }
            }
          } catch (NumberFormatException e) {
            System.err.println("Error al parsear ID de reserva en línea CSV: " + linea + " - " + e.getMessage());
          } catch (IllegalArgumentException e) {
            System.err
                .println("Error al parsear RUT o datos de reserva en línea CSV: " + linea + " - " + e.getMessage());
          } catch (RuntimeException e) {
            System.err.println("Error procesando línea de reserva CSV: " + linea + " - " + e.getMessage());
          }
        } else {
          System.err.println("Línea CSV de reserva con formato incorrecto (datos insuficientes): " + linea);
        }
      }
      System.out.println("Reservas cargadas desde: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al leer el archivo de reservas CSV: " + e.getMessage());
    }
  }

  /**
   * Guarda la lista de reservas en un archivo CSV.
   * Formato CSV: id_reserva;rut_usuario;uuid_libro
   */
  public void guardarReservasEnCSV(String rutaArchivoCsv, List<Reserva> reservas) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivoCsv))) {
      // Escribir la cabecera
      bw.write("id_reserva" + SEPARADOR_CSV + "rut_usuario" + SEPARADOR_CSV + "uuid_libro");
      bw.newLine();

      // Escribir cada reserva
      for (Reserva reserva : reservas) {
        String linea = String.join(SEPARADOR_CSV,
            String.valueOf(reserva.getId()),
            reserva.getUsuario().getRut().getFormateado(),
            reserva.getLibro().getUuid());
        bw.write(linea);
        bw.newLine();
      }
      System.out.println("Reservas guardadas en: " + rutaArchivoCsv);
    } catch (IOException e) {
      System.err.println("Error al escribir el archivo de reservas CSV: " + e.getMessage());
    }
  }
}
