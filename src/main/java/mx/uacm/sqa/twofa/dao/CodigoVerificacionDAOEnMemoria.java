package mx.uacm.sqa.twofa.dao;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import mx.uacm.sqa.twofa.model.CodigoVerificacion;

public class CodigoVerificacionDAOEnMemoria implements CodigoVerificacionDAO {

    private final Map<String, CodigoVerificacion> porIntento = new ConcurrentHashMap<>();
    private final AtomicInteger secuencia = new AtomicInteger(1);

    @Override
    public void guardar(CodigoVerificacion codigo) {
        int id = secuencia.getAndIncrement();
        CodigoVerificacion conId = new CodigoVerificacion(
                id,
                codigo.getIdIntento(),
                codigo.getCodigo(),
                codigo.getExpiraEn(),
                codigo.isUtilizado(),
                codigo.getIntentosValidacion()
        );
        porIntento.put(codigo.getIdIntento(), conId);
    }

    @Override
    public Optional<CodigoVerificacion> buscarPorIntento(String idIntento) {
        if (idIntento == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(porIntento.get(idIntento));
    }

    @Override
    public void incrementarIntentos(int idCodigo) {
        porIntento.values().stream()
                .filter(c -> c.getId() == idCodigo)
                .findFirst()
                .ifPresent(c -> porIntento.put(c.getIdIntento(),
                        new CodigoVerificacion(c.getId(), c.getIdIntento(), c.getCodigo(),
                                c.getExpiraEn(), c.isUtilizado(), c.getIntentosValidacion() + 1)));
    }

    @Override
    public void marcarUtilizado(int idCodigo) {
        porIntento.values().stream()
                .filter(c -> c.getId() == idCodigo)
                .findFirst()
                .ifPresent(c -> porIntento.put(c.getIdIntento(),
                        new CodigoVerificacion(c.getId(), c.getIdIntento(), c.getCodigo(),
                                c.getExpiraEn(), true, c.getIntentosValidacion())));
    }
}
