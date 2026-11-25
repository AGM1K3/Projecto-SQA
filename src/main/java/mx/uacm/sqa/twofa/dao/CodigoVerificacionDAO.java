package mx.uacm.sqa.twofa.dao;

import java.util.Optional;
import mx.uacm.sqa.twofa.model.CodigoVerificacion;

public interface CodigoVerificacionDAO {
    void guardar(CodigoVerificacion codigo);
    Optional<CodigoVerificacion> buscarPorIntento(String idIntento);
    void incrementarIntentos(int idCodigo);
    void marcarUtilizado(int idCodigo);
}
