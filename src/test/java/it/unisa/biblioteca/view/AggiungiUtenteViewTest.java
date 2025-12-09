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
public class AggiungiUtenteViewTest {

    private AggiungiUtenteView view;

    /**
     * Configurazione Iniziale
     */
    @Start
    private void start(Stage stage) {
        // 1. Istanzio la view
        view = new AggiungiUtenteView();

        // 2. SIMULO LA LOGICA DEL CONTROLLER PER LA MATRICOLA
        // Replico la logica: lunghezza 10 e solo cifre -> VERDE, altrimenti -> ROSSO
        view.getTxtMatricola().textProperty().addListener((obs, old, nev) -> {
            int len = nev.length();
            view.getLblContatoreMatr().setText(len + " su 10");

            boolean isValid = (len == 10 && nev.matches("\\d+"));
            view.getLblContatoreMatr().setTextFill(isValid ? Color.GREEN : Color.RED);
        });

        // 3. Mostro la scena
        stage.setScene(new Scene(view));
        stage.show();
    }

    @Test
    void testInterazioneCompleta(FxRobot robot) {
        // --- FASE 1: Scrittura Campi Base ---

        // Nome
        robot.clickOn(view.getTxtNome()).write("Alessandro");
        assertEquals("Alessandro", view.getTxtNome().getText());

        // Cognome
        robot.clickOn(view.getTxtCognome()).write("Manzoni");
        assertEquals("Manzoni", view.getTxtCognome().getText());

        // Email
        robot.clickOn(view.getTxtEmail()).write("a.manzoni@studenti.unisa.it");
        assertEquals("a.manzoni@studenti.unisa.it", view.getTxtEmail().getText());


        // --- FASE 2: Validazione Matricola (Rosso) ---

        // Clicco sul campo matricola
        robot.clickOn(view.getTxtMatricola());

        // Scrivo una matricola troppo corta (5 cifre)
        robot.write("05121");

        // Verifico il contatore tramite getter
        Label lblContatore = view.getLblContatoreMatr();

        assertEquals("5 su 10", lblContatore.getText());
        assertEquals(Color.RED, lblContatore.getTextFill(), "Il contatore deve essere ROSSO se incompleto (5 cifre)");


        // --- FASE 3: Validazione Matricola (Verde) ---

        // Completo la matricola (aggiungo altre 5 cifre per arrivare a 10)
        robot.write("00001");

        // Verifico che il testo totale sia corretto
        assertEquals("0512100001", view.getTxtMatricola().getText());

        // Verifico il feedback visivo
        assertEquals("10 su 10", lblContatore.getText());
        assertEquals(Color.GREEN, lblContatore.getTextFill(), "Il contatore deve essere VERDE se corretto (10 cifre)");


        // --- FASE 4: Click bottoni ---

        // Clicco su Salva per verificare che sia interattivo
        robot.clickOn(view.getBtnSalva());
    }
}