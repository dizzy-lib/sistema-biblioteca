package domain.services;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;
import shared.exceptions.UsuarioNoEncontradoException;
import shared.exceptions.UsuarioYaRegistradoException;
import shared.utils.Validaciones;

import java.util.Optional;

/**
 * Clase de servicio de usuarios que maneja la lógica de negocio para el manejo
 * de usuarios dentro del sistema
 */
public class ServicioUsuarios {
    private final IRepositorioUsuarios repositorioUsuarios;

    public ServicioUsuarios(IRepositorioUsuarios repositorioUsuarios) {
        this.repositorioUsuarios = repositorioUsuarios;
    }

    /**
     * Método que obtiene un usuario por rut dentro del repositorio de usuarios.
     * en caso de no encontrar usuario lanza excepción UsuarioNoEncontradoException
     * @param rut rut a buscar
     * @return usuario encontrado
     */
    public Usuario obtenerUsuarioPorRut(DocumentoRut rut) {
        // ejecuta la query de búsqueda dentro del repositorio
        // de usuarios
        Optional<Usuario> usuarioOptional = this.repositorioUsuarios.obtenerUsuario(rut);

        if (usuarioOptional.isEmpty()) {
            throw new UsuarioNoEncontradoException(String.format("Usuario de rut %s no encontrado", rut.getFormateado()));
        }

        return usuarioOptional.get();
    }

    /**
     * Método que agrega un usuario dentro del repositorio de usuarios
     * @param nombre nombre del usuario
     * @param rut rut del usuario
     * @return Usuario registrado
     */
    public Usuario registrarUsuario(String nombre, DocumentoRut rut) {
        // Valída que el nombre sea válido
        Validaciones.esAlfanumericoFlexible(nombre);

        // valída que el usuario no se encuentre registrado
        this.validaUsuarioExiste(rut);

        // genera un usuario
        Usuario usuario = new Usuario(nombre, rut);

        // agrega el usuario al repositorio de usuarios
        this.repositorioUsuarios.agregarUsuario(usuario);

        return usuario;
    }

    /**
     * Método que valída si un usuario existe dentro del repositorio de
     * usuarios, lanza UsuarioYaRegistradoException en caso de que el usuario
     * ya esté registrado dentro del sistema
     * @param rut rut del usuario a validar
     */
    private void validaUsuarioExiste(DocumentoRut rut) {
        Optional<Usuario> usuario = this.repositorioUsuarios.obtenerUsuario(rut);

        if (usuario.isPresent()) {
            throw new UsuarioYaRegistradoException(String.format("Usuario %s ya registrado", rut.getFormateado()));
        }
    }
}
