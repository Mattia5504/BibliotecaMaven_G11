package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Controller principale - Gruppo 11.
 * Include logica di navigazione, ricerca e controllo duplicati (ISBN/Matricola).
 */
public class BibliotecaController {

    private final Stage stage;
    private final GestoreFile gestoreFile = new GestoreFile();

    // --- DATABASE IN MEMORIA ---
    private ObservableList<Libro> catalogo = FXCollections.observableArrayList();
    private ObservableList<Utente> anagrafica = FXCollections.observableArrayList();
    private ObservableList<Prestito> prestiti = FXCollections.observableArrayList();

    public BibliotecaController(Stage stage) {
        this.stage = stage;

        gestoreFile.caricaTutto(catalogo, anagrafica, prestiti);

        if (catalogo.isEmpty() && anagrafica.isEmpty()) {
            inizializzaDatiProva();
        }

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

    // --- GESTIONE LIBRI ---
    public void mostraLibri() {
        LibriView view = new LibriView(catalogo);

        // Wrapper FilteredList
        FilteredList<Libro> filteredData = new FilteredList<>(catalogo, b -> true);
        view.getTabella().setItems(filteredData);

        // AZIONE TASTO CERCA
        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) {
                filteredData.setPredicate(p -> true);
                return;
            }

            String lowerCaseFilter = filter.toLowerCase();
            String criterio = view.getCmbCriterio().getValue();

            filteredData.setPredicate(libro -> {
                switch (criterio) {
                    case "Titolo": return libro.getTitolo().toLowerCase().contains(lowerCaseFilter);
                    case "ISBN": return libro.getIsbn().toLowerCase().contains(lowerCaseFilter);
                    case "Autore": return libro.getAutori().toString().toLowerCase().contains(lowerCaseFilter);
                    case "Anno": return String.valueOf(libro.getDataPubblicazione().getYear()).contains(lowerCaseFilter);
                    default: return false;
                }
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiLibro());

        view.getBtnPiu().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) { l.incrementaDisponibilita(); view.refresh(); }
        });

        view.getBtnMeno().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) {
                try { l.decrementaDisponibilita(); view.refresh(); }
                catch (IllegalStateException ex) { showAlert("Attenzione", ex.getMessage()); }
            }
        });

        stage.setTitle("Gestione Libri - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiLibro() {
        AggiungiLibroView view = new AggiungiLibroView();
        view.getBtnAnnulla().setOnAction(e -> mostraLibri());

        // --- LOGICA CONTATORE ISBN (LISTENER) ---
        view.getTxtIsbn().textProperty().addListener((observable, oldValue, newValue) -> {
            int lunghezza = newValue.length();
            view.getLblContatoreIsbn().setText(lunghezza + " su 13");

            // Feedback Visivo: Verde se ok (13), Rosso altrimenti
            if (lunghezza == 13 && newValue.matches("\\d+")) {
                view.getLblContatoreIsbn().setTextFill(javafx.scene.paint.Color.GREEN);
            } else {
                view.getLblContatoreIsbn().setTextFill(javafx.scene.paint.Color.RED);
            }
        });

        view.getBtnSalva().setOnAction(e -> {
            try {
                String isbnInserito = view.getTxtIsbn().getText().trim();
                String copieTxt = view.getTxtCopie().getText().trim();

                // Validazione ISBN
                if (!isbnInserito.matches("\\d{13}")) {
                    throw new IllegalArgumentException("L'ISBN deve contenere esattamente 13 numeri.");
                }

                // Controllo Duplicati
                for (Libro l : catalogo) {
                    if (l.getIsbn().equals(isbnInserito)) throw new IllegalArgumentException("Esiste già un libro con questo ISBN.");
                }

                // Validazione Copie
                if (!copieTxt.matches("\\d+")) throw new IllegalArgumentException("Il campo Copie deve essere un numero.");

                List<String> autori = Arrays.asList(view.getTxtAutori().getText().split(","));

                Libro nuovo = new Libro(
                        view.getTxtTitolo().getText(), autori, view.getDatePicker().getValue(),
                        isbnInserito, Integer.parseInt(copieTxt)
                );

                catalogo.add(nuovo);
                mostraLibri();

            } catch (Exception ex) { showAlert("Errore Inserimento", ex.getMessage()); }
        });

        stage.setScene(new Scene(view, 600, 500));
    }

    // --- GESTIONE UTENTI ---
    public void mostraUtenti() {
        UtentiView view = new UtentiView(anagrafica);

        FilteredList<Utente> filteredData = new FilteredList<>(anagrafica, u -> true);
        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) {
                filteredData.setPredicate(p -> true);
                return;
            }

            String lower = filter.toLowerCase();
            String crit = view.getCmbCriterio().getValue();

            filteredData.setPredicate(utente -> {
                if (crit.equals("Cognome")) return utente.getCognome().toLowerCase().contains(lower);
                if (crit.equals("Matricola")) return utente.getMatricola().toLowerCase().contains(lower);
                if (crit.equals("Email")) return utente.getEmail().toLowerCase().contains(lower);
                return false;
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiUtente());

        stage.setTitle("Gestione Utenti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiUtente() {
        AggiungiUtenteView view = new AggiungiUtenteView();
        view.getBtnAnnulla().setOnAction(e -> mostraUtenti());

        // --- LOGICA CONTATORE MATRICOLA (LISTENER) ---
        view.getTxtMatricola().textProperty().addListener((observable, oldValue, newValue) -> {
            int lunghezza = newValue.length();
            // Matricola è "fino a 10", quindi ok se <= 10 e > 0
            view.getLblContatoreMatr().setText(lunghezza + " su 10");

            if (lunghezza > 0 && lunghezza <= 10 && newValue.matches("\\d+")) {
                view.getLblContatoreMatr().setTextFill(javafx.scene.paint.Color.GREEN);
            } else {
                view.getLblContatoreMatr().setTextFill(javafx.scene.paint.Color.RED);
            }
        });

        view.getBtnSalva().setOnAction(e -> {
            try {
                String matricolaInserita = view.getTxtMatricola().getText().trim();

                if (!matricolaInserita.matches("\\d{1,10}")) {
                    throw new IllegalArgumentException("Matricola non valida (solo numeri, max 10 cifre).");
                }

                for (Utente u : anagrafica) {
                    if (u.getMatricola().equals(matricolaInserita)) throw new IllegalArgumentException("Matricola già esistente.");
                }

                Utente nuovo = new Utente(
                        view.getTxtNome().getText(), view.getTxtCognome().getText(),
                        matricolaInserita, view.getTxtEmail().getText()
                );

                anagrafica.add(nuovo);
                mostraUtenti();

            } catch (Exception ex) { showAlert("Errore Inserimento", ex.getMessage()); }
        });

        stage.setScene(new Scene(view, 600, 400));
    }

    // --- GESTIONE PRESTITI ---
    public void mostraPrestiti() {
        PrestitiView view = new PrestitiView(prestiti);

        FilteredList<Prestito> filteredData = new FilteredList<>(prestiti, p -> true);
        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) {
                filteredData.setPredicate(p -> true);
                return;
            }

            String lower = filter.toLowerCase();
            String crit = view.getCmbCriterio().getValue();

            filteredData.setPredicate(prestito -> {
                if (crit.startsWith("Utente")) return prestito.getUtente().getCognome().toLowerCase().contains(lower);
                if (crit.startsWith("Libro")) return prestito.getLibro().getTitolo().toLowerCase().contains(lower);
                return false;
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiPrestito());

        stage.setTitle("Registro Prestiti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiPrestito() {
        AggiungiPrestitoView view = new AggiungiPrestitoView(anagrafica, catalogo);
        view.getBtnAnnulla().setOnAction(e -> mostraPrestiti());

        view.getBtnSalva().setOnAction(e -> {
            try {
                Utente u = view.getComboUtenti().getValue();
                Libro l = view.getComboLibri().getValue();

                if (u != null && l != null && l.getDisponibilita() > 0) {
                    Prestito p = new Prestito(u, l, LocalDate.now());
                    u.aggiungiPrestito(p);
                    l.decrementaDisponibilita();
                    prestiti.add(p);
                    mostraPrestiti();
                } else {
                    showAlert("Errore", "Dati mancanti o libro non disponibile.");
                }
            } catch (Exception ex) {
                showAlert("Errore Prestito", ex.getMessage());
            }
        });

        stage.setScene(new Scene(view, 600, 400));
    }

    // --- UTILS ---
    private void mostraInfo() {
        String credits = "Progetto Ingegneria del Software\nGRUPPO 11:\n- Mattia Lettariello\n- Jonathan Punzo\n- Antonia Lamberti\n- Valentino Potapchuck";
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
        catalogo.add(new Libro("Clean Code", Arrays.asList("Martin"), LocalDate.now(), "ISBN1", 5));
        anagrafica.add(new Utente("Mario", "Rossi", "001", "m@test.it"));
    }
}