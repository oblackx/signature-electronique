package com.monprojet.ClientLourd;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        showLoginView(primaryStage);
    }

    private void showLoginView(Stage stage) {
        LoginView loginView = new LoginView();
        Scene loginScene = loginView.createLoginScene();

        AuthController authController = new AuthController();
        authController.setup(loginView, stage, () -> {
            showMainView(stage);
        });

        stage.setTitle("Connexion");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
    }

    private void showMainView(Stage stage) {
        MainView mainView = new MainView();
        Scene mainScene = mainView.createScene();

        stage.setTitle("Tableau de bord - " + Session.getNom());
        stage.setScene(mainScene);
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}