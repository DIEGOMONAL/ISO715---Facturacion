package model;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Factura con relaciones a Cliente, Condición de Pago, Vendedor
 * y líneas de detalle (artículos facturados).
 */
public class Factura {

    private int id;
    private int clienteId;
    private String clienteNombre;
    private int condicionPagoId;
    private String condicionPagoDescripcion;
    private int vendedorId;
    private String vendedorNombre;
    private Date fecha;
    private Time hora;
    private double total;
    private List<FacturaDetalle> detalles = new ArrayList<>();

    public Factura() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public int getCondicionPagoId() {
        return condicionPagoId;
    }

    public void setCondicionPagoId(int condicionPagoId) {
        this.condicionPagoId = condicionPagoId;
    }

    public String getCondicionPagoDescripcion() {
        return condicionPagoDescripcion;
    }

    public void setCondicionPagoDescripcion(String condicionPagoDescripcion) {
        this.condicionPagoDescripcion = condicionPagoDescripcion;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public String getVendedorNombre() {
        return vendedorNombre;
    }

    public void setVendedorNombre(String vendedorNombre) {
        this.vendedorNombre = vendedorNombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<FacturaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<FacturaDetalle> detalles) {
        this.detalles = detalles;
    }

    public void addDetalle(FacturaDetalle detalle) {
        this.detalles.add(detalle);
    }
}
