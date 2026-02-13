package model;

public class Usuario {
    private int id;
    private String usuario;
    private String password;
    private String estado;

    public Usuario() {}
    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
