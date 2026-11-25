package mx.uacm.sqa.twofa.service;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import mx.uacm.sqa.twofa.dao.CodigoVerificacionDAO;
import mx.uacm.sqa.twofa.model.CodigoVerificacion;
import mx.uacm.sqa.twofa.model.ResultadoValidacion;
import mx.uacm.sqa.twofa.model.Usuario;

public class ServicioSegundoFactor {

    private static final int VIGENCIA_MINUTOS = 5;
    private static final int MAX_INTENTOS = 3;

    private final CodigoVerificacionDAO codigoDAO;
    private final ServicioCorreo servicioCorreo;
    private final Clock reloj;
    private final SecureRandom random = new SecureRandom();
    private String codigoFijoPruebas = null;

    public ServicioSegundoFactor(CodigoVerificacionDAO codigoDAO, ServicioCorreo servicioCorreo, Clock reloj) {
        this.codigoDAO = codigoDAO;
        this.servicioCorreo = servicioCorreo;
        this.reloj = reloj;
    }

    public void setCodigoFijoPruebas(String codigoFijoPruebas) {
        this.codigoFijoPruebas = codigoFijoPruebas;
    }

    public boolean generarYEnviar(Usuario usuario, String idIntento) {
        if (usuario == null || idIntento == null || idIntento.isBlank()) {
            throw new IllegalArgumentException("Datos de intento inválidos.");
        }
        String otp = (codigoFijoPruebas != null)
                ? codigoFijoPruebas
                : String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime expiracion = LocalDateTime.now(reloj).plusMinutes(VIGENCIA_MINUTOS);

        CodigoVerificacion registro = new CodigoVerificacion(idIntento, otp, expiracion, false, 0);
        codigoDAO.guardar(registro);

        return servicioCorreo.enviarCodigo(usuario.getCorreo(), otp);
    }

    public ResultadoValidacion validarCodigo(String idIntento, String codigoIngresado) {
        if (codigoIngresado == null || codigoIngresado.isBlank()) {
            return ResultadoValidacion.CAMPO_VACIO;
        }
        if (!codigoIngresado.matches("\\d{6}")) {
            return ResultadoValidacion.FORMATO_INVALIDO;
        }

        Optional<CodigoVerificacion> optCodigo = codigoDAO.buscarPorIntento(idIntento);
        if (optCodigo.isEmpty()) {
            return ResultadoValidacion.INTENTO_NO_VALIDO;
        }

        CodigoVerificacion registro = optCodigo.get();

        if (registro.isUtilizado()) {
            return ResultadoValidacion.CODIGO_REUTILIZADO;
        }
        if (registro.getIntentosValidacion() >= MAX_INTENTOS) {
            return ResultadoValidacion.LIMITE_INTENTOS_EXCEDIDO;
        }
        if (LocalDateTime.now(reloj).isAfter(registro.getExpiraEn())) {
            return ResultadoValidacion.CODIGO_EXPIRADO;
        }
        if (!registro.getCodigo().equals(codigoIngresado)) {
            int nuevosIntentos = registro.getIntentosValidacion() + 1;
            codigoDAO.incrementarIntentos(registro.getId());
            if (nuevosIntentos >= MAX_INTENTOS) {
                return ResultadoValidacion.LIMITE_INTENTOS_EXCEDIDO;
            }
            return ResultadoValidacion.CODIGO_INCORRECTO;
        }

        codigoDAO.marcarUtilizado(registro.getId());
        return ResultadoValidacion.VALIDO;
    }
}
