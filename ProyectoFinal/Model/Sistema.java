package Model;

import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private Usuario usuarioActual;
    private List<Producto> productos;
    private List<Venta> ventas;
    private int proximaIdVenta = 1;

    public Sistema() {
        this.productos = new ArrayList<>();
        this.ventas = new ArrayList<>();
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        // Productos de ejemplo
        productos.add(new Producto(1, "Arroz", "Alimentos", 2500, 50, null));
        productos.add(new Producto(2, "Leche", "Lácteos", 3200, 30, null));
        productos.add(new Producto(3, "Jabón", "Aseo", 1800, 40, null));
        productos.add(new Producto(4, "Café", "Bebidas", 4500, 25, null));
        productos.add(new Producto(5, "Atún", "Enlatados", 3000, 35, null));
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public Producto buscarProducto(int id) {
        for (Producto producto : productos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void agregarVenta(Venta venta) {
        ventas.add(venta);
    }

    public int getProximaIdVenta() {
        return proximaIdVenta++;
    }
}