package interfaces.infraestructure;

import domain.entities.Usuario;
import domain.valueObject.DocumentoRut;

import java.util.ArrayList;
import java.util.Optional;

public interface IRepositorioUsuarios {
    /**
     * Método que obtiene un usuario por el RUT
     * @param rut rut a buscar
     * @return Opcional de usuario encontrado
     */
    Optional<Usuario> obtenerUsuario(DocumentoRut rut);

    /**
     * Método que agrega un usuario al repositorio de usuarios
     * @param usuario usuario a agregar al repositorio
     */
    void agregarUsuario(Usuario usuario);

    /**
     * Método que elimina un usuario del repositorio de usuarios
     * @param rut rut del usuario a eliminar
     * @return boolean indicativo si el proceso de eliminación fue exitoso o fallido
     */
    boolean eliminarUsuario(DocumentoRut rut);

    /**
     * Método que obtiene todos los usuarios del repositorio de usuarios
     * @return Arraylist de usuarios
     */
    ArrayList<Usuario> obtenerTodosLosUsuarios();
}
