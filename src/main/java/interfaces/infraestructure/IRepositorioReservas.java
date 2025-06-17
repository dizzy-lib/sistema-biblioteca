package interfaces.infraestructure;

import domain.entities.Reserva;

import java.util.Optional;

public interface IRepositorioReservas {
    /**
     * Método que busca una reserva por el id
     * @param id id de la reserva a buscar
     * @return Opcional de la reserva
     */
    Optional<Reserva> buscarReservaPorId(int id);

    /**
     * Método que elimina una reserva del repositorio de reservas
     * @param id id de la reserva
     * @return boolean indicativo si el proceso de eliminación fue exitoso o no
     */
    boolean eliminarReservaPorId(int id);

    /**
     * Método que agrega una reserva dentro del repositorio de reservas
     * @param reserva reserva a eliminar
     */
    void agregarReserva(Reserva reserva);

    /**
     * Obtiene la última reserva (la que tiene el ID más alto)
     * @return Optional con la última reserva o empty si no hay reservas
     */
    Optional<Reserva> obtenerUltimaReserva();

    /**
     * Método que obtiene todas las reservas del repositorio de reservas
     * @return ArrayList de reservas registradas en el repositorio
     */
    java.util.ArrayList<Reserva> obtenerTodasLasReservas();
}
