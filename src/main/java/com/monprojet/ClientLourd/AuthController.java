package com.monprojet.ClientLourd;

import javafx.stage.Stage;

public class AuthController {
    private final AuthService authService = new AuthService();

    public void setup(LoginView view, Stage stage) {
        view.getLoginButton().setOnAction(e -> {
            String email = view.getEmail();
            String password = view.getPassword();
            boolean isAuthenticated = authService.authenticate(email, password);

            if (isAuthenticated) {
                System.out.println("Connexion réussie !");
                // TODO: Redirection vers l’application principale
            } else {
                System.out.println("Échec de l’authentification.");
            }
        });

        view.getRegisterButton().setOnAction(e -> {
            RegisterController registerController = new RegisterController();
            registerController.showRegister(stage, () -> {
                // Retour à la vue login
                stage.setScene(view.createLoginScene());
                stage.setTitle("Connexion");
            });
        });
    }
}
