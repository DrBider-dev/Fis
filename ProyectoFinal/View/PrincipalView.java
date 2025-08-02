package View;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

public class PrincipalView extends JFrame {

    public JButton btnActualizarStock;
    public JButton btnAgregarProducto;
    public JButton btnAgregarProductoInv;
    public JButton btnFinalizarVenta;
    public JButton btnEliminarProducto;
    public JButton btnModificarVenta;
    public JButton btnEliminarVenta;
    public JLabel lblTotalVenta;
    public JPanel panelInventario;
    public JPanel panelVentas;
    public JTabbedPane tabbedPane;
    public JTable tablaInventario;
    public JTable tablaVenta;
    public JTextField txtCantidad;
    public JTextField txtProductoId;

    public JPanel panelHistorial; // Nuevo panel para historial
    public JTable tablaHistorial; // Nueva tabla para historial
    public JButton btnActualizarHistorial; // Botón para refrescar historial

    public PrincipalView() {
        // Apply FlatLaf look-and-feel for a modern UI
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        initComponents();
        setTitle("Autoservicio Los Paisas");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        panelVentas = new JPanel();
        txtProductoId = new JTextField();
        txtCantidad = new JTextField();
        btnAgregarProducto = new JButton();
        JScrollPane scrollVenta = new JScrollPane();
        tablaVenta = new JTable();
        lblTotalVenta = new JLabel();
        btnFinalizarVenta = new JButton();
        panelInventario = new JPanel();
        JScrollPane scrollInventario = new JScrollPane();
        tablaInventario = new JTable();
        btnAgregarProductoInv = new JButton();
        btnActualizarStock = new JButton();

        // Window styling
        getContentPane().setBackground(Color.WHITE);
        getContentPane().setLayout(new BorderLayout());

        // Tab styling
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- VENTAS TAB ---
        panelVentas.setLayout(new BorderLayout(10, 10));
        panelVentas.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Top input panel
        JPanel inputVentas = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        inputVentas.setOpaque(false);
        inputVentas.add(createLabel("ID Producto:"));
        txtProductoId.setPreferredSize(new Dimension(100, 25));
        inputVentas.add(txtProductoId);
        inputVentas.add(createLabel("Cantidad:"));
        txtCantidad.setPreferredSize(new Dimension(60, 25));
        inputVentas.add(txtCantidad);
        styleButton(btnAgregarProducto, "Agregar");
        inputVentas.add(btnAgregarProducto);
        panelVentas.add(inputVentas, BorderLayout.NORTH);

        // Table Venta
        tablaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Descripción", "Cantidad", "Precio", "Subtotal"}
        ));
        styleTable(tablaVenta);
        scrollVenta.setViewportView(tablaVenta);
        panelVentas.add(scrollVenta, BorderLayout.CENTER);

        // Bottom panel with total and finish
        JPanel bottomVentas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomVentas.setOpaque(false);
        lblTotalVenta.setText("Total: $0.00");
        lblTotalVenta.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomVentas.add(lblTotalVenta);
        styleButton(btnFinalizarVenta, "Finalizar");
        bottomVentas.add(btnFinalizarVenta);
        panelVentas.add(bottomVentas, BorderLayout.SOUTH);

        tabbedPane.addTab("Ventas", panelVentas);

        // --- INVENTARIO TAB ---
        panelInventario.setLayout(new BorderLayout(10, 10));
        panelInventario.setBorder(new EmptyBorder(10, 10, 10, 10));

        tablaInventario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Descripción", "Stock", "Precio"}
        ));
        styleTable(tablaInventario);
        scrollInventario.setViewportView(tablaInventario);
        panelInventario.add(scrollInventario, BorderLayout.CENTER);

        JPanel bottomInv = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomInv.setOpaque(false);
        styleButton(btnAgregarProductoInv, "Agregar");
        bottomInv.add(btnAgregarProductoInv);
        styleButton(btnActualizarStock, "Actualizar");
        bottomInv.add(btnActualizarStock);
        panelInventario.add(bottomInv, BorderLayout.SOUTH);

        tabbedPane.addTab("Inventario", panelInventario);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        pack();
        setSize(new Dimension(820, 620));

        btnEliminarProducto = new JButton();
        styleButton(btnEliminarProducto, "Eliminar");
        bottomInv.add(btnEliminarProducto);

        panelInventario.add(bottomInv, BorderLayout.SOUTH);

        btnModificarVenta = new JButton();
        styleButton(btnModificarVenta, "Modificar Cantidad");
        bottomVentas.add(btnModificarVenta);

        btnEliminarVenta = new JButton();
        styleButton(btnEliminarVenta, "Eliminar Producto");
        bottomVentas.add(btnEliminarVenta);
        
        panelVentas.add(bottomVentas, BorderLayout.SOUTH);


        // --- NUEVO TAB: HISTORIAL DE VENTAS ---
        panelHistorial = new JPanel(new BorderLayout(10, 10));
        panelHistorial.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Configurar tabla de historial
        tablaHistorial = new JTable();
        tablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID Venta", "Fecha", "Total"}
        ));
        styleTable(tablaHistorial);
        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        panelHistorial.add(scrollHistorial, BorderLayout.CENTER);
        
        // Botón para actualizar historial
        JPanel bottomHistorial = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        bottomHistorial.setOpaque(false);
        btnActualizarHistorial = new JButton();
        styleButton(btnActualizarHistorial, "Actualizar Historial");
        bottomHistorial.add(btnActualizarHistorial);
        panelHistorial.add(bottomHistorial, BorderLayout.SOUTH);
        
        // Agregar pestaña al tabbedPane
        tabbedPane.addTab("Historial", panelHistorial);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private void styleButton(JButton btn, String text) {
        btn.setText(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    private void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);
    }

    // Getters for MVC
    public JTextField getTxtProductoId() { return txtProductoId; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JButton getBtnAgregarProducto() { return btnAgregarProducto; }
    public JTable getTablaVenta() { return tablaVenta; }
    public JButton getBtnFinalizarVenta() { return btnFinalizarVenta; }
    public JLabel getLblTotalVenta() { return lblTotalVenta; }
    public JTable getTablaInventario() { return tablaInventario; }
    public JButton getBtnActualizarStock() { return btnActualizarStock; }
    public JButton getBtnAgregarProductoInv() { return btnAgregarProductoInv; }
    public JButton getBtnEliminarProducto() { return btnEliminarProducto; }
    public JButton getBtnModificarVenta() { return btnModificarVenta; }
    public JButton getBtnEliminarVenta() { return btnEliminarVenta; }

    /**
     * Setter para actualizar el modelo de la tabla de ventas desde el controlador
     */
    public void setModeloTablaVenta(TableModel model) {
        tablaVenta.setModel(model);
    }

    public JTable getTablaHistorial() {
        return tablaHistorial;
    }
    
    public JButton getBtnActualizarHistorial() {
        return btnActualizarHistorial;
    }
    
    // Setter para el modelo de la tabla de historial
    public void setModeloTablaHistorial(TableModel model) {
        tablaHistorial.setModel(model);
    }
    
}
