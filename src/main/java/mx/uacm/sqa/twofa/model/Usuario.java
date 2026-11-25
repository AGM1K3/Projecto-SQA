package mx.uacm.sqa.twofa.model;

import java.time.LocalDateTime;

public class Usuario {
    private final int id;
    private final String identificador;
    private final String passwordHash;
    private final String correo;
    private int intentosFallidos;
    private LocalDateTime bloqueadoHasta;

    public Usuario(int id, String identificador, String passwordHash, String correo) {
        this.id = id;
        this.identificador = identificador;
        this.passwordHash = passwordHash;
        this.correo = correo;
        this.intentosFallidos = 0;
    }

    public int getId() { return id; }
    public String getIdentificador() { return identificador; }
    public String getPasswordHash() { return passwordHash; }
    public String getCorreo() { return correo; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }
}
