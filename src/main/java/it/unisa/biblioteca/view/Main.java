package it.unisa.biblioteca;

import it.unisa.biblioteca.controller.BibliotecaController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Creo il controller e gli passo il "telecomando" (lo Stage)
        BibliotecaController controller = new BibliotecaController(primaryStage);

        // Dico al controller di partire
        controller.mostraHome();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}