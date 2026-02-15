package model;

public class Empresa {
    private int id;
    private String rnc;
    private String nombre;
    private String logoPath;
    private String direccion;
    private String telefono;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getRnc() { return rnc; }
    public void setRnc(String rnc) { this.rnc = rnc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
