package mx.uacm.sqa.twofa.model;

public enum ResultadoValidacion {
    VALIDO,
    CAMPO_VACIO,
    FORMATO_INVALIDO,
    INTENTO_NO_VALIDO,
    CODIGO_INCORRECTO,
    CODIGO_EXPIRADO,
    CODIGO_REUTILIZADO,
    LIMITE_INTENTOS_EXCEDIDO
}
