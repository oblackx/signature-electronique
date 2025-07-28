package com.monprojet.ClientLourd;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;

public class MainApp extends Application {

    private File fichierChoisi; // mémorise le fichier choisi
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    // -------------------- VUE LOGIN --------------------
 // -------------------- VUE LOGIN --------------------
    private void showLoginView() {
        LoginView loginView = new LoginView();

        // 1. نادينا على createLoginScene() باش يتصايبو ال Buttons
        Scene loginScene = loginView.createLoginScene();

        // 2. دابا loginBtn ماشي null، نقدر نستعمله
        AuthController controller = new AuthController();
        controller.setup(loginView, primaryStage, this::showMainView);

        // 3. عرض المشهد
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Connexion");
        primaryStage.show();
    }




    // -------------------- VUE SIGNATURE --------------------
    private void showMainView() {
        // Bouton pour joindre un fichier
        Button btnJoindre = new Button("Joindre un fichier");
        Label lblFichier = new Label("Aucun fichier choisi");

        // Champ pour la clé de signature
        TextField txtCle = new TextField();
        txtCle.setPromptText("Entrer la clé de signature");

        // Bouton pour signer
        Button btnSigner = new Button("Signer");
        Label lblResultat = new Label();

        // Action du bouton joindre fichier
        btnJoindre.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un document à signer");
            File choisi = fileChooser.showOpenDialog(primaryStage);
            if (choisi != null) {
                fichierChoisi = choisi;
                lblFichier.setText("Fichier choisi : " + fichierChoisi.getName());
            }
        });

        // Action du bouton signer
        btnSigner.setOnAction(e -> {
            String cle = txtCle.getText();
            if (fichierChoisi == null) {
                lblResultat.setText("⚠️ Veuillez choisir un fichier !");
            } else if (cle.isEmpty()) {
                lblResultat.setText("⚠️ Veuillez entrer une clé !");
            } else {
                // Ici plus tard tu feras la vraie signature et l'appel JSON
                lblResultat.setText("✅ Document signé avec succès !");
            }
        });

        VBox root = new VBox(10, btnJoindre, lblFichier, txtCle, btnSigner, lblResultat);
        Scene mainScene = new Scene(root, 400, 250);

        primaryStage.setTitle("Tableau de bord - Signature");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
