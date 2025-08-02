package Model;

public class LineaVenta {
    private int id;
    private int cantidad;
    private double subtotal;
    private Producto producto;

    public LineaVenta(int id, int cantidad, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * producto.getPrecio();
    }

    // Getters
    public int getId() { return id; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }
    public Producto getProducto() { return producto; }

    public void setId(int id) {
        this.id = id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    
}