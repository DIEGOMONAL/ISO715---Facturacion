package model;

/**
 * Modelo de Condición de Pago.
 * Datos mínimos: Identificador, Descripción, Cantidad de Días, Estado.
 */
public class CondicionPago {

    private int id;
    private String descripcion;
    private int cantidadDias;
    private String estado;

    public CondicionPago() {
    }

    public CondicionPago(int id, String descripcion, int cantidadDias, String estado) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidadDias = cantidadDias;
        this.estado = estado;
    }

    public CondicionPago(String descripcion, int cantidadDias, String estado) {
        this.descripcion = descripcion;
        this.cantidadDias = cantidadDias;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
