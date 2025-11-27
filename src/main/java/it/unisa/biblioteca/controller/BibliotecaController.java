package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controller principale - Gruppo 11.
 * Include gestione "Lazy Loading": se ci sono troppi dati, le tabelle partono vuote.
 */
public class BibliotecaController {

    private final Stage stage;
    private final GestoreFile gestoreFile = new GestoreFile();

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

        // 1. Creazione Lista Filtrata
        FilteredList<Libro> filteredData = new FilteredList<>(catalogo, b -> true);

        // --- MODIFICA RICHIESTA: Se > 50 elementi, parti vuoto ---
        if (catalogo.size() > 50) {
            filteredData.setPredicate(p -> false); // Nascondi tutto all'inizio
            view.getTabella().setPlaceholder(new Label("Ci sono molti libri. Usa la ricerca per visualizzarli."));
        }

        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) {
                // Se la ricerca è vuota, mostriamo tutto (reset)
                filteredData.setPredicate(p -> true);
                return;
            }
            String lower = filter.toLowerCase();
            String crit = view.getCmbCriterio().getValue();
            filteredData.setPredicate(libro -> {
                switch (crit) {
                    case "Titolo": return libro.getTitolo().toLowerCase().contains(lower);
                    case "ISBN": return libro.getIsbn().toLowerCase().contains(lower);
                    case "Autore": return libro.getAutori().toString().toLowerCase().contains(lower);
                    case "Anno": return String.valueOf(libro.getDataPubblicazione().getYear()).contains(lower);
                    default: return false;
                }
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiLibro());

        view.getBtnElimina().setOnAction(e -> {
            Libro selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) { showAlert("Attenzione", "Seleziona un libro."); return; }

            // Controllo integrità
            boolean inPrestito = false;
            for (Prestito p : prestiti) if (p.getLibro().equals(selezionato)) { inPrestito = true; break; }
            if (inPrestito) { showAlert("Impossibile Eliminare", "Libro attualmente in prestito."); return; }

            if (confermaAzione("Elimina Libro", "Eliminare '" + selezionato.getTitolo() + "'?")) {
                catalogo.remove(selezionato);
            }
        });

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

        view.getTxtIsbn().textProperty().addListener((obs, old, nev) -> {
            int len = nev.length();
            view.getLblContatoreIsbn().setText(len + " su 13");
            view.getLblContatoreIsbn().setTextFill((len == 13 && nev.matches("\\d+")) ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
        });

        view.getBtnSalva().setOnAction(e -> {
            try {
                String isbn = view.getTxtIsbn().getText().trim();
                String copie = view.getTxtCopie().getText().trim();

                if (!isbn.matches("\\d{13}")) throw new IllegalArgumentException("ISBN deve essere 13 cifre.");
                for(Libro l : catalogo) if(l.getIsbn().equals(isbn)) throw new IllegalArgumentException("ISBN duplicato.");
                if (!copie.matches("\\d+")) throw new IllegalArgumentException("Copie deve essere un numero.");

                List<String> autori = Arrays.asList(view.getTxtAutori().getText().split(","));
                catalogo.add(new Libro(view.getTxtTitolo().getText(), autori, view.getDatePicker().getValue(), isbn, Integer.parseInt(copie)));
                mostraLibri();
            } catch (Exception ex) { showAlert("Errore", ex.getMessage()); }
        });
        stage.setScene(new Scene(view, 600, 500));
    }

    // --- GESTIONE UTENTI ---
    public void mostraUtenti() {
        UtentiView view = new UtentiView(anagrafica);
        FilteredList<Utente> filteredData = new FilteredList<>(anagrafica, u -> true);

        // --- MODIFICA RICHIESTA: Se > 50 elementi, parti vuoto ---
        if (anagrafica.size() > 50) {
            filteredData.setPredicate(p -> false);
            view.getTabella().setPlaceholder(new Label("Molti utenti presenti. Usa la ricerca."));
        }

        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) { filteredData.setPredicate(p -> true); return; }
            String lower = filter.toLowerCase();
            String crit = view.getCmbCriterio().getValue();
            filteredData.setPredicate(u -> {
                if (crit.equals("Cognome")) return u.getCognome().toLowerCase().contains(lower);
                if (crit.equals("Matricola")) return u.getMatricola().toLowerCase().contains(lower);
                if (crit.equals("Email")) return u.getEmail().toLowerCase().contains(lower);
                return false;
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiUtente());

        view.getBtnElimina().setOnAction(e -> {
            Utente selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) { showAlert("Attenzione", "Seleziona un utente."); return; }

            boolean haPrestiti = false;
            for (Prestito p : prestiti) if (p.getUtente().equals(selezionato)) { haPrestiti = true; break; }
            if (haPrestiti) { showAlert("Errore", "Utente ha prestiti attivi."); return; }

            if (confermaAzione("Elimina Utente", "Eliminare " + selezionato.getNome() + "?")) {
                anagrafica.remove(selezionato);
            }
        });

        stage.setTitle("Gestione Utenti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    public void mostraAggiungiUtente() {
        AggiungiUtenteView view = new AggiungiUtenteView();
        view.getBtnAnnulla().setOnAction(e -> mostraUtenti());

        view.getTxtMatricola().textProperty().addListener((obs, old, nev) -> {
            int len = nev.length();
            view.getLblContatoreMatr().setText(len + " su 10");
            view.getLblContatoreMatr().setTextFill((len > 0 && len <= 10 && nev.matches("\\d+")) ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
        });

        view.getBtnSalva().setOnAction(e -> {
            try {
                String matr = view.getTxtMatricola().getText().trim();
                if (!matr.matches("\\d{1,10}")) throw new IllegalArgumentException("Matricola max 10 cifre.");
                for(Utente u : anagrafica) if(u.getMatricola().equals(matr)) throw new IllegalArgumentException("Matricola esistente.");

                anagrafica.add(new Utente(view.getTxtNome().getText(), view.getTxtCognome().getText(), matr, view.getTxtEmail().getText()));
                mostraUtenti();
            } catch (Exception ex) { showAlert("Errore", ex.getMessage()); }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    // --- GESTIONE PRESTITI ---
    public void mostraPrestiti() {
        PrestitiView view = new PrestitiView(prestiti);
        FilteredList<Prestito> filteredData = new FilteredList<>(prestiti, p -> true);

        // --- MODIFICA RICHIESTA ---
        if (prestiti.size() > 50) {
            filteredData.setPredicate(p -> false);
            view.getTabella().setPlaceholder(new Label("Molti prestiti in archivio. Cerca per visualizzare."));
        }

        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) { filteredData.setPredicate(p -> true); return; }
            String lower = filter.toLowerCase();
            String crit = view.getCmbCriterio().getValue();
            filteredData.setPredicate(p -> {
                if (crit.startsWith("Utente")) return p.getUtente().getCognome().toLowerCase().contains(lower);
                if (crit.startsWith("Libro")) return p.getLibro().getTitolo().toLowerCase().contains(lower);
                return false;
            });
        });

        view.getBtnIndietro().setOnAction(e -> mostraHome());
        view.getBtnNuovo().setOnAction(e -> mostraAggiungiPrestito());

        view.getBtnElimina().setOnAction(e -> {
            Prestito selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) { showAlert("Attenzione", "Seleziona un prestito."); return; }
            if (confermaAzione("Elimina Prestito", "Rimuovere dallo storico?")) prestiti.remove(selezionato);
        });

        stage.setTitle("Registro Prestiti - Gruppo 11");
        stage.setScene(new Scene(view, 900, 600));
    }

    // --- NUOVO PRESTITO CON SPLIT VIEW ---
    public void mostraAggiungiPrestito() {
        AggiungiPrestitoView view = new AggiungiPrestitoView(anagrafica, catalogo);

        // 1. UTENTI (Sinistra)
        FilteredList<Utente> filteredUtenti = new FilteredList<>(anagrafica, u -> true);

        // --- MODIFICA RICHIESTA: Se > 50 utenti, parti vuoto ---
        if (anagrafica.size() > 50) {
            filteredUtenti.setPredicate(p -> false);
            view.getTableUtenti().setPlaceholder(new Label("Cerca studente..."));
        }
        view.getTableUtenti().setItems(filteredUtenti);

        view.getBtnCercaUtente().setOnAction(e -> {
            String filter = view.getTxtSearchUtente().getText();
            if (filter == null || filter.isEmpty()) { filteredUtenti.setPredicate(p -> true); return; }
            String lower = filter.toLowerCase();
            String crit = view.getCmbFilterUtente().getValue();
            filteredUtenti.setPredicate(u -> {
                if (crit.equals("Cognome")) return u.getCognome().toLowerCase().contains(lower);
                if (crit.equals("Matricola")) return u.getMatricola().toLowerCase().contains(lower);
                return false;
            });
        });

        // 2. LIBRI (Destra)
        FilteredList<Libro> filteredLibri = new FilteredList<>(catalogo, l -> true);

        // --- MODIFICA RICHIESTA: Se > 50 libri, parti vuoto ---
        if (catalogo.size() > 50) {
            filteredLibri.setPredicate(p -> false);
            view.getTableLibri().setPlaceholder(new Label("Cerca libro..."));
        }
        view.getTableLibri().setItems(filteredLibri);

        view.getBtnCercaLibro().setOnAction(e -> {
            String filter = view.getTxtSearchLibro().getText();
            if (filter == null || filter.isEmpty()) { filteredLibri.setPredicate(p -> true); return; }
            String lower = filter.toLowerCase();
            String crit = view.getCmbFilterLibro().getValue();
            filteredLibri.setPredicate(l -> {
                if (crit.equals("Titolo")) return l.getTitolo().toLowerCase().contains(lower);
                if (crit.equals("ISBN")) return l.getIsbn().toLowerCase().contains(lower);
                return false;
            });
        });

        view.getBtnAnnulla().setOnAction(e -> mostraPrestiti());

        view.getBtnSalva().setOnAction(e -> {
            Utente u = view.getTableUtenti().getSelectionModel().getSelectedItem();
            Libro l = view.getTableLibri().getSelectionModel().getSelectedItem();
            try {
                if (u == null) throw new IllegalArgumentException("Seleziona uno STUDENTE a sinistra.");
                if (l == null) throw new IllegalArgumentException("Seleziona un LIBRO a destra.");

                if (l.getDisponibilita() > 0) {
                    Prestito p = new Prestito(u, l, LocalDate.now());
                    u.aggiungiPrestito(p);
                    l.decrementaDisponibilita();
                    prestiti.add(p);
                    showAlert("Successo", "Prestito registrato!");
                    mostraPrestiti();
                } else {
                    showAlert("Non disponibile", "Copie esaurite per questo libro.");
                }
            } catch (Exception ex) { showAlert("Errore", ex.getMessage()); }
        });

        stage.setScene(new Scene(view, 900, 600));
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

    private boolean confermaAzione(String titolo, String domanda) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(domanda);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /// ... FUNZIONE CHE INIZIALIZZA IL FILE DAT IN MODO DA AVERE 60 LIBRI e 60 UTENTI PER INIZIARE ... ///

    private void inizializzaDatiProva() {
        System.out.println("Generazione dati di test massivi in corso...");

        // Genero 60 Libri
        for (int i = 1; i <= 60; i++) {
            // Creo un ISBN valido di 13 cifre (es. 9780000000001, 9780000000002...)
            String isbnFinto = String.format("97800000%05d", i);

            List<String> autori = Arrays.asList("Autore Generico " + i, "Co-Autore " + i);

            Libro l = new Libro(
                    "Libro di Ingegneria Vol. " + i, // Titolo diverso per ognuno
                    autori,
                    LocalDate.now().minusDays(i), // Date diverse
                    isbnFinto,
                    5 // Copie
            );
            catalogo.add(l);
        }

        // Genero 60 Utenti
        for (int i = 1; i <= 60; i++) {
            // Matricola valida (numeri, max 10 cifre)
            String matricolaFinta = String.format("%06d", i);

            Utente u = new Utente(
                    "Studente",
                    "Numero " + i,
                    matricolaFinta,
                    "studente" + i + "@unisa.it"
            );
            anagrafica.add(u);
        }

        System.out.println("Dati generati: " + catalogo.size() + " libri e " + anagrafica.size() + " utenti.");
    }
}