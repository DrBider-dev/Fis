package Persistencia;

import Model.LineaVenta;
import Model.Producto;
import Model.Venta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager() {
        this.connection = Database.getInstance().getConnection();
    }

    // Operaciones para Productos
    public void guardarProducto(Producto producto) throws SQLException {
        String sql = "INSERT OR REPLACE INTO productos(id, nombre, categoria, precio, cantidad, fecha_vencimiento, proveedor) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, producto.getId());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getCategoria());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setInt(5, producto.getCantidad());
            
            if (producto.getFechaVencimiento() != null) {
                pstmt.setDate(6, new java.sql.Date(producto.getFechaVencimiento().getTime()));
            } else {
                pstmt.setNull(6, Types.DATE);
            }
            pstmt.setString(7, producto.getProveedor());
            
            pstmt.executeUpdate();
        }
    }

    public List<Producto> cargarProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getDate("fecha_vencimiento"),
                    rs.getString("proveedor")
                );
                productos.add(producto);
            }
        }
        return productos;
    }

    public void eliminarProducto(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Operaciones para Ventas
    public void guardarVenta(Venta venta) throws SQLException {
        // Guardar la venta principal
        String sqlVenta = "INSERT INTO ventas(id, fecha, total) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlVenta)) {
            pstmt.setInt(1, venta.getId());
            pstmt.setTimestamp(2, new Timestamp(venta.getFecha().getTime()));
            pstmt.setDouble(3, venta.getTotal());
            pstmt.executeUpdate();
        }
        
        // Guardar las líneas de venta
        String sqlLinea = "INSERT INTO lineas_venta(venta_id, producto_id, cantidad, subtotal) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sqlLinea)) {
            for (LineaVenta linea : venta.getLineasVenta()) {
                pstmt.setInt(1, venta.getId());
                pstmt.setInt(2, linea.getProducto().getId());
                pstmt.setInt(3, linea.getCantidad());
                pstmt.setDouble(4, linea.getSubtotal());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    public List<Venta> cargarVentas() throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Venta venta = new Venta(rs.getInt("id"));
                long fechaMillis = rs.getLong("fecha");
                if (!rs.wasNull()) {
                    Timestamp timestamp = new Timestamp(fechaMillis);
                    venta.setFecha(new java.util.Date(timestamp.getTime()));
                } else {
                    venta.setFecha(null);
                }
                
                venta.setTotal(rs.getDouble("total"));
                
                // Cargar líneas de venta para esta venta
                cargarLineasVenta(venta);
                
                ventas.add(venta);
            }
        }
        return ventas;
    }

    private void cargarLineasVenta(Venta venta) throws SQLException {
        String sql = "SELECT lv.id AS lv_id, lv.cantidad AS lv_cantidad, " +
             "p.id AS producto_id, p.nombre, p.categoria, p.precio, p.cantidad, p.fecha_vencimiento, " +
             "p.proveedor AS proveedor " +
             "FROM lineas_venta lv " +
             "JOIN productos p ON lv.producto_id = p.id " +
             "WHERE lv.venta_id = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, venta.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Crear producto
                    Producto producto = new Producto(
                        rs.getInt("producto_id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("cantidad"),
                        rs.getDate("fecha_vencimiento"),
                        rs.getString("proveedor")
                    );
                    
                    // Crear línea de venta
                    LineaVenta linea = new LineaVenta(
                        rs.getInt("lv_id"),
                        rs.getInt("lv_cantidad"),
                        producto
                    );
                    
                    venta.agregarLineaVenta(linea);
                }
            }
        }
    }

    public int obtenerProximaIdVenta() throws SQLException {
        String sql = "SELECT MAX(id) AS max_id FROM ventas";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt("max_id") + 1 : 1;
        }
    }

    public List<Venta> obtenerVentas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT id, fecha, total FROM ventas ORDER BY fecha DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta venta = new Venta(rs.getInt("id"));
                venta.setFecha(rs.getDate("fecha"));
                venta.setTotal(rs.getDouble("total"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener ventas", e);
        }
        return ventas;
    }
}