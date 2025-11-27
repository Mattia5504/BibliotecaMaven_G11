package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane; // Classe generica per VBox, BorderPane, etc.
import javafx.scene.paint.Color;

import java.time.LocalDate;

public class BibliotecaController {

    /// --- NAVIGAZIONE (I PANNELLI) --- ///
    // Uso "Pane" che è la classe padre sia di VBox che di BorderPane.
    // Così il controller è flessibile.
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
    @FXML private Label lblMessaggio;


    /// --- INITIALIZE --- ///
    @FXML
    public void initialize() {
        // Setup Colonne Libri
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        // Setup Colonne Utenti
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Binding Dati
        tabellaLibri.setItems(catalogoLibri);
        tabellaUtenti.setItems(anagraficaUtenti);
        comboUtenti.setItems(anagraficaUtenti);
        comboLibri.setItems(catalogoLibri);
    }

    /// --- GESTIONE VISIBILITÀ (NAVIGAZIONE) --- ///

    // Metodo privato per spegnere tutto prima di accendere una vista specifica
    private void resetViste() {
        homeView.setVisible(false);
        libriView.setVisible(false);
        utentiView.setVisible(false);
        prestitiView.setVisible(false);
    }

    @FXML
    public void tornaHome() {
        resetViste();
        homeView.setVisible(true);
    }

    @FXML
    public void mostraLibri() {
        resetViste();
        libriView.setVisible(true);
    }

    @FXML
    public void mostraUtenti() {
        resetViste();
        utentiView.setVisible(true);
    }

    @FXML
    public void mostraPrestiti() {
        resetViste();
        prestitiView.setVisible(true);
        // Pulisco il form quando entro
        comboUtenti.getSelectionModel().clearSelection();
        comboLibri.getSelectionModel().clearSelection();
        lblMessaggio.setVisible(false);
    }

    @FXML
    public void mostraInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("Gestione Biblioteca v1.0");
        alert.setContentText("Progetto Ingegneria del Software\nStudente: Tuo Nome");
        alert.showAndWait();
    }


    /// --- LOGICA OPERATIVA --- ///

    @FXML
    private void handleRegistraPrestito() {
        lblMessaggio.setVisible(false);

        Utente u = comboUtenti.getValue();
        Libro l = comboLibri.getValue();

        if (u == null || l == null) {
            mostraErrore("Seleziona utente e libro.");
            return;
        }

        try {
            if (l.getDisponibilita() < 1) throw new IllegalStateException("Libro non disponibile!");

            Prestito p = new Prestito(u, l, LocalDate.now());
            u.aggiungiPrestito(p);
            l.decrementaDisponibilita();

            mostraSuccesso("Prestito registrato! Scadenza: " + p.getDataFinePrevista());

            // Refresh tabelle per vedere i cambiamenti
            tabellaLibri.refresh();
            tabellaUtenti.refresh();

        } catch (Exception e) {
            mostraErrore(e.getMessage());
        }
    }

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

    // API Pubbliche per inserire dati
    public void aggiungiLibro(Libro l) { catalogoLibri.add(l); }
    public void aggiungiUtente(Utente u) { anagraficaUtenti.add(u); }
}