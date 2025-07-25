package com.monprojet.ClientLourd;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MainView {
    public Scene createScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + Session.getNom() + " !");
        Button logoutBtn = new Button("Déconnexion");

        logoutBtn.setOnAction(e -> {
            Session.clear();
            // Retour au login
        });

        root.getChildren().addAll(welcomeLabel, logoutBtn);
        return new Scene(root, 600, 400);
    }
}