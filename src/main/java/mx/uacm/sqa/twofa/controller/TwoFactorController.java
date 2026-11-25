package mx.uacm.sqa.twofa.controller;

import mx.uacm.sqa.twofa.model.ResultadoValidacion;
import mx.uacm.sqa.twofa.model.Usuario;
import mx.uacm.sqa.twofa.service.ServicioSegundoFactor;

public class TwoFactorController {

    private final ServicioSegundoFactor servicio2FA;

    public TwoFactorController(ServicioSegundoFactor servicio2FA) {
        this.servicio2FA = servicio2FA;
    }

    public String postVerificarCodigo(String idIntento, String codigo) {
        ResultadoValidacion res = servicio2FA.validarCodigo(idIntento, codigo);
        return (res == ResultadoValidacion.VALIDO)
                ? "VIEW: area-protegida"
                : "ERROR: " + res.name();
    }

    public String postReenviarCodigo(Usuario usuario, String idIntento) {
        boolean enviado = servicio2FA.generarYEnviar(usuario, idIntento);
        return enviado ? "INFO: Nuevo código enviado." : "ERROR: FALLO_CORREO";
    }
}
