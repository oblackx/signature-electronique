package com.monprojet.ClientLourd;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginView {
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginBtn;
    private Button registerBtn;
    private Label errorLabel;

    public Scene createLoginScene() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25));

        Label title = new Label("Authentification");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        emailField = new TextField();
        emailField.setPromptText("votre@email.com");

        passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(passwordField, 1, 1);

        loginBtn = new Button("Se connecter");
        registerBtn = new Button("Créer un compte");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-underline: true;");

        errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setVisible(false);

        VBox root = new VBox(20, title, grid, errorLabel, loginBtn, registerBtn);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        return new Scene(root, 400, 350);
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    public void clearError() {
        errorLabel.setVisible(false);
    }

    // Getters
    public String getEmail() { return emailField.getText(); }
    public String getPassword() { return passwordField.getText(); }
    public Button getLoginButton() { return loginBtn; }
    public Button getRegisterButton() { return registerBtn; }
}