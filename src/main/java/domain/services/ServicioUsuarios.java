package domain.services;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;
import shared.exceptions.UsuarioException;
import shared.exceptions.UsuarioYaRegistradoException;
import shared.utils.Validaciones;

import java.util.Optional;

public class ServicioUsuarios {
    private final IRepositorioUsuarios repositorioUsuarios;

    public ServicioUsuarios(IRepositorioUsuarios repositorioUsuarios) {
        this.repositorioUsuarios = repositorioUsuarios;
    }

    public Usuario obtenerUsuarioPorRut(DocumentoRut rut) {
        // ejecuta la query de búsqueda dentro del repositorio
        // de usuarios
        Optional<Usuario> usuarioOptional = this.repositorioUsuarios.obtenerUsuario(rut);

        if (usuarioOptional.isEmpty()) {
            throw new UsuarioException(String.format("Usuario de rut %s no encontrado", rut.getFormateado()));
        }

        return usuarioOptional.get();
    }

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

    private void validaUsuarioExiste(DocumentoRut rut) {
        Optional<Usuario> usuario = this.repositorioUsuarios.obtenerUsuario(rut);

        if (usuario.isPresent()) {
            throw new UsuarioYaRegistradoException(String.format("Usuario %s ya registrado", rut.getFormateado()));
        }
    }
}
