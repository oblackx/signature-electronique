package com.monprojet.ClientLourd;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class RegisterController {
    private final AuthService authService = new AuthService();
    private final RegisterView registerView = new RegisterView();

    public void showRegister(Stage stage, Runnable showLoginScreen) {
        stage.setScene(registerView.createRegisterScene(() -> {
            String nom = registerView.getNom();
            String prenom = registerView.getPrenom();
            String email = registerView.getEmail();
            String password = registerView.getPassword();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            boolean success = authService.registerUser(nom, prenom, email, password);

            if (success) {
                showAlert("Succès", "Compte créé avec succès !");
                showLoginScreen.run(); // Retour à l’écran de login
            } else {
                showAlert("Erreur", "Email déjà utilisé ou erreur serveur.");
            }
        }, showLoginScreen));

        stage.setTitle("Créer un compte");
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

