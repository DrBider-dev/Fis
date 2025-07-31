package Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venta {
    private int id;
    private Date fecha;
    private double total;
    private List<LineaVenta> lineasVenta;

    public Venta(int id) {
        this.id = id;
        this.fecha = new Date();
        this.lineasVenta = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarLineaVenta(LineaVenta lineaVenta) {
        lineasVenta.add(lineaVenta);
        total += lineaVenta.getSubtotal();
    }

    // Getters
    public int getId() { return id; }
    public Date getFecha() { return fecha; }
    public double getTotal() { return total; }
    public List<LineaVenta> getLineasVenta() { return lineasVenta; }
}