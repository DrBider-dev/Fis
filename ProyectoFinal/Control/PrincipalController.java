package Control;

import Model.*;
import View.PrincipalView;

import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class PrincipalController {
    private PrincipalView vista;
    private Sistema sistema;
    private Venta ventaActual;
    private DefaultTableModel modeloTablaVenta;
    private DefaultTableModel modeloTablaInventario;
    Date dateVencimiento;
    String textoVencimiento;
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
        modeloTablaInventario = new DefaultTableModel(new Object[]{"ID", "Nombre", "Categoría", "Precio", "Cantidad", "Vencimiento", "Proveedor"}, 0) {
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

        // Evento para eliminar producto
        vista.getBtnEliminarProducto().addActionListener(e -> eliminarProducto());

        // Nuevos eventos para ventas
        vista.getBtnModificarVenta().addActionListener(e -> modificarCantidadVenta());
        vista.getBtnEliminarVenta().addActionListener(e -> eliminarProductoVenta());

        vista.getBtnActualizarHistorial().addActionListener(e -> cargarHistorialVentas());
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
            sistema.actualizarProducto(producto);
        }
        
        // Guardar venta
        sistema.agregarVenta(ventaActual);
        
        // Construir factura detallada
        StringBuilder factura = new StringBuilder();
        factura.append("=== AUTOSERVICIO LOS PAISAS ===\n");
        factura.append("Venta #").append(ventaActual.getId()).append("\n");
        factura.append("Fecha: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n\n");
        factura.append("Detalle de la venta:\n");
        factura.append("------------------------------------------------\n");
        
        // Agregar cada producto con su información
        for (LineaVenta linea : ventaActual.getLineasVenta()) {
            factura.append(String.format(
                "%-20s %4d x $%,9.2f   $%,9.2f\n",
                linea.getProducto().getNombre(),
                linea.getCantidad(),
                linea.getProducto().getPrecio(),
                linea.getSubtotal()
            ));
        }
        
        factura.append("------------------------------------------------\n");
        factura.append(String.format("TOTAL: $%,.2f", ventaActual.getTotal()));
        factura.append("\n\n¡Gracias por su compra!");
        
        // Mostrar factura en un diálogo
        JTextArea textArea = new JTextArea(factura.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 300));
        
        JOptionPane.showMessageDialog(
            vista, 
            scrollPane, 
            "Factura de Venta #" + ventaActual.getId(), 
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Reiniciar venta
        reiniciarVenta();
        actualizarTablaInventario();
        cargarHistorialVentas();
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
        
        // Crear formulario con los valores actuales del producto
        JTextField txtId = new JTextField(String.valueOf(producto.getId()));
        txtId.setEditable(false); // ID no editable
        
        JTextField txtNombre = new JTextField(producto.getNombre());
        JTextField txtCategoria = new JTextField(producto.getCategoria());
        JTextField txtPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        JTextField txtCantidad = new JTextField(String.valueOf(producto.getCantidad()));
        
        // Formatear fecha (asumiendo que tienes getFechaVencimiento())
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String fechaStr = producto.getFechaVencimiento() != null ? 
                        sdf.format(producto.getFechaVencimiento()) : "";
        JTextField txtVencimiento = new JTextField(fechaStr);
        
        JTextField txtProveedor = new JTextField(producto.getProveedor());
        
        Object[] campos = {
            "ID:", txtId,
            "Nombre:", txtNombre,
            "Categoría:", txtCategoria,
            "Precio:", txtPrecio,
            "Cantidad:", txtCantidad,
            "Vencimiento (dd-MM-yyyy):", txtVencimiento,
            "Proveedor:", txtProveedor
        };
        
        int opcion = JOptionPane.showConfirmDialog(
            vista, 
            campos, 
            "Editar Producto", 
            JOptionPane.OK_CANCEL_OPTION
        );
        
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                // Validar campos obligatorios
                if (txtNombre.getText().isEmpty() || 
                    txtCategoria.getText().isEmpty() || 
                    txtProveedor.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Parsear datos
                String nombre = txtNombre.getText();
                String categoria = txtCategoria.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String proveedor = txtProveedor.getText();
                
                // Parsear fecha
                Date fechaVen = null;
                if (!txtVencimiento.getText().isEmpty()) {
                    try {
                        fechaVen = sdf.parse(txtVencimiento.getText());
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(vista, "Formato de fecha inválido (dd-MM-yyyy)", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                
                // Actualizar el objeto producto
                producto.setNombre(nombre);
                producto.setCategoria(categoria);
                producto.setPrecio(precio);
                producto.setCantidad(cantidad);  // Reemplazar cantidad (no sumar)
                producto.setFechaVencimiento(fechaVen);
                producto.setProveedor(proveedor);
                
                // Guardar en base de datos
                sistema.actualizarProducto(producto);
                actualizarTablaInventario();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Valores numéricos inválidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void agregarNuevoProducto() {
        // Crear un formulario simple para nuevo producto
        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtCategoria = new JTextField();
        JTextField txtPrecio = new JTextField();
        JTextField txtCantidad = new JTextField();
        JTextField txtVencimiento = new JTextField();
        JTextField txtProveedor = new JTextField();
        
        Object[] campos = {
            "ID:", txtId,
            "Nombre:", txtNombre,
            "Categoría:", txtCategoria,
            "Precio:", txtPrecio,
            "Cantidad:", txtCantidad,
            "Vencimiento:", txtVencimiento,
            "Proveedor:", txtProveedor
        };
        
        int opcion = JOptionPane.showConfirmDialog(vista, campos, "Nuevo Producto", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try {
                int id = -1;
                if (!txtId.getText().isEmpty()) {
                    id = Integer.parseInt(txtId.getText());
                }
                String nombre = txtNombre.getText();
                String categoria = txtCategoria.getText();
                double precio = Double.parseDouble(txtPrecio.getText());
                int cantidad = Integer.parseInt(txtCantidad.getText());
                textoVencimiento = txtVencimiento.getText();
                String proveedor = txtProveedor.getText();
                if (id == -1 || nombre.isEmpty() || categoria.isEmpty() || textoVencimiento.isEmpty() || proveedor.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                
                try {
                    dateVencimiento = formatter.parse(textoVencimiento);  // Parsear el String Ven
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Inserte la Fecha en formato: dd-MM-yyyy");
                    e.printStackTrace();  // Manejo de excepciones si el formato es incorrecto
                }
                
                // Crear nuevo producto (sin fecha de vencimiento por ahora)
                Producto nuevoProducto = new Producto(id, nombre, categoria, precio, cantidad, dateVencimiento , proveedor);
                sistema.agregarProducto(nuevoProducto); // Esto guarda en la base de datos
                actualizarTablaInventario();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "Ingrese valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarProducto() {
        int filaSeleccionada = vista.getTablaInventario().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int idProducto = (int) vista.getTablaInventario().getValueAt(filaSeleccionada, 0);
        
        int confirm = JOptionPane.showConfirmDialog(
            vista, 
            "¿Está seguro de eliminar este producto?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            sistema.eliminarProducto(idProducto);
            actualizarTablaInventario();
        }
    }

    private void modificarCantidadVenta() {
        int filaSeleccionada = vista.getTablaVenta().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener datos de la fila seleccionada
        int idProducto = (int) vista.getTablaVenta().getValueAt(filaSeleccionada, 0);
        int cantidadActual = (int) vista.getTablaVenta().getValueAt(filaSeleccionada, 2);
        
        // Pedir nueva cantidad
        String input = JOptionPane.showInputDialog(
            vista, 
            "Ingrese la nueva cantidad:", 
            "Modificar Cantidad", 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (input == null || input.isEmpty()) return;
        
        try {
            int nuevaCantidad = Integer.parseInt(input);
            Producto producto = sistema.buscarProducto(idProducto);
            
            // Verificar disponibilidad
            int stockDisponible = producto.getCantidad() + cantidadActual; // Stock + lo ya reservado
            if (nuevaCantidad > stockDisponible) {
                JOptionPane.showMessageDialog(vista, 
                    "Stock insuficiente. Disponible: " + stockDisponible, 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Buscar la línea de venta correspondiente
            for (LineaVenta linea : ventaActual.getLineasVenta()) {
                if (linea.getProducto().getId() == idProducto) {
                    // Ajustar stock
                    producto.actualizarStock(cantidadActual - nuevaCantidad); // Diferencia
                    
                    // Actualizar línea de venta
                    linea.setCantidad(nuevaCantidad);
                    
                    // Actualizar tabla
                    vista.getTablaVenta().setValueAt(nuevaCantidad, filaSeleccionada, 2);
                    vista.getTablaVenta().setValueAt(linea.getSubtotal(), filaSeleccionada, 4);
                    
                    // Actualizar total
                    vista.getLblTotalVenta().setText(String.format("Total: $%.2f", ventaActual.getTotal()));
                    break;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProductoVenta() {
        int filaSeleccionada = vista.getTablaVenta().getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la venta", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtener datos de la fila seleccionada
        int idProducto = (int) vista.getTablaVenta().getValueAt(filaSeleccionada, 0);
        int cantidad = (int) vista.getTablaVenta().getValueAt(filaSeleccionada, 2);
        
        int confirm = JOptionPane.showConfirmDialog(
            vista, 
            "¿Eliminar este producto de la venta?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            Producto producto = sistema.buscarProducto(idProducto);
            
            // Devolver stock
            producto.actualizarStock(cantidad);
            
            // Eliminar línea de venta
            ventaActual.getLineasVenta().removeIf(linea -> linea.getProducto().getId() == idProducto);
            
            // Actualizar tabla
            ((DefaultTableModel) vista.getTablaVenta().getModel()).removeRow(filaSeleccionada);
            
            // Actualizar total
            vista.getLblTotalVenta().setText(String.format("Total: $%.2f", ventaActual.getTotal()));
        }
    }

        private void cargarHistorialVentas() {
        DefaultTableModel model = (DefaultTableModel) vista.getTablaHistorial().getModel();
        model.setRowCount(0);
        
        for (Venta venta : sistema.getVentas()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            model.addRow(new Object[]{
                venta.getId(),
                sdf.format(venta.getFecha()),
                String.format("$%,.2f", venta.getTotal())
            });
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
                producto.getFechaVencimiento() != null ? producto.getFechaVencimiento().toString() : "N/A",
                producto.getProveedor()
            });
        }
    }
}