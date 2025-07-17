package com.monprojet.ClientLourd;
	
	import javafx.application.Application;
	import javafx.stage.Stage;
	import javafx.scene.Scene;
	import javafx.scene.control.Button;
	import javafx.scene.control.Label;
	import javafx.scene.control.TextField;
	import javafx.scene.layout.VBox;
	import javafx.stage.FileChooser;
	import java.io.File;

	public class MainApp extends Application {

	    private File fichierChoisi; // pour mémoriser le fichier choisi

	    @Override
	    public void start(Stage primaryStage) {
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
	            fichierChoisi = fileChooser.showOpenDialog(primaryStage);
	            if (fichierChoisi != null) {
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

	        // Mise en page (VBox = vertical)
	        VBox root = new VBox(10);
	        root.getChildren().addAll(btnJoindre, lblFichier, txtCle, btnSigner, lblResultat);

	        // Création et affichage de la fenêtre
	        Scene scene = new Scene(root, 400, 250);
	        primaryStage.setTitle("Signature Électronique");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	    }

	    public static void main(String[] args) {
	        launch(args); // Lance l'application JavaFX
	    }
	}


