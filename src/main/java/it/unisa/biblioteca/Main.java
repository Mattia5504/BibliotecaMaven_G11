package it.unisa.biblioteca;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Creiamo una semplice etichetta
        Label benvenuto = new Label("Setup del Gruppo Completato!");

        // Creiamo un pulsante
        Button btnTest = new Button("Clicca per testare la console");

        // Azione del pulsante (Java 8 Lambda)
        btnTest.setOnAction(e -> {
            System.out.println("Tutto funziona correttamente! Siamo pronti a sviluppare.");
            benvenuto.setText("Test Superato ✅");
        });

        // Layout verticale (uno sotto l'altro)
        VBox root = new VBox(20); // 20 è lo spazio tra gli elementi
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(benvenuto, btnTest);

        // Scena e Stage
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Library Manager - Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}