package presentation.services;

import application.usecases.RegistrarUsuarioCasoUso;
import domain.entities.Usuario;
import domain.services.ServicioPersistencia;
import domain.services.ServicioUsuarios;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;
import domain.entities.Usuario;
import shared.exceptions.UsuarioNoEncontradoException;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Clase que ejecuta los casos de uso relacionados a los usuarios,
 * es la conexión del back y el front de la aplicación
 */
public class UsuarioApplicationService {
    private final RegistrarUsuarioCasoUso registrarUsuarioCasoUso;
    private final ServicioPersistencia servicioPersistencia;
    private final IRepositorioUsuarios repositorioUsuarios;
    private final String rutaUsuariosCsv;

    public UsuarioApplicationService(
            ServicioUsuarios servicioUsuarios,
            ServicioPersistencia servicioPersistencia,
            IRepositorioUsuarios repositorioUsuarios,
            String rutaUsuariosCsv
    ) {
        this.registrarUsuarioCasoUso = new RegistrarUsuarioCasoUso(servicioUsuarios);
        this.servicioPersistencia = servicioPersistencia;
        this.repositorioUsuarios = repositorioUsuarios;
        this.rutaUsuariosCsv = rutaUsuariosCsv;
    }

    /**
     * Método que ejecuta el caso de uso que agrega un usuario al repositorio de usuarios
     * @param nombre nombre del usuario
     * @param rut rut del usuario
     * @return Usuario registrado
     */
    public Usuario registrarUsuario(String nombre, DocumentoRut rut) {
        // Ejecuta el caso de uso que registra un usuario dentro del sistema
        Usuario usuario = this.registrarUsuarioCasoUso.ejecutar(nombre, rut);

        // Agrega la persistencia del servicio almacenando al usuario
        if (usuario != null) {
            this.servicioPersistencia.guardarUsuariosEnCSV(this.rutaUsuariosCsv, this.repositorioUsuarios.obtenerTodosLosUsuarios());
        }

        return usuario;
    }

    /**
     * Método que verifica si un usuario está registrado dentro del sistema
     * @param rut rut del usuario
     */
    public void verificarUsuarioRegistrado(DocumentoRut rut) {
        Optional<Usuario> usuario = this.repositorioUsuarios.obtenerUsuario(rut);
        if (usuario.isEmpty()) {
            String messageError = String.format("Usuario con rut: %s no encontrado", rut);
            throw new UsuarioNoEncontradoException(messageError);
        }
    }

    /**
     * Método que obtiene todos los usuarios del sistema
     * @return ArrayList de todos los usuarios registrados
     */
    public ArrayList<Usuario> obtenerTodosLosUsuarios() {
        return this.repositorioUsuarios.obtenerTodosLosUsuarios();
    }
}
