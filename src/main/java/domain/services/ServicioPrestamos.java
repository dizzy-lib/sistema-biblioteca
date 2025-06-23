package domain.services;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioReservas;
import shared.exceptions.ReservaNoEncontradaException;

import java.util.Optional;

/**
 * Clase de servicio que maneja la lógica de negocio de reservas o prestamos
 */
public class ServicioPrestamos {
  private final IRepositorioReservas repositorioReservas;
  private final ServicioLibros servicioLibros;
  private final ServicioUsuarios servicioUsuarios;

  public ServicioPrestamos(
      IRepositorioReservas repositorioReservas,
      ServicioLibros servicioLibros,
      ServicioUsuarios servicioUsuarios) {
    this.repositorioReservas = repositorioReservas;
    this.servicioLibros = servicioLibros;
    this.servicioUsuarios = servicioUsuarios;
  }

  /**
   * Método que genera una reserva
   * @param uuidLibro uuid del libro a reservar
   * @param rutUsuario rut del usuario registrado que quiere reservar
   * @return reserva generada
   */
  public Reserva generarReserva(String uuidLibro, DocumentoRut rutUsuario) {
    // Busca el libro a reservar
    Libro libro = this.servicioLibros.obtenerLibroPorId(uuidLibro);

    // busca el usuario
    Usuario usuario = this.servicioUsuarios.obtenerUsuarioPorRut(rutUsuario);

    // obtiene último id de reserva generado
    int ultimoIdDeReserva = this.obtenerUltimoIdReserva();
    // incrementa en 1 el id de reserva
    int idReserva = ultimoIdDeReserva + 1;

    // Genera la reserva
    Reserva reserva = new Reserva(idReserva, usuario, libro);

    // agrega la reserva al repositorio de reservas
    this.repositorioReservas.agregarReserva(reserva);

    return reserva;
  }

  /**
   * Método que busca una reserva por ID dentro del repositorio, en caso de no encontrar
   * la reserva lanza una excepción ReservaNoEncontradaException
   * de reservas dentro del sistema
   * @param idReserva id de reserva
   * @return reserva encontrada
   */
  public Reserva buscarReservaPorId(int idReserva) {
    // busca la reserva dentro del repositorio de reservas por el id
    Optional<Reserva> reserva = this.repositorioReservas.buscarReservaPorId(idReserva);

    // En caso de no encontrar reserva lanza ReservaNoEncontradaException
    if (reserva.isEmpty()) {
      throw new ReservaNoEncontradaException(String.format("Reserva %d no encontrada", idReserva));
    }

    return reserva.get();
  }

  /**
   * Método que elimina una reserva dentro del sistema, disponibilizando el libro
   * reservado, y eliminando la reserva dentro del repositorio de reservas
   * @param reserva reserva a cancelar
   */
  public void eliminarReserva(Reserva reserva) {
    // obtiene el id de la reserva
    int idReserva = reserva.getId();

    // Marca como disponible el libro de la reserva
    Libro libroReservado = reserva.getLibro();
    String uuidLibroReservado = libroReservado.getUuid();
    this.servicioLibros.disponibilizarLibro(uuidLibroReservado);

    // con el libro disponible procede a eliminar la reserva del
    // repositorio de reservas
    this.repositorioReservas.eliminarReservaPorId(idReserva);
  }

  /**
   * Método que obtiene el último id de reserva generado
   * 
   * @return obtiene el último id generado
   */
  private int obtenerUltimoIdReserva() {
    Optional<Reserva> ultimaReserva = this.repositorioReservas.obtenerUltimaReserva();

    // obtiene id de la reserva más alta, en caso de que no exista alguna reserva
    // devuelve 0
    return ultimaReserva.map(Reserva::getId).orElse(0);

  }
}
