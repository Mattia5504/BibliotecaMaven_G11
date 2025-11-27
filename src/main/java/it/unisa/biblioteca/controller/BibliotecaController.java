package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.util.Arrays; // Import utile per creare liste al volo se serve

public class BibliotecaController {

    /// --- NAVIGAZIONE --- ///
    @FXML private Pane homeView;
    @FXML private Pane libriView;
    @FXML private Pane utentiView;
    @FXML private Pane prestitiView;

    /// --- DATI (MODEL) --- ///
    private ObservableList<Libro> catalogoLibri = FXCollections.observableArrayList();
    private ObservableList<Utente> anagraficaUtenti = FXCollections.observableArrayList();

    /// --- TABELLA LIBRI --- ///
    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutori; // NUOVA
    @FXML private TableColumn<Libro, Integer> colAnno;  // NUOVA
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, Integer> colDisp;

    /// --- TABELLA UTENTI --- ///
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colEmail;

    /// --- FORM PRESTITI --- ///
    @FXML private ComboBox<Utente> comboUtenti;
    @FXML private ComboBox<Libro> comboLibri;

    // Unica Label per i messaggi (usata in tutte le view)
    @FXML private Label lblMessaggio;


    /// --- INITIALIZE --- ///
    @FXML
    public void initialize() {
        configuraTabellaLibri();
        configuraTabellaUtenti();

        // Binding Dati alle Tabelle e ComboBox
        tabellaLibri.setItems(catalogoLibri);
        tabellaUtenti.setItems(anagraficaUtenti);
        comboUtenti.setItems(anagraficaUtenti);
        comboLibri.setItems(catalogoLibri);

        // Dati di prova per vedere subito il risultato
        caricaDatiDiProva();
    }

    private void configuraTabellaLibri() {
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        // --- FORMATTAZIONE SPECIALE AUTORI ---
        // Prendo la lista di autori e la unisco con la virgola
        colAutori.setCellValueFactory(cellData -> {
            String autoriFormattati = String.join(", ", cellData.getValue().getAutori());
            return new SimpleStringProperty(autoriFormattati);
        });

        // --- FORMATTAZIONE SPECIALE ANNO ---
        // Estraggo solo l'anno dalla data di pubblicazione
        colAnno.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getDataPubblicazione().getYear())
        );
    }

    private void configuraTabellaUtenti() {
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    /// --- GESTIONE LIBRI (Aumenta/Diminuisci) --- ///

    @FXML
    public void handleAumentaCopie() {
        Libro libroSelezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (libroSelezionato != null) {
            libroSelezionato.incrementaDisponibilita();
            tabellaLibri.refresh(); // Aggiorna la vista per mostrare il numero cambiato
            mostraSuccesso("Copie aumentate per: " + libroSelezionato.getTitolo());
        } else {
            mostraErrore("Seleziona un libro dalla tabella per aumentare le copie.");
        }
    }

    @FXML
    public void handleRiduciCopie() {
        Libro libroSelezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (libroSelezionato != null) {
            try {
                libroSelezionato.decrementaDisponibilita();
                tabellaLibri.refresh();
                mostraSuccesso("Copie ridotte per: " + libroSelezionato.getTitolo());
            } catch (IllegalStateException e) {
                mostraErrore("Impossibile ridurre: " + e.getMessage());
            }
        } else {
            mostraErrore("Seleziona un libro dalla tabella per ridurre le copie.");
        }
    }

    /// --- NAVIGAZIONE --- ///

    private void resetViste() {
        homeView.setVisible(false);
        libriView.setVisible(false);
        utentiView.setVisible(false);
        prestitiView.setVisible(false);
        lblMessaggio.setVisible(false); // Resetta anche il messaggio
    }

    @FXML public void tornaHome() { resetViste(); homeView.setVisible(true); }
    @FXML public void mostraLibri() { resetViste(); libriView.setVisible(true); }
    @FXML public void mostraUtenti() { resetViste(); utentiView.setVisible(true); }

    @FXML
    public void mostraPrestiti() {
        resetViste();
        prestitiView.setVisible(true);
        comboUtenti.getSelectionModel().clearSelection();
        comboLibri.getSelectionModel().clearSelection();
    }

    @FXML
    public void mostraInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Gestione Biblioteca v1.0");
        alert.setContentText("Progetto Ingegneria del Software\nStudente: [Tuo Nome]");
        alert.showAndWait();
    }

    /// --- LOGICA OPERATIVA PRESTITI --- ///

    @FXML
    private void handleRegistraPrestito() {
        Utente u = comboUtenti.getValue();
        Libro l = comboLibri.getValue();

        if (u == null || l == null) {
            mostraErrore("Seleziona utente e libro.");
            return;
        }

        try {
            // Verifica disponibilità (il model lo fa, ma controlliamo per sicurezza UI)
            if (l.getDisponibilita() < 1) throw new IllegalStateException("Libro non disponibile!");

            Prestito p = new Prestito(u, l, LocalDate.now());
            u.aggiungiPrestito(p);
            l.decrementaDisponibilita();

            mostraSuccesso("Prestito registrato! Scadenza: " + p.getDataFinePrevista());
            tabellaLibri.refresh();
            tabellaUtenti.refresh();

        } catch (Exception e) {
            mostraErrore(e.getMessage());
        }
    }

    // Feedback Visivo
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

    // Dati di Prova
    private void caricaDatiDiProva() {
        catalogoLibri.add(new Libro("Clean Code", Arrays.asList("Robert C. Martin"), LocalDate.of(2008, 8, 1), "978-0132350884", 5));
        catalogoLibri.add(new Libro("Design Patterns", Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"), LocalDate.of(1994, 10, 21), "978-0201633610", 3));

        anagraficaUtenti.add(new Utente("Mario", "Rossi", "0512101234", "m.rossi@studenti.unisa.it"));
    }
}