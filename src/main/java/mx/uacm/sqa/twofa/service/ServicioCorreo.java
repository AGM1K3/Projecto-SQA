package mx.uacm.sqa.twofa.service;

public interface ServicioCorreo {
    boolean enviarCodigo(String correoDestino, String codigoOtp);
}
