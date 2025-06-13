package interfaces.infraestructure;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;

import java.util.ArrayList;
import java.util.Optional;

public interface IRepositorioUsuarios {
    Optional<Usuario> obtenerUsuario(DocumentoRut rut);
    void agregarUsuario(Usuario usuario);
    boolean eliminarUsuario(DocumentoRut rut);
    ArrayList<Usuario> obtenerTodosLosUsuarios();
}
