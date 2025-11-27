package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class BibliotecaController {

    /// --- SEZIONE DATI (MODEL VIEW) --- ///
    // Liste osservabili: se aggiungi un elemento qui, la tabella si aggiorna da sola.
    // Al momento sono vuote.
    private ObservableList<Libro> catalogoLibri = FXCollections.observableArrayList();
    private ObservableList<Utente> anagraficaUtenti = FXCollections.observableArrayList();


    /// --- SEZIONE RIFERIMENTI FXML --- ///
    // Questi devono combaciare con i fx:id nel file .fxml

    // Tabella Libri
    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colDisp;

    // Tabella Utenti
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colEmail;

    // Sezione Prestiti
    @FXML private ComboBox<Utente> comboUtenti;
    @FXML private ComboBox<Libro> comboLibri;
    @FXML private Button btnRegistraPrestito;
    @FXML private Label lblMessaggio; // Label per feedback errori/successi


    /// --- INIZIALIZZAZIONE --- ///
    @FXML
    public void initialize() {
        // 1. Configuro le colonne della tabella Libri
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        // 2. Configuro le colonne della tabella Utenti
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // 3. Collego i dati alle Tabelle
        tabellaLibri.setItems(catalogoLibri);
        tabellaUtenti.setItems(anagraficaUtenti);

        // 4. Collego i dati alle ComboBox (per la selezione nel form Prestiti)
        comboUtenti.setItems(anagraficaUtenti);
        comboLibri.setItems(catalogoLibri);

        // NOTA: Non carico dati di prova. Il programma parte vuoto.
        // Per aggiungere dati, dovrai creare dei metodi pubblici o collegare un DB.
    }


    /// --- LOGICA GESTIONE EVENTI --- ///

    /**
     * Gestisce il click sul bottone "Conferma Prestito".
     * Esegue la logica di business e gestisce eventuali errori del Model.
     */
    @FXML
    private void handleRegistraPrestito() {
        resetMessaggio();

        Utente utente = comboUtenti.getValue();
        Libro libro = comboLibri.getValue();

        // Validazione GUI (Input mancante)
        if (utente == null || libro == null) {
            mostraErrore("Seleziona sia un utente che un libro.");
            return;
        }

        try {
            // 1. Logica di Business (Model)
            // Verifiche preliminari
            if (libro.getDisponibilita() < 1) {
                throw new IllegalStateException("Il libro '" + libro.getTitolo() + "' non è disponibile.");
            }

            // Creo il prestito (Il costruttore di Prestito potrebbe lanciare eccezioni)
            Prestito nuovoPrestito = new Prestito(utente, libro, LocalDate.now());

            // 2. Aggiornamento Stato
            utente.aggiungiPrestito(nuovoPrestito); // Potrebbe lanciare eccezione (max 5 prestiti)
            libro.decrementaDisponibilita();

            // 3. Feedback Positivo
            mostraSuccesso("Prestito registrato! Scadenza: " + nuovoPrestito.getDataFinePrevista());

            // 4. Refresh Grafico (Importante per vedere il calo disponibilità)
            tabellaLibri.refresh();
            tabellaUtenti.refresh();

            // Pulisco la selezione
            comboLibri.getSelectionModel().clearSelection();
            comboUtenti.getSelectionModel().clearSelection();

        } catch (IllegalArgumentException | IllegalStateException e) {
            // Qui catturiamo gli errori lanciati dal tuo Model (es. "Matricola nulla", "Copie esaurite")
            mostraErrore(e.getMessage());
        } catch (Exception e) {
            mostraErrore("Errore imprevisto: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /// --- METODI DI UTILITÀ (API INTERNA) --- ///

    // Metodi pubblici per permettere di aggiungere dati da altre parti del codice (o futuri form)

    public void aggiungiLibroAlCatalogo(Libro nuovoLibro) {
        if (nuovoLibro != null) {
            catalogoLibri.add(nuovoLibro);
        }
    }

    public void aggiungiUtenteInAnagrafica(Utente nuovoUtente) {
        if (nuovoUtente != null) {
            anagraficaUtenti.add(nuovoUtente);
        }
    }

    // Gestione feedback visivo (Label rossa/verde)
    private void mostraErrore(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setTextFill(Color.RED);
        lblMessaggio.setVisible(true);
    }

    private void mostraSuccesso(String msg) {
        lblMessaggio.setText(msg);
        lblMessaggio.setTextFill(Color.GREEN);
        lblMessaggio.setVisible(true);
    }

    private void resetMessaggio() {
        lblMessaggio.setVisible(false);
        lblMessaggio.setText("");
    }
}