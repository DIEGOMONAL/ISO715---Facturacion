package model;

/**
 * Línea de detalle de una factura (artículo facturado con cantidad y precio).
 */
public class FacturaDetalle {

    private int id;
    private int facturaId;
    private int articuloId;
    private String articuloDescripcion;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    public FacturaDetalle() {
    }

    public FacturaDetalle(int facturaId, int articuloId, int cantidad, double precioUnitario) {
        this.facturaId = facturaId;
        this.articuloId = articuloId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getArticuloId() {
        return articuloId;
    }

    public void setArticuloId(int articuloId) {
        this.articuloId = articuloId;
    }

    public String getArticuloDescripcion() {
        return articuloDescripcion;
    }

    public void setArticuloDescripcion(String articuloDescripcion) {
        this.articuloDescripcion = articuloDescripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
