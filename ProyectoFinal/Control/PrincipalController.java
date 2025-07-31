package Control;

import Model.*;
import View.PrincipalView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PrincipalController {
    private PrincipalView vista;
    private Sistema sistema;
    private Venta ventaActual;
    private DefaultTableModel modeloTablaVenta;
    private DefaultTableModel modeloTablaInventario;

    public PrincipalController(PrincipalView vista, Sistema sistema) {
        this.vista = vista;
        this.sistema = sistema;
        
        inicializarVenta();
        configurarTablas();
        configurarEventos();
        actualizarTablaInventario();
    }

    private void inicializarVenta() {
        ventaActual = new Venta(sistema.getProximaIdVenta());
    }

    private void configurarTablas() {
        // Configurar tabla de venta
        modeloTablaVenta = new DefaultTableModel(new Object[]{"ID", "Producto", "Cantidad", "Precio", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTablaVenta().setModel(modeloTablaVenta);
        
        // Configurar tabla de inventario
        modeloTablaInventario = new DefaultTableModel(new Object[]{"ID", "Nombre", "Categoría", "Precio", "Cantidad", "Vencimiento"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vista.getTablaInventario().setModel(modeloTablaInventario);
    }

    private void configurarEventos() {
        // Evento para agregar producto a la venta
        vista.getBtnAgregarProducto().addActionListener(e -> agregarProductoVenta());
        
        // Evento para finalizar venta
        vista.getBtnFinalizarVenta().addActionListener(e -> finalizarVenta());
        
        // Evento para actualizar stock
        vista.getBtnActualizarStock().addActionListener(e -> actualizarStock());
        
        // Evento para agregar nuevo producto
        vista.getBtnAgregarProductoInv().addActionListener(e -> agregarNuevoProducto());
    }

    private void agregarProductoVenta() {
        try {
            int idProducto = Integer.parseInt(vista.getTxtProductoId().getText());
            int cantidad = Integer.parseInt(vista.getTxtCantidad().getText());
            
            Producto producto = sistema.buscarProducto(idProducto);
            if (producto == null) {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (producto.getCantidad() < cantidad) {
                JOptionPane.showMessageDialog(vista, "Stock insuficiente", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Crear línea de venta
            LineaVenta linea = new LineaVenta(ventaActual.getLineasVenta().size() + 1, cantidad, producto);
            ventaActual.agregarLineaVenta(linea);
            
            // Actualizar tabla
            modeloTablaVenta.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                cantidad,
                producto.getPrecio(),
                linea.getSubtotal()
            });
            
            // Actualizar total
            vista.getLblTotalVenta().setText(String.format("Total: $%.2f", ventaActual.getTotal()));
            
            // Limpiar campos
            vista.getTxtProductoId().setText("");
            vista.getTxtCantidad().setText("");
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarVenta() {
        if (ventaActual.getLineasVenta().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay productos en la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Actualizar inventario
        for (LineaVenta linea : ventaActual.getLineasVenta()) {
            Producto producto = linea.getProducto();
            producto.actualizarStock(-linea.getCantidad());
        }
        
        // Guardar venta
        sistema.agregarVenta(ventaActual);
        
        // Mostrar resumen
        JOptionPane.showMessageDialog(vista, 
            String.format("Venta finalizada!\nTotal: $%.2f", ventaActual.getTotal()),
            "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
        
        // Reiniciar venta
        reiniciarVenta();
        actualizarTablaInventario();
    }

    private void reiniciarVenta() {
        ventaActual = new Venta(sistema.getProximaIdVenta());
        modeloTablaVenta.setRowCount(0);
        vista.getLblTotalVenta().setText("Total: $0.00");
    }

    private void actualizarStock() {
        int filaSeleccionada = vista.getTablaInventario().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idProducto = (int) vista.getTablaInventario().getValueAt(filaSeleccionada, 0);
        Producto producto = sistema.buscarProducto(idProducto);
        
        String input = JOptionPane.showInputDialog(vista, "Ingrese la cantidad a añadir:", "Actualizar Stock", JOptionPane.QUESTION_MESSAGE);
        if (input == null || input.isEmpty()) return;
        
        try {
            int cantidad = Integer.parseInt(input);
            producto.actualizarStock(cantidad);
            actualizarTablaInventario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarNuevoProducto() {
        // Crear un formulario simple para nuevo producto
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCategoria = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();
        
        Object[] campos = {
            "ID:", txtId,
            "Nombre:", txtNombre,
            "Categoría:", txtCategoria,
            "Precio:", txtPrecio,
            "Cantidad:", txtCantidad
        };
        
        int opcion = JOptionPane.showConfirmDialog(vista, campos, "Nuevo Producto", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(txtId.getText());
                String nombre = txtNombre.getText();
                String categoria = txtCategoria.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int cantidad = Integer.parseInt(txtCantidad.getText());
                
                // Crear nuevo producto (sin fecha de vencimiento por ahora)
                Producto nuevoProducto = new Producto(id, nombre, categoria, precio, cantidad, null);
                sistema.agregarProducto(nuevoProducto);
                actualizarTablaInventario();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarTablaInventario() {
        modeloTablaInventario.setRowCount(0);
        for (Producto producto : sistema.getProductos()) {
            modeloTablaInventario.addRow(new Object[]{
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getFechaVencimiento() != null ? producto.getFechaVencimiento().toString() : "N/A"
            });
        }
    }
}