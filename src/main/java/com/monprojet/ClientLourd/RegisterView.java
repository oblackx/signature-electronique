package com.monprojet.ClientLourd;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class RegisterView {
    private TextField nomField;
    private TextField prenomField;
    private TextField emailField;
    private PasswordField passwordField;
    private Button registerButton;
    private Button goToLoginButton;

    public Scene createRegisterScene(Runnable onRegister, Runnable onGoToLogin) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label title = new Label("Créer un compte");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        nomField = new TextField();
        nomField.setPromptText("Nom");

        prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        emailField = new TextField();
        emailField.setPromptText("Votre email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        registerButton = new Button("S'inscrire");
        registerButton.setOnAction(e -> onRegister.run());

        goToLoginButton = new Button("Se connecter");
        goToLoginButton.setOnAction(e -> onGoToLogin.run());

        grid.add(new Label("Nom :"), 0, 0);
        grid.add(nomField, 1, 0);

        grid.add(new Label("Prénom :"), 0, 1);
        grid.add(prenomField, 1, 1);

        grid.add(new Label("Email :"), 0, 2);
        grid.add(emailField, 1, 2);

        grid.add(new Label("Mot de passe :"), 0, 3);
        grid.add(passwordField, 1, 3);

        VBox root = new VBox(15, title, grid, registerButton, goToLoginButton);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        return new Scene(root, 400, 350);
    }

    public String getNom() {
        return nomField.getText();
    }

    public String getPrenom() {
        return prenomField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getGoToLoginButton() {
        return goToLoginButton;
    }
}
