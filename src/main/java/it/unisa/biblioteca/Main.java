package it.unisa.biblioteca;

import it.unisa.biblioteca.controller.BibliotecaController;
import javafx.application.Application;
import javafx.stage.Stage;

//la classe principale che permette al programma di partire
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inizializzo il controller passandogli lo Stage (la finestra principale)
        // Il controller farà da "regista" cambiando le scene.
        BibliotecaController controller = new BibliotecaController(primaryStage);

        // Dico al controller di mostrare la schermata Home
        controller.mostraHome();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}