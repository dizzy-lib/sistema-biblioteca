package domain.entities;

public class Reserva {
    private final int id;
    private final Usuario usuario;
    private final Libro libro;

    public Reserva(int id, Usuario usuario, Libro libro) {
        this.id = id;
        this.usuario = usuario;
        this.libro = libro;
    }

    // Una reserva es inmutable por lo que
    // solo generamos getters

    /**
     * Método que obtiene el usuario de la reserva
     * @return usuario
     */
    public Usuario getUsuario() {
        return this.usuario;
    }

    /**
     * Método que obtiene el libro reservado
     * @return libro reservado
     */
    public Libro getLibro() {
        return this.libro;
    }

    /**
     * Método que obtiene el ID de la reserva
     * @return id de reserva
     */
    public int getId() {
        return this.id;
    }
}
