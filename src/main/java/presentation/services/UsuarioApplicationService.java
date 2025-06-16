package presentation.services;

import application.usecases.RegistrarUsuarioCasoUso;
import domain.entities.Usuario;
import domain.services.ServicioPersistencia;
import domain.services.ServicioUsuarios;
import domain.valueObject.DocumentoRut;
import interfaces.infraestructure.IRepositorioUsuarios;
import shared.exceptions.UsuarioNoEncontradoException;

import java.util.Optional;

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

    public Usuario registrarUsuario(String nombre, DocumentoRut rut) {
        Usuario usuario = this.registrarUsuarioCasoUso.ejecutar(nombre, rut);
        if (usuario != null) {
            this.servicioPersistencia.guardarUsuariosEnCSV(this.rutaUsuariosCsv, this.repositorioUsuarios.obtenerTodosLosUsuarios());
        }
        return usuario;
    }

    public void verificarUsuarioRegistrado(DocumentoRut rut) {
        Optional<Usuario> usuario = this.repositorioUsuarios.obtenerUsuario(rut);
        if (usuario.isEmpty()) {
            String messageError = String.format("Usuario con rut: %s no encontrado", rut);
            throw new UsuarioNoEncontradoException(messageError);
        }
    }
}
