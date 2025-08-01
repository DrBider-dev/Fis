package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:autoservicio.db";
    private static Database instance;
    private Connection connection;

    private Database() {
        try {
            connection = DriverManager.getConnection(URL);
            crearTablas();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void crearTablas() throws SQLException {
        String sqlProductos = "CREATE TABLE IF NOT EXISTS productos (" +
                "id INTEGER PRIMARY KEY," +
                "nombre TEXT NOT NULL," +
                "categoria TEXT," +
                "precio REAL NOT NULL," +
                "cantidad INTEGER NOT NULL," +
                "fecha_vencimiento TEXT)";

        String sqlVentas = "CREATE TABLE IF NOT EXISTS ventas (" +
                "id INTEGER PRIMARY KEY," +
                "fecha TEXT NOT NULL," +
                "total REAL NOT NULL)";

        String sqlLineasVenta = "CREATE TABLE IF NOT EXISTS lineas_venta (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "venta_id INTEGER NOT NULL," +
                "producto_id INTEGER NOT NULL," +
                "cantidad INTEGER NOT NULL," +
                "subtotal REAL NOT NULL," +
                "FOREIGN KEY(venta_id) REFERENCES ventas(id) ON DELETE CASCADE," +
                "FOREIGN KEY(producto_id) REFERENCES productos(id))";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON"); // Habilitar claves foráneas
            stmt.execute(sqlProductos);
            stmt.execute(sqlVentas);
            stmt.execute(sqlLineasVenta);
        }
    }
}