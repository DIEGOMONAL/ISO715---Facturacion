package model;

import java.sql.Date;
import java.sql.Time;

public class Factura {

    private int id;
    private String cliente;
    private Date fecha;
    private Time hora;
    private double total;

    public Factura() {
    }

    public Factura(int id, String cliente, Date fecha, Time hora, double total) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
        this.total = total;
    }

    public Factura(String cliente, Date fecha, Time hora, double total) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
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
}

