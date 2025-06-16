package domain.services;

import domain.entities.Libro;
import domain.entities.Reserva;
import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioReservas;
import shared.exceptions.ReservaNoEncontradaException;

import java.util.Optional;

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

  public Reserva buscarReservaPorId(int idReserva) {
    Optional<Reserva> reserva = this.repositorioReservas.buscarReservaPorId(idReserva);

    if (reserva.isEmpty()) {
      throw new ReservaNoEncontradaException(String.format("Reserva %d no encontrada", idReserva));
    }

    return reserva.get();
  }

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
