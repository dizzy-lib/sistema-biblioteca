package application.usecases;

import domain.entities.Libro;
import domain.services.ServicioGenerador;
import domain.services.ServicioLibros;
import interfaces.infraestructure.IRepositorioLibros;
import shared.utils.Formateador;

public class AgregarLibroCasoUso {
    private final IRepositorioLibros repositorioLibros;
    private final ServicioLibros servicioLibros;
    private final ServicioGenerador servicioGenerador;

    public AgregarLibroCasoUso(
            IRepositorioLibros repositorioLibros,
            ServicioLibros servicioLibros,
            ServicioGenerador servicioGenerador
    ) {
        this.repositorioLibros = repositorioLibros;
        this.servicioLibros = servicioLibros;
        this.servicioGenerador = servicioGenerador;
    }

    /**
     * Método que ejecuta el caso de uso para agregar un libro en el repositorio
     * de libros solo en caso de que la información agregada esté validada, en caso
     * contrario lanza excepción InputMismatchException
     *
     * @param titulo título del libro
     * @param autor autor del libro
     * @param genero género del libro
     * @param editorial editorial del libro
     */
    public Libro ejecutar(String titulo, String autor, String genero, String editorial) {
        // normalizar los inputs
        String tituloFormateado = Formateador.normalizarString(titulo);
        String autorFormateado = Formateador.normalizarString(autor);
        String generoFormateado = Formateador.normalizarString(genero);
        String editorialFormateado = Formateador.normalizarString(editorial);

        // Genera el libro
        Libro libro = this.servicioGenerador.generarLibro(
                tituloFormateado,
                autorFormateado,
                generoFormateado,
                editorialFormateado
        );

        // validar que el libro esté correcto o lanza excepción
        // validar que el libro esté correcto o lanza excepción
        this.servicioLibros.validarLibro(libro);

        // guarda libro en el repositorio de libros
        this.repositorioLibros.agregarLibro(libro);

        return libro;
    }
}
