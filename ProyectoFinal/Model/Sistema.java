package Model;

import Persistencia.DatabaseManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private Usuario usuarioActual;
    private List<Producto> productos;
    private List<Venta> ventas;
    private DatabaseManager dbManager;

    public Sistema() {
        this.dbManager = new DatabaseManager();
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            productos = dbManager.cargarProductos();
            ventas = dbManager.cargarVentas();
            
            // Si no hay productos, cargar datos iniciales
            if (productos.isEmpty()) {
                cargarDatosIniciales();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // En caso de error, cargar datos iniciales en memoria
            productos = new ArrayList<>();
            ventas = new ArrayList<>();
            cargarDatosIniciales();
        }
    }

    private void cargarDatosIniciales() {
        // Productos de ejemplo
        productos.add(new Producto(1, "Arroz", "Alimentos", 2500, 50, null));
        productos.add(new Producto(2, "Leche", "Lácteos", 3200, 30, null));
        productos.add(new Producto(3, "Jabón", "Aseo", 1800, 40, null));
        productos.add(new Producto(4, "Café", "Bebidas", 4500, 25, null));
        productos.add(new Producto(5, "Atún", "Enlatados", 3000, 35, null));
        
        // Guardar productos iniciales en la base de datos
        for (Producto producto : productos) {
            try {
                dbManager.guardarProducto(producto);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
        try {
            dbManager.guardarProducto(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            dbManager.guardarVenta(venta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getProximaIdVenta() {
        try {
            return dbManager.obtenerProximaIdVenta();
        } catch (SQLException e) {
            e.printStackTrace();
            return ventas.size() + 1;
        }
    }
    
    public void actualizarProducto(Producto producto) {
        try {
            dbManager.guardarProducto(producto);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}