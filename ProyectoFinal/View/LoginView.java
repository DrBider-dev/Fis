package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class LoginView extends JFrame {

    public JButton btnLogin;
    public JLabel lblUsuario;
    public JLabel lblContrasena;
    public JPasswordField txtContrasena;
    public JTextField txtUsuario;

    public LoginView() {
        initComponents();
        setTitle("Autoservicio Los Paisas - Login");
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        // Panel principal con fondo
        JPanel panelFondo = new JPanel();
        panelFondo.setLayout(null);
        panelFondo.setBackground(new Color(245, 245, 245)); // Gris claro

        Font fuente = new Font("SansSerif", Font.BOLD, 14);
        Border bordeCampo = new LineBorder(new Color(200, 200, 200), 1, true);

        lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(fuente);
        lblUsuario.setBounds(30, 30, 80, 25);
        panelFondo.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(120, 30, 180, 30);
        txtUsuario.setFont(fuente);
        txtUsuario.setBorder(bordeCampo);
        txtUsuario.setName("txtUsuario");
        panelFondo.add(txtUsuario);

        lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(fuente);
        lblContrasena.setBounds(30, 80, 100, 25);
        panelFondo.add(lblContrasena);

        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(120, 80, 180, 30);
        txtContrasena.setFont(fuente);
        txtContrasena.setBorder(bordeCampo);
        txtContrasena.setName("txtContrasena");
        panelFondo.add(txtContrasena);

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(fuente);
        btnLogin.setBounds(120, 130, 140, 35);
        btnLogin.setBackground(new Color(33, 150, 243)); // Azul Material
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(new RoundedBorder(10));
        btnLogin.setName("btnLogin");
        panelFondo.add(btnLogin);

        getContentPane().add(panelFondo);
        setSize(new Dimension(370, 230));
    }

    // Clase para botón con bordes redondeados
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = 8;
            insets.top = insets.bottom = 4;
            return insets;
        }
    }

    // Getters
    public String getUsuario() {
        return txtUsuario.getText();
    }

    public String getContrasena() {
        return new String(txtContrasena.getPassword());
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    
}
