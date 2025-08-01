package com.monprojet.ClientLourd;

import com.monprojet.ClientLourd.services.USBDirectoryDetector;
import com.monprojet.ClientLourd.services.SignatureManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.security.Security;
import java.util.Timer;
import java.util.TimerTask;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class MainApp extends Application {

    private File fichierChoisi;
    private Stage primaryStage;
    private File usbDrive;
    private Timer usbDetectionTimer;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginView();
    }

    private void showLoginView() {
        LoginView loginView = new LoginView();

        Scene loginScene = loginView.createLoginScene();
        loginScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        AuthController controller = new AuthController();
        controller.setup(loginView, primaryStage, this::showMainView);

        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Connexion");
        primaryStage.show();
    }

    private void showMainView() {
        Button btnJoindre = new Button("Joindre un fichier");
        Label lblFichier = new Label("Aucun fichier choisi");
        Label lblUSBStatus = new Label("Statut USB: Non connectée");
        Button btnSigner = new Button("Signer/Vérifier");
        Label lblResultat = new Label();

        btnJoindre.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un document à signer");
            File choisi = fileChooser.showOpenDialog(primaryStage);
            if (choisi != null) {
                fichierChoisi = choisi;
                lblFichier.setText("Fichier choisi : " + fichierChoisi.getName());
            }
        });

        btnSigner.setOnAction(e -> {
            if (fichierChoisi == null) {
                lblResultat.setText("⚠️ Veuillez choisir un fichier !");
                return;
            }

            if (usbDrive == null) {
                lblResultat.setText("⚠️ Aucune clé USB détectée !");
                return;
            }

            try {
                File certP7s = new File(usbDrive, "certifica.p7s");
                File pkcs12File = new File(usbDrive, "certificat.p12");

                if (!pkcs12File.exists()) {
                    lblResultat.setText("❌ 'certificat.p12' manquant sur la clé USB !");
                    return;
                }

                // 1. SIGNER le fichier
                File signatureOutput = new File(fichierChoisi.getParent(), fichierChoisi.getName() + ".p7s");
                SignatureManager.signDocumentDetached(fichierChoisi, pkcs12File, "0000", signatureOutput);

                // 2. VERIFIER la signature
                boolean isVerified = SignatureManager.verifyDocumentSignature(fichierChoisi, signatureOutput);

                lblResultat.setText(isVerified ?
                        "✅ Signature créée et vérifiée avec succès !" :
                        "⚠️ Signature générée, mais la vérification a échoué !");

            } catch (Exception ex) {
                ex.printStackTrace();
                lblResultat.setText("❌ Erreur : " + ex.getMessage());
            }
        });

        startUSBDetection(lblUSBStatus);

        VBox root = new VBox(10, btnJoindre, lblFichier, lblUSBStatus, btnSigner, lblResultat);
        Scene mainScene = new Scene(root, 500, 300);
        mainScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        btnJoindre.getStyleClass().add("button");
        btnSigner.getStyleClass().add("button");
        lblResultat.getStyleClass().add("result-label");
        lblUSBStatus.getStyleClass().add("usb-status");

        primaryStage.setTitle("Tableau de bord - Signature Électronique");
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }

    private void startUSBDetection(Label statusLabel) {
        usbDetectionTimer = new Timer(true);
        usbDetectionTimer.scheduleAtFixedRate(new TimerTask() {
            private File lastUSBDrive = null;

            @Override
            public void run() {
                File currentUSB = USBDirectoryDetector.findUSBDrive();

                if (currentUSB != null && !currentUSB.equals(lastUSBDrive)) {
                    lastUSBDrive = currentUSB;
                    usbDrive = currentUSB;
                    Platform.runLater(() ->
                        statusLabel.setText("Statut USB: Connectée (" + currentUSB.getAbsolutePath() + ")")
                    );
                } else if (currentUSB == null && lastUSBDrive != null) {
                    lastUSBDrive = null;
                    usbDrive = null;
                    Platform.runLater(() ->
                        statusLabel.setText("Statut USB: Non connectée")
                    );
                }
            }
        }, 0, 5000);
    }

    @Override
    public void stop() {
        if (usbDetectionTimer != null) {
            usbDetectionTimer.cancel();
        }
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        launch(args);
    }
}
