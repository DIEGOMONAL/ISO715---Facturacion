package model;

/**
 * Usuario del sistema con rol y estado de aprobación.
 * Roles: ADMIN, CAJERO, SUPERVISOR, INVENTARIO, CONTADOR
 * Estado: PENDING (pendiente aprobación), ACTIVO, INACTIVO
 */
public class Usuario {
    private int id;
    private String usuario;
    private String password;
    private String estado;  // PENDING, ACTIVO, INACTIVO
    private String rol;     // ADMIN, CAJERO, SUPERVISOR, INVENTARIO, CONTADOR
    private String nombreCompleto;
    private java.sql.Timestamp fechaRegistro;
    private Integer aprobadoPor;

    public Usuario() {}
    public Usuario(String usuario, String password, String rol) {
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public java.sql.Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(java.sql.Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Integer getAprobadoPor() { return aprobadoPor; }
    public void setAprobadoPor(Integer aprobadoPor) { this.aprobadoPor = aprobadoPor; }

    public boolean isPending() { return "PENDING".equals(estado); }
    public boolean isAdmin() { return "ADMIN".equals(rol); }
}
