package View;

import javax.swing.*;
import java.awt.*;

public class PrincipalView extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel panelVentas;
    private JPanel panelInventario;

    // Componentes de ventas
    private JTextField txtProductoId;
    private JTextField txtCantidad;
    private JButton btnAgregarProducto;
    private JTable tablaVenta;
    private JButton btnFinalizarVenta;
    private JLabel lblTotalVenta;

    // Componentes de inventario
    private JTable tablaInventario;
    private JButton btnActualizarStock;
    private JButton btnAgregarProductoInv;

    public PrincipalView() {
        setTitle("Autoservicio Los Paisas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Panel de Ventas
        panelVentas = new JPanel(new BorderLayout());
        JPanel panelSuperiorVentas = new JPanel(new GridLayout(1, 3, 10, 10));
        
        panelSuperiorVentas.add(new JLabel("ID Producto:"));
        txtProductoId = new JTextField();
        panelSuperiorVentas.add(txtProductoId);
        
        panelSuperiorVentas.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelSuperiorVentas.add(txtCantidad);
        
        btnAgregarProducto = new JButton("Agregar Producto");
        panelSuperiorVentas.add(btnAgregarProducto);
        
        panelVentas.add(panelSuperiorVentas, BorderLayout.NORTH);
        
        // Tabla de venta
        tablaVenta = new JTable();
        JScrollPane scrollVenta = new JScrollPane(tablaVenta);
        panelVentas.add(scrollVenta, BorderLayout.CENTER);
        
        // Panel inferior ventas
        JPanel panelInferiorVentas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalVenta = new JLabel("Total: $0.00");
        panelInferiorVentas.add(lblTotalVenta);
        
        btnFinalizarVenta = new JButton("Finalizar Venta");
        panelInferiorVentas.add(btnFinalizarVenta);
        
        panelVentas.add(panelInferiorVentas, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Ventas", panelVentas);
        
        // Panel de Inventario
        panelInventario = new JPanel(new BorderLayout());
        
        tablaInventario = new JTable();
        JScrollPane scrollInventario = new JScrollPane(tablaInventario);
        panelInventario.add(scrollInventario, BorderLayout.CENTER);
        
        JPanel panelBotonesInventario = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAgregarProductoInv = new JButton("Agregar Producto");
        btnActualizarStock = new JButton("Actualizar Stock");
        
        panelBotonesInventario.add(btnAgregarProductoInv);
        panelBotonesInventario.add(btnActualizarStock);
        
        panelInventario.add(panelBotonesInventario, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Inventario", panelInventario);
        
        add(tabbedPane);
    }

    // Getters para componentes de ventas
    public JTextField getTxtProductoId() { return txtProductoId; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JButton getBtnAgregarProducto() { return btnAgregarProducto; }
    public JTable getTablaVenta() { return tablaVenta; }
    public JButton getBtnFinalizarVenta() { return btnFinalizarVenta; }
    public JLabel getLblTotalVenta() { return lblTotalVenta; }

    // Getters para componentes de inventario
    public JTable getTablaInventario() { return tablaInventario; }
    public JButton getBtnActualizarStock() { return btnActualizarStock; }
    public JButton getBtnAgregarProductoInv() { return btnAgregarProductoInv; }
}