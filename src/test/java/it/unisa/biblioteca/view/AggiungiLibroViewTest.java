package it.unisa.biblioteca.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AggiungiLibroViewTest {

    private AggiungiLibroView view;

    /**
     * Configurazione Iniziale (Simula il Main + parti del Controller)
     */
    @Start
    private void start(Stage stage) {
        // 1. Istanzio la view (come farebbe il controller)
        view = new AggiungiLibroView();

        // 2. SIMULO LA LOGICA DEL CONTROLLER
        // Siccome stiamo testando solo la View isolata, dobbiamo attaccare qui
        // la logica del listener per vedere se la view reagisce graficamente.
        view.getTxtIsbn().textProperty().addListener((obs, old, nev) -> {
            int len = nev.length();
            view.getLblContatoreIsbn().setText(len + " su 13");
            view.getLblContatoreIsbn().setTextFill((len == 13 && nev.matches("\\d+")) ? Color.GREEN : Color.RED);
        });

        // 3. Mostro la scena
        stage.setScene(new Scene(view));
        stage.show();
    }

    @Test
    void testInterazioneCompleta(FxRobot robot) {
        // --- FASE 1: Scrittura (Uso i GETTER per dire al robot dove scrivere) ---

        // Clicca sul campo titolo (preso dal getter) e scrivi
        robot.clickOn(view.getTxtTitolo()).write("Ingegneria Del Software");

        robot.clickOn(view.getTxtAutori()).write("Pippo, Paperino");


        // Verifica che il testo sia stato inserito
        assertEquals("Ingegneria Del Software", view.getTxtTitolo().getText());

        assertEquals("Pippo, Paperino", view.getTxtAutori().getText());


        // --- FASE 2: Validazione ISBN (Rosso) ---

        // Scrivo un ISBN troppo corto (5 cifre)
        robot.clickOn(view.getTxtIsbn()).write("12345");

        // Verifico il contatore tramite getter
        Label lblContatore = view.getLblContatoreIsbn();
        assertEquals("5 su 13", lblContatore.getText());
        assertEquals(Color.RED, lblContatore.getTextFill(), "Il contatore deve essere ROSSO se incompleto");


        // --- FASE 3: Validazione ISBN (Verde) ---

        // Completo l'ISBN (aggiungo altre 8 cifre per arrivare a 13)
        robot.write("67890123");

        // Verifico di nuovo
        assertEquals("13 su 13", lblContatore.getText());
        assertEquals(Color.GREEN, lblContatore.getTextFill(), "Il contatore deve essere VERDE se corretto");

        robot.clickOn(view.getDatePicker());


        robot.interact(() -> view.getDatePicker().setValue(java.time.LocalDate.now()));
        // --- FASE 4: Click bottoni ---
        // Clicco su Salva (non succederà nulla di logico perché non c'è il vero controller,
        // ma verifichiamo che il bottone sia cliccabile senza eccezioni)

        robot.clickOn(view.getTxtCopie()).write("2");

        assertEquals("2",view.getTxtCopie().getText());
        robot.clickOn(view.getBtnSalva());
    }
}