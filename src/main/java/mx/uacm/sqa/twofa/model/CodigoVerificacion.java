package mx.uacm.sqa.twofa.model;

import java.time.LocalDateTime;

public class CodigoVerificacion {
    private final int id;
    private final String idIntento;
    private final String codigo;
    private final LocalDateTime expiraEn;
    private final boolean utilizado;
    private final int intentosValidacion;

    public CodigoVerificacion(int id, String idIntento, String codigo, LocalDateTime expiraEn,
                              boolean utilizado, int intentosValidacion) {
        this.id = id;
        this.idIntento = idIntento;
        this.codigo = codigo;
        this.expiraEn = expiraEn;
        this.utilizado = utilizado;
        this.intentosValidacion = intentosValidacion;
    }

    public CodigoVerificacion(String idIntento, String codigo, LocalDateTime expiraEn,
                              boolean utilizado, int intentosValidacion) {
        this(0, idIntento, codigo, expiraEn, utilizado, intentosValidacion);
    }

    public int getId() { return id; }
    public String getIdIntento() { return idIntento; }
    public String getCodigo() { return codigo; }
    public LocalDateTime getExpiraEn() { return expiraEn; }
    public boolean isUtilizado() { return utilizado; }
    public int getIntentosValidacion() { return intentosValidacion; }
}
