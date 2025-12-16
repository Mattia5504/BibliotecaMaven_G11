package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel; // TRUCCO PER JAVA 8
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BibliotecaControllerTest {

    private BibliotecaController controller;
    private Stage mockStage;
    private GestoreFile mockGestoreFile;

    private static final String FILE_NAME = "database_biblioteca.dat";


    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    @BeforeEach
    void setUp() throws Exception {
        // 1. Pulizia preventiva del file database
        File dbFile = new File(FILE_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        // 2. Mock dello Stage (La "finestra" finta)
        // Usiamo Mockito per non far aprire finestre vere durante i test
        mockStage = mock(Stage.class);
        Scene mockScene = mock(Scene.class);

        // Evitiamo NullPointerException se il controller chiede la scena
        lenient().when(mockStage.getScene()).thenReturn(mockScene);

        // 3. Eseguiamo la creazione del controller nel thread JavaFX
        // Necessario perché il controller istanzia componenti grafici (View)
        runOnJavaFXThread(() -> {
            controller = new BibliotecaController(mockStage);
        });

        // 4. Inject del Mock GestoreFile
        // Sostituiamo il gestore file reale con uno finto per controllare i salvataggi
        mockGestoreFile = mock(GestoreFile.class);
        setPrivateField(controller, "gestoreFile", mockGestoreFile);
    }

    @AfterEach
    void tearDown() {
        // Pulizia finale
        File dbFile = new File(FILE_NAME);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testInizializzazioneDatiDiProva() throws Exception {
        // Verifica che, se non c'è il file, vengano creati i dati di default

        ObservableList<Libro> catalogo = getPrivateField(controller, "catalogo");
        ObservableList<Utente> anagrafica = getPrivateField(controller, "anagrafica");

        // Controllo i numeri definiti nel metodo inizializzaDatiProva
        assertNotNull(catalogo);
        assertEquals(40, catalogo.size(), "Dovrebbero essere generati 40 libri di default");
        assertEquals(30, anagrafica.size(), "Dovrebbero essere generati 30 utenti di default");
    }

    @Test
    void testNavigazioneHome() {
        runOnJavaFXThread(() -> controller.mostraHome());
        // Verifica che il titolo sia cambiato, segno che la vista è stata caricata
        verify(mockStage, atLeastOnce()).setTitle(contains("Home"));
    }

    @Test
    void testNavigazioneLibri() {
        runOnJavaFXThread(() -> controller.mostraLibri());
        verify(mockStage, atLeastOnce()).setTitle(contains("Libri"));
    }

    @Test
    void testNavigazioneUtenti() {
        runOnJavaFXThread(() -> controller.mostraUtenti());
        verify(mockStage, atLeastOnce()).setTitle(contains("Utenti"));
    }

    @Test
    void testNavigazionePrestiti() {
        runOnJavaFXThread(() -> controller.mostraPrestiti());
        verify(mockStage, atLeastOnce()).setTitle(contains("Prestiti"));
    }

    @Test
    void testSalvataggioAllaChiusura() {
        // Verifica che il controller abbia impostato un listener per la chiusura
        ArgumentCaptor<EventHandler<WindowEvent>> captor = ArgumentCaptor.forClass(EventHandler.class);
        verify(mockStage).setOnCloseRequest(captor.capture());

        EventHandler<WindowEvent> closeHandler = captor.getValue();
        assertNotNull(closeHandler, "Il gestore di chiusura deve essere impostato");

        // Simuliamo manualmente il click sulla "X" della finestra
        runOnJavaFXThread(() -> {
            closeHandler.handle(new WindowEvent(mockStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });

        // Verifichiamo che il metodo salvaTutto sia stato chiamato sul gestore file finto
        verify(mockGestoreFile, times(1)).salvaTutto(any(), any(), any());
    }

    @Test
    void testAggiuntaLibro() throws Exception {
        // Test di integrazione logica: aggiungiamo un libro e verifichiamo che sia nel catalogo
        runOnJavaFXThread(() -> {
            // Simuliamo l'apertura della vista
            controller.mostraAggiungiLibro();

        });

        ObservableList<Libro> catalogo = getPrivateField(controller, "catalogo");
        int sizeIniziale = catalogo.size();

        // Aggiunta manuale simulata
        runOnJavaFXThread(() -> {
            catalogo.add(new Libro("Nuovo Libro Test", java.util.Arrays.asList("Autore Test"), java.time.LocalDate.now(), "9788812345678", 5));
        });

        assertEquals(sizeIniziale + 1, catalogo.size());
        assertEquals("Nuovo Libro Test", catalogo.get(sizeIniziale).getTitolo());
    }

    // --------------------------------------------------------------------------------
    // UTILITIES
    // --------------------------------------------------------------------------------

    /**
     * Esegue codice nel thread JavaFX e aspetta che finisca.
     * Necessario per interagire con componenti grafici o ObservableList attive.
     */
    private void runOnJavaFXThread(Runnable action) {
        if (Platform.isFxApplicationThread()) {
            action.run();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                try {
                    action.run();
                } finally {
                    latch.countDown();
                }
            });
            try {
                latch.await(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException("Timeout waiting for JavaFX", e);
            }
        }
    }

    /**
     * Reflection per leggere campi privati (catalogo, anagrafica, ecc.) che altrimenti non sarebbero accessibili
     */
    @SuppressWarnings("unchecked")
    private <T> T getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(target);
    }

    /**
     * Reflection per scrivere campi privati (mock injection) che altrimenti non sarebbero visibili
     */
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}