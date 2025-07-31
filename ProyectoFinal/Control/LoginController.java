package Control;

import Model.*;
import View.LoginView;
import View.PrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class LoginController {
    private LoginView vista;
    private Sistema sistema;

    public LoginController(LoginView vista, Sistema sistema) {
        this.vista = vista;
        this.sistema = sistema;
        
        // Configurar acción del botón de login
        vista.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticarUsuario();
            }
        });
    }

    private void autenticarUsuario() {
        String usuario = vista.txtUsuario.getText();
        String contrasena = vista.txtContrasena.getText();
        
        // Autenticación básica
        if (usuario.equals("admin") && contrasena.equals("admin123")) {
            sistema.setUsuarioActual(new Administrador(1, "Administrador", "ADMIN", "admin123"));
            abrirVistaPrincipal();
        } else if (usuario.equals("cajero") && contrasena.equals("cajero123")) {
            sistema.setUsuarioActual(new Cajero(2, "Cajero", "CAJERO", "cajero123"));
            abrirVistaPrincipal();
        } else {
            JOptionPane.showMessageDialog(vista, "Credenciales inválidas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirVistaPrincipal() {
        PrincipalView principalView = new PrincipalView();
        PrincipalController principalController = new PrincipalController(principalView, sistema);
        principalView.setVisible(true);
        vista.dispose();
    }
}