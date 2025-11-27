package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class BibliotecaController {

    private final Stage stage; // Il riferimento alla finestra principale

    // --- I NOSTRI DATI (MODEL) ---
    private ObservableList<Libro> catalogo = FXCollections.observableArrayList();
    private ObservableList<Utente> anagrafica = FXCollections.observableArrayList();
    private ObservableList<Prestito> prestiti = FXCollections.observableArrayList();

    public BibliotecaController(Stage stage) {
        this.stage = stage;
        inizializzaDatiProva(); // Metodo in fondo per popolare dati finti
    }

    // --- NAVIGAZIONE: HOME ---
    public void mostraHome() {
        HomeView view = new HomeView();

        // Collego i bottoni della vista ai metodi del controller
        view.getBtnLibri().setOnAction(e -> mostraLibri());
        view.getBtnUtenti().setOnAction(e -> mostraUtenti());
        view.getBtnPrestiti().setOnAction(e -> mostraPrestiti());

        stage.setTitle("Biblioteca - Home");
        stage.setScene(new Scene(view, 900, 600));
    }

    // --- SEZIONE LIBRI ---
    public void mostraLibri() {
        LibriView view = new LibriView(catalogo); // Passo i dati alla view

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraFormLibro());

        // Logica bottoni rapidi (+/- copie)
        view.getBtnPiu().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) { l.incrementaDisponibilita(); view.refresh(); }
        });

        view.getBtnMeno().setOnAction(e -> {
            Libro l = view.getTabella().getSelectionModel().getSelectedItem();
            if (l != null) {
                try { l.decrementaDisponibilita(); view.refresh(); }
                catch (Exception ex) { /* Gestire errore UI */ }
            }
        });

        stage.setTitle("Gestione Libri");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraFormLibro() {
        FormLibroView view = new FormLibroView();

        view.getBtnAnnulla().setOnAction(e -> mostraLibri());
        view.getBtnSalva().setOnAction(e -> {
            try {
                // Converto input stringa in lista autori
                List<String> autori = Arrays.asList(view.getTxtAutori().getText().split(","));

                Libro nuovo = new Libro(
                        view.getTxtTitolo().getText(),
                        autori,
                        view.getDatePicker().getValue(),
                        view.getTxtIsbn().getText(),
                        Integer.parseInt(view.getTxtCopie().getText())
                );
                catalogo.add(nuovo);
                mostraLibri(); // Torno alla lista dopo il salvataggio
            } catch (Exception ex) {
                System.out.println("Errore inserimento: " + ex.getMessage());
            }
        });

        stage.setScene(new Scene(view, 600, 400));
    }

    // --- SEZIONE UTENTI ---
    public void mostraUtenti() {
        UtentiView view = new UtentiView(anagrafica);
        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraFormUtente());

        stage.setTitle("Gestione Utenti");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraFormUtente() {
        FormUtenteView view = new FormUtenteView();
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
                System.out.println("Errore utente: " + ex.getMessage());
            }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    // --- SEZIONE PRESTITI ---
    public void mostraPrestiti() {
        PrestitiView view = new PrestitiView(prestiti);
        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraFormPrestito());

        stage.setTitle("Registro Prestiti");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraFormPrestito() {
        // Passo le liste per popolare le tendine (ComboBox)
        FormPrestitoView view = new FormPrestitoView(anagrafica, catalogo);

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
                }
            } catch (Exception ex) {
                System.out.println("Errore prestito: " + ex.getMessage());
            }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    private void inizializzaDatiProva() {
        Libro l1 = new Libro("Clean Code", Arrays.asList("Martin"), LocalDate.now(), "ISBN1", 5);
        catalogo.add(l1);
        Utente u1 = new Utente("Mario", "Rossi", "001", "m@test.it");
        anagrafica.add(u1);
    }
}