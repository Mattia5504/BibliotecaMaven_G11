package it.unisa.biblioteca.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /// --- SEZIONE AVVIO JAVAFX --- ///

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carico la View dal file FXML
            // Nota lo slash iniziale "/" che dice a Java: "parti dalla radice delle risorse"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unisa/biblioteca/view/BibliotecaView.fxml"));            Parent root = loader.load();

            // Configuro la finestra (Stage)
            primaryStage.setTitle("Biblioteca Ingegneria - UniSa");

            // Imposto la scena (Scene) con dimensioni preferite
            primaryStage.setScene(new Scene(root, 900, 600));

            // Impedisco che la finestra diventi troppo piccola rompendo il layout
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(400);

            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Errore critico nel caricamento dell'interfaccia FXML.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}