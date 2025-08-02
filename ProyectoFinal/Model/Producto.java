package Model;

import java.util.Date;

public class Producto {
    private int id;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    private Date fechaVencimiento;
    private String proveedor;

    public Producto(int id, String nombre, String categoria, double precio, int cantidad, Date fechaVencimiento, String proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.cantidad = cantidad;
        this.fechaVencimiento = fechaVencimiento;
        this.proveedor = proveedor;
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
    public String getProveedor() { return proveedor; }

    // Setter para cantidad
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    
}