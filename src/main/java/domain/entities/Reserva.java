package domain.entities;

/**
 * Record de reserva, se escoge trabajar con un record vs una clase
 * ya que este record solo puede obtener datos y no actualizar, esto se debe
 * a que por lógica de negocio una reserva no puede ser modificada internamente
 */
public record Reserva(int id, Usuario usuario, Libro libro) {
    /**
     * Método que obtiene el usuario de la reserva
     *
     * @return usuario
     */
    @Override
    public Usuario usuario() {
        return this.usuario;
    }

    /**
     * Método que obtiene el libro reservado
     *
     * @return libro reservado
     */
    @Override
    public Libro libro() {
        return this.libro;
    }

    /**
     * Método que obtiene el ID de la reserva
     *
     * @return id de reserva
     */
    @Override
    public int id() {
        return this.id;
    }
}
