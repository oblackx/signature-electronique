package com.monprojet.ClientLourd;

import javafx.stage.Stage;
import javafx.scene.control.Alert;

public class AuthController {
    private final AuthService authService = new AuthService();

    public void setup(LoginView view, Stage stage, Runnable onLoginSuccess) {
        view.getLoginButton().setOnAction(e -> {
            view.clearError();
            String email = view.getEmail();
            String password = view.getPassword();

            if (email.isEmpty() || password.isEmpty()) {
                view.showError("Email et mot de passe requis");
                return;
            }

            if (authService.authenticate(email, password)) {
                System.out.println("✅ Connexion réussie: Bienvenue, " + Session.getNom());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText("Bienvenue !");
                alert.setContentText("Bonjour " + Session.getNom() + ", connexion réussie.");
                alert.showAndWait();
            } else {
                view.showError("Email ou mot de passe incorrect");
            }
        });

        view.getRegisterButton().setOnAction(e -> {
            RegisterController registerController = new RegisterController();
            registerController.showRegister(stage, () -> {
                stage.setScene(view.createLoginScene());
                stage.setTitle("Connexion");
            });
        });
    }
}