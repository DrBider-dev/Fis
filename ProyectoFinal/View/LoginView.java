package View;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    public LoginView() {
        setTitle("Autoservicio Los Paisas - Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        panel.add(txtUsuario);
        
        panel.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panel.add(txtContrasena);
        
        btnLogin = new JButton("Iniciar Sesión");
        panel.add(btnLogin);
        
        add(panel);
    }

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