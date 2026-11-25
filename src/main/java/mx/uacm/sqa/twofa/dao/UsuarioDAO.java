package mx.uacm.sqa.twofa.dao;

import java.time.LocalDateTime;
import java.util.Optional;
import mx.uacm.sqa.twofa.model.Usuario;

public interface UsuarioDAO {
    Optional<Usuario> buscarPorIdentificador(String identificador);
    void actualizarBloqueo(int idUsuario, int intentos, LocalDateTime bloqueadoHasta);
}
