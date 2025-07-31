package Model;

import java.util.Date;

public class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    private Date fechaVencimiento;

    public Producto(int id, String nombre, String categoria, double precio, int cantidad, Date fechaVencimiento) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fechaVencimiento = fechaVencimiento;
    }

    public void actualizarStock(int cantidad) {
        this.cantidad += cantidad;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getCantidad() { return cantidad; }
    public Date getFechaVencimiento() { return fechaVencimiento; }
}