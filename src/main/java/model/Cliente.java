package model;

/**
 * Modelo de Cliente.
 * Datos mínimos: Identificador, Nombre Comercial o Razón Social,
 * RNC o Cédula, Cuenta Contable, Estado.
 */
public class Cliente {

    private int id;
    private String nombreComercial;
    private String rncCedula;
    private String cuentaContable;
    private String estado;

    public Cliente() {
    }

    public Cliente(int id, String nombreComercial, String rncCedula, String cuentaContable, String estado) {
        this.id = id;
        this.nombreComercial = nombreComercial;
        this.rncCedula = rncCedula;
        this.cuentaContable = cuentaContable;
        this.estado = estado;
    }

    public Cliente(String nombreComercial, String rncCedula, String cuentaContable, String estado) {
        this.nombreComercial = nombreComercial;
        this.rncCedula = rncCedula;
        this.cuentaContable = cuentaContable;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRncCedula() {
        return rncCedula;
    }

    public void setRncCedula(String rncCedula) {
        this.rncCedula = rncCedula;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
