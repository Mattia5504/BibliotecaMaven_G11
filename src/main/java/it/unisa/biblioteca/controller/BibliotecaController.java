package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Controller principale - Gruppo 11.
 * Gestisce la navigazione tra le viste e la logica operativa.
 */
public class BibliotecaController {

    private final Stage stage;
    private final GestoreFile gestoreFile = new GestoreFile();

    // --- DATABASE IN MEMORIA (Liste Osservabili) ---
    private ObservableList<Libro> catalogo = FXCollections.observableArrayList();
    private ObservableList<Utente> anagrafica = FXCollections.observableArrayList();
    private ObservableList<Prestito> prestiti = FXCollections.observableArrayList();

    public BibliotecaController(Stage stage) {
        this.stage = stage;

        // Caricamento dati da file all'avvio
        gestoreFile.caricaTutto(catalogo, anagrafica, prestiti);

        // Se è la prima volta assoluta, carico dati finti per non mostrare tabelle vuote
        if (catalogo.isEmpty() && anagrafica.isEmpty()) {
            inizializzaDatiProva();
        }

        // Salvataggio automatico alla chiusura
        stage.setOnCloseRequest(event -> gestoreFile.salvaTutto(catalogo, anagrafica, prestiti));
    }

    // --- NAVIGAZIONE: HOME ---
    public void mostraHome() {
        HomeView view = new HomeView();

        view.getBtnLibri().setOnAction(e -> mostraLibri());
        view.getBtnUtenti().setOnAction(e -> mostraUtenti());
        view.getBtnPrestiti().setOnAction(e -> mostraPrestiti());
        view.getBtnInfo().setOnAction(e -> mostraInfo());



        stage.setTitle("Biblioteca - Gruppo 11 Home");
        stage.setScene(new Scene(view, 900, 600));
    }

    // --- SEZIONE: GESTIONE LIBRI ---
    public void mostraLibri() {
        LibriView view = new LibriView(catalogo);

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiLibro()); // <--- CAMBIATO

        // Logica pulsanti rapidi copie
        view.getBtnPiu().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) {
                l.incrementaDisponibilita();
                view.refresh();
            }
        });

        view.getBtnMeno().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) {
                try {
                    l.decrementaDisponibilita();
                    view.refresh();
                } catch (IllegalStateException ex) {
                    showAlert("Attenzione", ex.getMessage());
                }
            }
        });

        stage.setTitle("Gestione Libri - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiLibro() { // <--- CAMBIATO NOME METODO
        AggiungiLibroView view = new AggiungiLibroView(); // <--- CAMBIATO CLASSE

        view.getBtnAnnulla().setOnAction(e -> mostraLibri());
        view.getBtnSalva().setOnAction(e -> {
            try {
                String autoriRaw = view.getTxtAutori().getText();
                List<String> autori = Arrays.asList(autoriRaw.split(","));

                Libro nuovo = new Libro(
                        view.getTxtTitolo().getText(),
                        autori,
                        view.getDatePicker().getValue(),
                        view.getTxtIsbn().getText(),
                        Integer.parseInt(view.getTxtCopie().getText())
                );
                catalogo.add(nuovo);
                mostraLibri();
            } catch (Exception ex) {
                showAlert("Errore Inserimento", "Controlla i dati: " + ex.getMessage());
            }
        });

        stage.setScene(new Scene(view, 600, 500));
    }

    // --- SEZIONE: GESTIONE UTENTI ---
    public void mostraUtenti() {
        UtentiView view = new UtentiView(anagrafica);
        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiUtente()); // <--- CAMBIATO

        stage.setTitle("Gestione Utenti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiUtente() { // <--- CAMBIATO
        AggiungiUtenteView view = new AggiungiUtenteView(); // <--- CAMBIATO

        view.getBtnAnnulla().setOnAction(e -> mostraUtenti());
        view.getBtnSalva().setOnAction(e -> {
            try {
                Utente nuovo = new Utente(
                        view.getTxtNome().getText(),
                        view.getTxtCognome().getText(),
                        view.getTxtMatricola().getText(),
                        view.getTxtEmail().getText()
                );
                anagrafica.add(nuovo);
                mostraUtenti();
            } catch (Exception ex) {
                showAlert("Errore Utente", ex.getMessage());
            }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    // --- SEZIONE: GESTIONE PRESTITI ---
    public void mostraPrestiti() {
        PrestitiView view = new PrestitiView(prestiti);
        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiPrestito()); // <--- CAMBIATO

        stage.setTitle("Registro Prestiti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiPrestito() { // <--- CAMBIATO
        AggiungiPrestitoView view = new AggiungiPrestitoView(anagrafica, catalogo); // <--- CAMBIATO

        view.getBtnAnnulla().setOnAction(e -> mostraPrestiti());
        view.getBtnSalva().setOnAction(e -> {
            try {
                Utente u = view.getComboUtenti().getValue();
                Libro l = view.getComboLibri().getValue();

                if (u == null || l == null) throw new IllegalArgumentException("Seleziona Utente e Libro.");

                if (l.getDisponibilita() > 0) {
                    Prestito p = new Prestito(u, l, LocalDate.now());
                    u.aggiungiPrestito(p);
                    l.decrementaDisponibilita();
                    prestiti.add(p);
                    mostraPrestiti();
                } else {
                    showAlert("Non disponibile", "Copie esaurite per questo libro.");
                }
            } catch (Exception ex) {
                showAlert("Errore Prestito", ex.getMessage());
            }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    // --- UTILS ---
    private void mostraInfo() {
        String credits = "Progetto Ingegneria del Software\n\n" +
                "GRUPPO 11:\n" +
                "- Mattia Lettariello\n" +
                "- Jonathan Punzo\n" +
                "- Antonia Lamberti\n" +
                "- Valentino Potapchuck\n\n" +
                "Anno Accademico 2024/2025";
        showAlert("Credits", credits);
    }

    private void showAlert(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    private void inizializzaDatiProva() {
        Libro l1 = new Libro("Clean Code", Arrays.asList("Robert C. Martin"), LocalDate.of(2008, 8, 1), "ISBN-001", 5);
        catalogo.add(l1);
        Utente u1 = new Utente("Mario", "Rossi", "0512101234", "m.rossi@studenti.unisa.it");
        anagrafica.add(u1);
    }
}