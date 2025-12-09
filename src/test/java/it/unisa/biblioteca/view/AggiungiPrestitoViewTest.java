package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

// Estensione necessaria per JUnit 5 + TestFX
@ExtendWith(ApplicationExtension.class)
public class AggiungiPrestitoViewTest {

    private AggiungiPrestitoView view;

    /**
     * SETUP MINIMALE: Solo dati e visualizzazione.
     * Nessuna logica di controller, nessun filtro.
     */
    @Start
    public void start(Stage stage) {
        // 1. Creo 1 Utente
        ObservableList<Utente> utenti = FXCollections.observableArrayList(
                new Utente("Mario", "Rossi", "0512100001", "m.rossi@test.it")
        );

        // 2. Creo 1 Libro
        ObservableList<Libro> libri = FXCollections.observableArrayList(
                new Libro("Libro Test", Arrays.asList("Autore X"), LocalDate.now(), "9788800000001", 5)
        );

        // 3. Istanzio la View
        view = new AggiungiPrestitoView(utenti, libri);

        // 4. Mostro la finestra
        stage.setScene(new Scene(view, 800, 600));
        stage.show();
        stage.toFront(); // Porta la finestra in primo piano
    }

    @Test
    void testClickSemplice(FxRobot robot) {
        // 1. Clicco sull'utente "Rossi" (TestFX cerca il testo nella cella)
        robot.clickOn("Rossi");

        // 2. Clicco sul libro "Libro Test"
        robot.clickOn("Libro Test");

        // 3. Preparo una "trappola" per vedere se il bottone funziona
        // Uso AtomicBoolean perché è thread-safe tra il test e l'interfaccia
        AtomicBoolean bottoneCliccato = new AtomicBoolean(false);

        // Assegno l'azione al bottone (devo farlo con interact perché modifico la UI)
        robot.interact(() -> {
            view.getBtnSalva().setOnAction(e -> bottoneCliccato.set(true));
        });

        // 4. Clicco fisicamente sul bottone Salva
        robot.clickOn(view.getBtnSalva());

        // 5. Verifico che l'azione sia partita
        Assertions.assertTrue(bottoneCliccato.get(), "Il click sul bottone 'Conferma Prestito' non è stato rilevato.");
    }
}