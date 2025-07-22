package com.monprojet.ClientLourd;

import javafx.application.Application;
import javafx.stage.Stage;


import javafx.scene.Scene;  // <-- ajoute ça

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView();
        Scene loginScene = loginView.createLoginScene();

        AuthController controller = new AuthController();
        controller.setup(loginView, primaryStage);

        primaryStage.setTitle("Connexion");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

