package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.*;
import it.unisa.biblioteca.view.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controller principale.
 * <p>
 *     La "mente" del progetto, attraverso il controller vengono effettuate tutte le operazioni logiche, sui dati messi
 *     a disposizione dalle altre classi.
 *     Include gestione "Lazy Loading": se ci sono troppi dati, le tabelle di visualizzazione partono vuote.
 * </p>
 *
 */
public class BibliotecaController {

    private final Stage stage;
    private final GestoreFile gestoreFile = new GestoreFile();

    private ObservableList<Libro> catalogo = FXCollections.observableArrayList();
    private ObservableList<Utente> anagrafica = FXCollections.observableArrayList();
    private ObservableList<Prestito> prestiti = FXCollections.observableArrayList();

    /**
     * Costruttore del controller
     * <p>
     *     Inizializza lo stage che viene passato come parametro.
     *     Si occupa di effettuare il caricamento del file dati all'avvio del programma ({@link caricaTutto})
     *     e si occupa di effettuare il salvataggio automatico in caso di tentativo di chiusura del programma ({@link salvaTutto})
     *
     * </p>
     * @param stage stage da caricare passato come parametro
     * @see caricaTutto
     * @see salvaTutto
     */
    public BibliotecaController(Stage stage) {
        this.stage = stage;

        gestoreFile.caricaTutto(catalogo, anagrafica, prestiti);

        if (catalogo.isEmpty() && anagrafica.isEmpty()) {
            inizializzaDatiProva();
        }

        stage.setOnCloseRequest(event -> gestoreFile.salvaTutto(catalogo, anagrafica, prestiti));
    }

    /**
     * Metodo centralizzato per il cambio schermata.
     * Gestisce la modalità "Borderless Windowed" (Finto Fullscreen).
     */
    private void cambiaVista(Parent view, String titolo) {
        // 1. Configurazione UNA TANTUM all'avvio
        if (stage.getScene() == null) {
            // Rimuove la barra del titolo (X, -, []) e i bordi
            // QUESTO è il segreto del "Windowless"
            stage.initStyle(StageStyle.UNDECORATED);

            // Calcola le dimensioni dello schermo
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            // Usa .getBounds() se vuoi coprire ANCHE la barra delle applicazioni (tutto tutto)
            // Usa .getVisualBounds() se vuoi lasciare visibile la barra sotto.

            // Imposta dimensioni e posizione manuali
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            // Crea la scena
            Scene scene = new Scene(view);
            stage.setScene(scene);
        } else {
            // Navigazione successiva: cambia solo il contenuto
            stage.setTitle(titolo); // Il titolo non si vede ma serve per la taskbar
            stage.getScene().setRoot(view);
        }

        // IMPORTANTE: NON usare setFullScreen(true) qui, altrimenti torni alla modalità gioco.
        // L'effetto fullscreen è dato dal fatto che è UNDECORATED e grande quanto il monitor.
        stage.show();
    }

    /**
     * Chiamata alla scene della Home.
     * <p>
     * Permette di visualizzare la schermata principale adattandosi dinamicamente
     * alle dimensioni del monitor (Responsive Design), evitando dimensioni hardcoded.
     * </p>
     */
    public void mostraHome() {
        HomeView view = new HomeView();
        // ... (tuoi listener rimangono uguali) ...
        view.getBtnLibri().setOnAction(e -> mostraLibri());
        view.getBtnUtenti().setOnAction(e -> mostraUtenti());
        view.getBtnPrestiti().setOnAction(e -> mostraPrestiti());
        view.getBtnInfo().setOnAction(e -> mostraInfo());
        view.getBtnEsci().setOnAction(e -> {
            // Salva i dati e chiude (chiama la funzione che già fa il salvataggio)
            gestoreFile.salvaTutto(catalogo, anagrafica, prestiti);
            System.exit(0);
        });
        // SOSTITUISCI TUTTA LA LOGICA DI SCENE/SCREEN CON QUESTA RIGA:
        cambiaVista(view, "Gestionale Biblioteca - Home");
    }

    // --- GESTIONE LIBRI ---

    /**
     * Inizializza la view del catalogo libri
     * <p>
     *     Inizializza la view del catalogo libri ({@link LibriView}), e di conseguenza tutte le componenti in essa contenute
     * </p>
     * @see tableView
     * @see LibriView
     *
     */
    public void mostraLibri() {
        LibriView view = new LibriView(catalogo);

        FilteredList<Libro> filteredData = new FilteredList<>(catalogo, b -> true);


        // Se > 50 elementi, parti vuoto (Codice esistente)
        if (catalogo.size() > 50) {
            filteredData.setPredicate(p -> false);
            view.getTabella().setPlaceholder(new Label("Ci sono molti libri. Usa la ricerca per visualizzarli."));
        }

        // --- MODIFICA: Aggiunta Wrapper SortedList ---
        // Avvolgiamo la lista filtrata in una SortedList
        javafx.collections.transformation.SortedList<Libro> sortedData = new javafx.collections.transformation.SortedList<>(filteredData);

        // Colleghiamo il comparatore della SortedList a quello della tabella
        // Questo permette all'utente di cliccare sulle intestazioni delle colonne per cambiare ordinamento
        sortedData.comparatorProperty().bind(view.getTabella().comparatorProperty());

        // Assegniamo la lista ORDINATA alla tabella (non più quella solo filtrata)
        view.getTabella().setItems(sortedData);

        // --- IMPOSTAZIONE ORDINAMENTO PREDEFINITO (Titolo) ---
        // Recuperiamo la colonna "Titolo" (indice 0 nella LibriView)
        TableColumn<Libro, ?> colTitoloIndex = view.getTabella().getColumns().get(0);

        // Impostiamo il tipo di ordinamento (Ascendente: A-Z)
        colTitoloIndex.setSortType(TableColumn.SortType.ASCENDING);

        // Aggiungiamo la colonna alla lista di ordinamento della tabella
        view.getTabella().getSortOrder().add(colTitoloIndex);
        view.getTabella().sort(); // Forza l'applicazione dell'ordinamento visivo

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

        ///Sezione per la gestione di editing dati dalla table


        view.getTabella().setEditable(true);

        // Colonna titolo
        TableColumn<Libro, String> colTitolo = (TableColumn<Libro, String>) view.getTabella().getColumns().get(0);
        colTitolo.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitolo.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            String nuovoTitolo = event.getNewValue();
            try {
                libro.setTitolo(nuovoTitolo); // Validazione dati della classe LIBRO
            } catch (IllegalArgumentException ex) {
                showAlert("Errore Modifica", ex.getMessage());
                view.getTabella().refresh(); // Ripristina il valore vecchio
            }
        });

        // --- COLONNA AUTORI ---
        TableColumn<Libro, String> colAutori = (TableColumn<Libro, String>) view.getTabella().getColumns().get(1);
        colAutori.setCellFactory(TextFieldTableCell.forTableColumn());
        colAutori.setOnEditCommit(event -> {
            Libro libro = event.getRowValue();
            String stringaAutori = event.getNewValue();
            try {
                // Converto stringa in lista
                List<String> listaAutori = Arrays.asList(stringaAutori.split(","));
                // Pulisco spazi extra
                listaAutori.replaceAll(String::trim);
                libro.setAutori(listaAutori);
            } catch (IllegalArgumentException ex) {
                showAlert("Errore Autori", ex.getMessage());
                view.getTabella().refresh();
            }
        });


        cambiaVista(view, "Gestionale Biblioteca - Gestione Libri");

    }

    /**
     * Inizializza la schermata per l'aggiunta di un libro
     *
     * <p>
     *     Inizializza la schermata per l'aggiunta di un libro {@link AggiungiLibroView}
     *     <br>
     *     I parametri da specificare sono:
     *     <ul>
     *          <li>Titolo({@link String})</li>
     *          <li>Autore({@link String})</li>
     *          <li>Data di Pubblicazione({@link LocalDate})</li>
     *          <li>Isbn({@link String})</li>
     *          <li>Disponibilità(int)</li>
     *     </ul>
     *
     * </p>
     *
     *
     *
     *
     * @see Libro
     * @throws IllegalArgumentException
     */

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
                // 1. Recupero e pulizia preliminare (Trim)
                String titolo = view.getTxtTitolo().getText().trim();
                String autoriRaw = view.getTxtAutori().getText().trim();
                String isbn = view.getTxtIsbn().getText().trim();
                String copie = view.getTxtCopie().getText().trim();
                LocalDate dataPubblicazione = view.getDatePicker().getValue();

                // 2. NUOVI CONTROLLI (Campi Obbligatori)
                if (titolo.isEmpty()) {
                    throw new IllegalArgumentException("Il campo 'Titolo' non può essere vuoto.");
                }
                if (autoriRaw.isEmpty()) {
                    throw new IllegalArgumentException("Inserire almeno un autore.");
                }
                if (dataPubblicazione == null) {
                    throw new IllegalArgumentException("Selezionare una data di pubblicazione.");
                }

                // 3. Controlli Formato (Già esistenti)
                if (!isbn.matches("\\d{13}")) {
                    throw new IllegalArgumentException("L'ISBN deve essere composto da 13 cifre esatte.");
                }
                // Controllo duplicati
                for (Libro l : catalogo) {
                    if (l.getIsbn().equals(isbn)) throw new IllegalArgumentException("Esiste già un libro con questo ISBN.");
                }
                if (!copie.matches("\\d+")) {
                    throw new IllegalArgumentException("Il campo 'Copie' deve contenere un numero valido.");
                }

                // 4. Elaborazione Dati (Split Autori)
                // Dividiamo per virgola e rimuoviamo spazi extra per ogni autore (es. "Tolkien, Lewis")
                List<String> autori = new java.util.ArrayList<>(Arrays.asList(autoriRaw.split(",")));
                autori.replaceAll(String::trim);
                // Rimuoviamo eventuali stringhe vuote residue
                autori.removeIf(String::isEmpty);

                // 5. Inserimento nel Model
                catalogo.add(new Libro(titolo, autori, dataPubblicazione, isbn, Integer.parseInt(copie)));

                // Torna indietro solo se tutto va a buon fine
                mostraLibri();

            } catch (Exception ex) {
                // Mostra il warning a video
                showAlert("Dati mancanti o non validi", ex.getMessage());
            }
        });
        cambiaVista(view, "Gestionale Biblioteca - Aggiungi Libro");
    }

    // --- GESTIONE UTENTI ---

    /**
     * Inizializza la view dell'anagrafica utenti
     * <p>
     *     Inizializza la view dell'anagrafica utenti ({@link UtentiView}), e di conseguenza tutte le componenti in essa contenute
     *
     * </p>
     * @see tableView
     * @see LibriView
     *
     */
    public void mostraUtenti() {
        UtentiView view = new UtentiView(anagrafica);
        FilteredList<Utente> filteredData = new FilteredList<>(anagrafica, u -> true);

// Se > 50 elementi, parti vuoto (Codice esistente)
        if (anagrafica.size() > 50) {
            filteredData.setPredicate(p -> false);
            view.getTabella().setPlaceholder(new Label("Molti utenti presenti. Usa la ricerca."));
        }

        // --- MODIFICA: Aggiunta Wrapper SortedList ---
        javafx.collections.transformation.SortedList<Utente> sortedData = new javafx.collections.transformation.SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(view.getTabella().comparatorProperty());
        view.getTabella().setItems(sortedData); // Setta la sortedData

        // --- IMPOSTAZIONE ORDINAMENTO PREDEFINITO (Cognome + Nome) ---
        // Indici basati su UtentiView: 0=Matricola, 1=Nome, 2=Cognome, 3=Email
        TableColumn<Utente, ?> colNomeIndex = view.getTabella().getColumns().get(1);
        TableColumn<Utente, ?> colCognomeIndex = view.getTabella().getColumns().get(2);

        // Impostiamo entrambi Ascendenti
        colCognomeIndex.setSortType(TableColumn.SortType.ASCENDING);
        colNomeIndex.setSortType(TableColumn.SortType.ASCENDING);

        // Puliamo eventuali ordinamenti precedenti
        view.getTabella().getSortOrder().clear();

        // Aggiungiamo PRIMA Cognome, POI Nome.
        // JavaFX userà il secondo criterio se il primo è uguale.
        view.getTabella().getSortOrder().addAll(colCognomeIndex, colNomeIndex);
        view.getTabella().sort();

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

        view.getTabella().setEditable(true);

        //COLONNA NOME
        TableColumn<Utente,String> colNome = (TableColumn<Utente, String>) view.getTabella().getColumns().get(1);
        colNome.setCellFactory(TextFieldTableCell.forTableColumn());
        colNome.setOnEditCommit(event -> {
            Utente utente = event.getRowValue();
            String nuovoNome = event.getNewValue();
            try{
                utente.setNome(nuovoNome);
            }
            catch(IllegalArgumentException ex){
                showAlert("Errore modifica", ex.getMessage());
                view.getTabella().refresh();
            }

        });

        //COLONNA COGNOME
        TableColumn<Utente,String> colCognome = (TableColumn<Utente, String>) view.getTabella().getColumns().get(2);
        colCognome.setCellFactory(TextFieldTableCell.forTableColumn());
        colCognome.setOnEditCommit(event -> {
            Utente utente = event.getRowValue();
            String nuovoCognome = event.getNewValue();
            try{
                utente.setCognome(nuovoCognome);
            }
            catch(IllegalArgumentException ex){
                showAlert("Errore modifica", ex.getMessage());
                view.getTabella().refresh();
            }

        });

        //COLONNA EMAIL
        TableColumn<Utente,String> colEmail = (TableColumn<Utente, String>) view.getTabella().getColumns().get(3);
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            Utente utente = event.getRowValue();
            String nuovaEmail = event.getNewValue();
            try{
                utente.setEmail(nuovaEmail);
            }
            catch(IllegalArgumentException ex){
                showAlert("Errore modifica", ex.getMessage());
                view.getTabella().refresh();
            }

        });

        cambiaVista(view, "Gestionale Biblioteca - Gestione Utenti");

    }

    /**
     * Inizializza la schermata per l'aggiunta di un Utente
     *
     * <p>
     *     Inizializza la schermata per l'aggiunta di un Utente {@link AggiungiUtenteView}
     *     <br>
     *     I parametri da specificare sono:
     *     <ul>
     *          <li>Nome({@link String})</li>
     *          <li>Cognome({@link String})</li>
     *          <li>Email({@link String})</li>
     *          <li>Matricola({@link String})</li>
     *     </ul>
     *
     * </p>
     *
     *
     *
     *
     * @see Utente
     * @throws IllegalArgumentException
     */

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
                if (!matr.matches("\\d{1,10}")) throw new IllegalArgumentException("Matricola deve essere composto da sole 10 CIFRE");

                for(Utente u : anagrafica) if(u.getMatricola().equals(matr)) throw new IllegalArgumentException("Matricola esistente.");

                anagrafica.add(new Utente(view.getTxtNome().getText(), view.getTxtCognome().getText(), matr, view.getTxtEmail().getText()));
                mostraUtenti();
            } catch (Exception ex) { showAlert("Errore", ex.getMessage()); }
        });
        cambiaVista(view, "Gestionale Biblioteca - Aggiungi Utente");
    }

    // --- GESTIONE PRESTITI ---

    /**
     * Inizializza la view dell'anagrafica utenti
     * <p>
     *
     *     Inizializza la view del catalogo prestiti ({@link PrestitiView}), e di conseguenza tutte le componenti in esso contenute
     *
     * </p>
     * @see tableView
     * @see PrestitiView
     *
     */
    public void mostraPrestiti() {
        PrestitiView view = new PrestitiView(prestiti);
        FilteredList<Prestito> filteredData = new FilteredList<>(prestiti, p -> true);

        // Se > 50 elementi, parti vuoto
        if (prestiti.size() > 50) {
            filteredData.setPredicate(p -> false);
            view.getTabella().setPlaceholder(new Label("Molti prestiti in archivio. Cerca per visualizzare."));
        }

        // --- MODIFICA: Aggiunta Wrapper SortedList per ordinamento ---
        javafx.collections.transformation.SortedList<Prestito> sortedData = new javafx.collections.transformation.SortedList<>(filteredData);

        // Collega il comparatore della SortedList a quello della TableView
        sortedData.comparatorProperty().bind(view.getTabella().comparatorProperty());

        // Imposta la lista ordinabile nella tabella
        view.getTabella().setItems(sortedData);

        // --- IMPOSTAZIONE ORDINAMENTO PREDEFINITO (Data Scadenza) ---
        // Recuperiamo la colonna "Scadenza" (indice 2 in PrestitiView)
        TableColumn<Prestito, ?> colScadenza = view.getTabella().getColumns().get(2);

        // Impostiamo ordinamento Ascendente (dal più vecchio al più futuro)
        colScadenza.setSortType(TableColumn.SortType.ASCENDING);

        // Applichiamo l'ordinamento
        view.getTabella().getSortOrder().add(colScadenza);
        view.getTabella().sort();
        // -------------------------------------------------------------

        view.getBtnCerca().setOnAction(e -> {
            String filter = view.getTxtRicerca().getText();
            if (filter == null || filter.isEmpty()) {
                filteredData.setPredicate(p -> true);
                return;
            }
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
            if (confermaAzione("Elimina Prestito", "Rimuovere dallo storico?")) {
                selezionato.getLibro().incrementaDisponibilita();
                prestiti.remove(selezionato);
            }
        });

        cambiaVista(view, "Gestionale Biblioteca - Gestione Prestiti");
    }

    // --- NUOVO PRESTITO CON SPLIT VIEW ---

    /**
     * Inizalizza la schermata per la registrazione di un nuovo prestito
     *
     * <p>
     *     Inizializza la schermata per la registrazione di un nuovo prestito ({@link AggiungiPrestitoView}), mostrando all'interno della {@link tableView}
     *     di sinistra l'anagrafica degli utenti, e nella {@link tableView} di destra il catalogo libri. Selezionando un elemento
     *     dalla prima tabella e un elemento dalla seconda, sarà possibile registrare un prestito
     * </p>
     *
     * @see AggiungiLibroView
     * @throws IllegalArgumentException
     */
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

        cambiaVista(view, "Gestionale Biblioteca - Aggiungi Prestiti");
    }

    // --- UTILS ---

    /**
     * Inizializza la schermata per la visione dei creators del progetto
     */
    private void mostraInfo() {
        String credits = "Progetto Ingegneria del Software\nGRUPPO 11:\n- Mattia Lettariello\n- Jonathan Punzo\n- Antonia Lamberti\n- Valentino Potapchuk";
        showAlert("Credits", credits);
    }

    /**
     * Generatore di una schermata di alert
     * <p>
     *     Genera una schermata di alert per segnalare eventuali errori durante l'esecuzione del programma.
     *     Il titolo e il contenuto del warning devono essere passati come parametri ({@link String}).
     * </p>
     * @param titolo
     * @param contenuto
     * @see Alert
     */
    private void showAlert(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }

    /**
     * Generatore di una schermata di conferma operazione
     *
     * <p>
     *     Genera una schermata di alert per chiedere conferma di eventuali operazioni critiche.
     *     Il titolo e il contenuto della schermata devono essere passati come parametri ({@link String})
     * </p>
     * @param titolo
     * @param domanda
     * @return boolean
     */
    private boolean confermaAzione(String titolo, String domanda) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(domanda);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Inizializza il database con dati realistici per test e demo.
     * Versione ottimizzata: usa Arrays.asList per maggiore pulizia.
     */
    private void inizializzaDatiProva() {
        System.out.println("--- INIZIO GENERAZIONE DATI REALISTICI ---");

        // 1. DATASET: Titoli e Autori
        String[] titoliReali = {
                "Clean Code", "The Pragmatic Programmer", "Design Patterns", "Introduction to Algorithms",
                "Head First Java", "Effective Java", "Refactoring", "Code Complete",
                "Artificial Intelligence", "Computer Networking", "Operating System Concepts",
                "Database System Concepts", "Compilers", "C Programming Language",
                "Java Concurrency", "Domain-Driven Design", "The Mythical Man-Month",
                "Sistemi Operativi", "Reti di Calcolatori", "Ingegneria del Software"
        };

        String[] autoriReali = {
                "Robert C. Martin", "Andy Hunt", "Erich Gamma", "Thomas Cormen",
                "Kathy Sierra", "Joshua Bloch", "Martin Fowler", "Steve McConnell",
                "Stuart Russell", "Andrew Tanenbaum", "Abraham Silberschatz",
                "Brian Kernighan", "Dennis Ritchie", "Eric Evans", "Fred Brooks"
        };

        String[] nomi = {"Mario", "Luigi", "Giovanna", "Anna", "Paolo", "Francesca", "Alessandro", "Elena", "Davide", "Sofia", "Marco", "Giulia"};
        String[] cognomi = {"Rossi", "Bianchi", "Verdi", "Esposito", "Russo", "Romano", "Ferrari", "Gallo", "Costa", "Fontana", "Conti", "Greco"};

        java.util.Random rand = new java.util.Random();

        // 2. GENERAZIONE LIBRI
        for (int i = 0; i < 40; i++) {
            String titoloBase = titoliReali[rand.nextInt(titoliReali.length)];
            // Aggiungo varietà ai titoli
            String titolo = (i % 3 == 0) ? titoloBase + " (Ed. Speciale)" : titoloBase;

            // SELEZIONE AUTORI SENZA ARRAYLIST
            String autore1 = autoriReali[rand.nextInt(autoriReali.length)];
            List<String> autoriLibro;

            // Randomizzo se avere 1 o 2 autori
            if (rand.nextBoolean()) {
                String autore2 = autoriReali[rand.nextInt(autoriReali.length)];
                // Arrays.asList crea una lista "ponte" fissa, perfetta per essere passata al costruttore
                autoriLibro = Arrays.asList(autore1, autore2);
            } else {
                autoriLibro = Arrays.asList(autore1);
            }

            String isbn = String.format("97888%08d", i * 1234);
            LocalDate dataPub = LocalDate.now().minusYears(rand.nextInt(20)).minusDays(rand.nextInt(365));
            int copie = 3 + rand.nextInt(8);

            // Aggiungo al catalogo (ObservableList gestisce l'implementazione interna)
            catalogo.add(new Libro(titolo, autoriLibro, dataPub, isbn, copie));
        }

        // 3. GENERAZIONE UTENTI
        for (int i = 1; i <= 30; i++) {
            String nome = nomi[rand.nextInt(nomi.length)];
            String cognome = cognomi[rand.nextInt(cognomi.length)];
            String matricola = String.format("05121%05d", i);
            String email = nome.toLowerCase() + "." + cognome.toLowerCase() + i + "@studenti.unisa.it";

            anagrafica.add(new Utente(nome, cognome, matricola, email));
        }

        // 4. GENERAZIONE PRESTITI (ATTIVI E SCADUTI)
        for (int i = 0; i < 25; i++) {
            // Pescaggio casuale dalle liste popolate sopra
            Utente u = anagrafica.get(rand.nextInt(anagrafica.size()));
            Libro l = catalogo.get(rand.nextInt(catalogo.size()));

            if (l.getDisponibilita() > 0 && u.getPrestitiAttivi().size() < 3) {

                // Genero data inizio tra oggi e 4 mesi fa (120 giorni)
                // Ricorda: la scadenza è fissata a +60gg nel costruttore di Prestito
                int giorniFa = rand.nextInt(120);
                LocalDate dataInizio = LocalDate.now().minusDays(giorniFa);

                Prestito p = new Prestito(u, l, dataInizio);

                try {
                    u.aggiungiPrestito(p);
                    l.decrementaDisponibilita();
                    prestiti.add(p);
                } catch (Exception e) {
                    // Ignoro errori di validazione (es. utente ha già questo libro specifico)
                }
            }
        }

        System.out.println("Dati generati: " + catalogo.size() + " libri, " + anagrafica.size() + " utenti, " + prestiti.size() + " prestiti.");
    }
}