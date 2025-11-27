package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controller principale - Gruppo 11.
 * Gestisce la logica CRUD con controlli di integrità referenziale.
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
        FilteredList<Libro> filteredData = new FilteredList<>(catalogo, b -> true);
        view.getTabella().setItems(filteredData);

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) { filteredData.setPredicate(p -> true); return; }
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

        // --- LOGICA ELIMINA LIBRO (Con controllo integrità) ---
        view.getBtnElimina().setOnAction(e -> {
            Libro selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) {
                showAlert("Attenzione", "Seleziona un libro dalla tabella per eliminarlo.");
                return;
            }

            // CONTROLLO: Il libro è in prestito?
            boolean inPrestito = false;
            for (Prestito p : prestiti) {
                if (p.getLibro().equals(selezionato)) {
                    inPrestito = true;
                    break;
                }
            }

            if (inPrestito) {
                showAlert("Impossibile Eliminare", "Questo libro è attualmente in prestito.\nDevi prima concludere il prestito.");
                return; // Blocco l'operazione
            }

            if (confermaAzione("Elimina Libro", "Sei sicuro di voler eliminare '" + selezionato.getTitolo() + "'?")) {
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

        // --- LOGICA ELIMINA UTENTE (Con controllo integrità) ---
        view.getBtnElimina().setOnAction(e -> {
            Utente selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) {
                showAlert("Attenzione", "Seleziona un utente da eliminare.");
                return;
            }

            // CONTROLLO: L'utente ha prestiti attivi?
            boolean haPrestiti = false;
            for (Prestito p : prestiti) {
                if (p.getUtente().equals(selezionato)) {
                    haPrestiti = true;
                    break;
                }
            }

            if (haPrestiti) {
                showAlert("Impossibile Eliminare", "L'utente ha prestiti attivi.\nDevono restituire i libri prima di poter eliminare l'account.");
                return; // Blocco l'operazione
            }

            if (confermaAzione("Elimina Utente", "Vuoi eliminare " + selezionato.getNome() + " " + selezionato.getCognome() + "?")) {
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
            view.getLblContatoreMatr().setTextFill((len == 10 && nev.matches("\\d+")) ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);
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

        // --- ELIMINA PRESTITO (Qui è più semplice, elimina dallo storico) ---
        view.getBtnElimina().setOnAction(e -> {
            Prestito selezionato = view.getTabella().getSelectionModel().getSelectedItem();
            if (selezionato == null) {
                showAlert("Attenzione", "Seleziona un prestito da concludere/eliminare.");
                return;
            }
            if (confermaAzione("Elimina Prestito", "Vuoi rimuovere questo prestito dallo storico?")) {
                prestiti.remove(selezionato);
            }
        });

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
                } else { showAlert("Errore", "Dati mancanti o libro non disponibile."); }
            } catch (Exception ex) { showAlert("Errore", ex.getMessage()); }
        });
        stage.setScene(new Scene(view, 600, 400));
    }

    // --- UTILS ---
    private void mostraInfo() {
        String credits = "Progetto Ingegneria del Software\nGRUPPO 11:\n- Mattia Lettariello\n- Jonathan Punzo\n- Antonia Lamberti\n- Valentino Potapchuck";
        showAlert("Credits", credits);
    }

    private void showAlert(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // O Error per errori gravi
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

    private void inizializzaDatiProva() {
        catalogo.add(new Libro("Clean Code", Arrays.asList("Martin"), LocalDate.now(), "9781234567890", 5));
        anagrafica.add(new Utente("Mario", "Rossi", "001", "m@test.it"));
    }
}