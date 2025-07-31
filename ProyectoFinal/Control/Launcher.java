package Control;

import Control.LoginController;
import Model.Sistema;
import View.LoginView;

public class Launcher {
    public static void main(String[] args) {
        // Crear el sistema
        Sistema sistema = new Sistema();
        
        // Crear la vista de login
        LoginView loginView = new LoginView();
        
        // Crear el controlador de login
        LoginController loginController = new LoginController(loginView, sistema);
        
        // Mostrar la vista
        loginView.setVisible(true);
    }
}