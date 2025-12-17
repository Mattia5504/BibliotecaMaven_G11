package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableCell;

/**
 * @brief Vista per la gestione dell'Anagrafica Utenti.
 * * Visualizza l'elenco degli utenti registrati e fornisce gli strumenti per
 * la ricerca, l'aggiunta e la rimozione.
 * * @details
 * **Layout:**
 * - **TOP**: Barra di navigazione e filtri di ricerca (Cognome, Matricola, Email).
 * - **CENTER**: Tabella con colonne ridimensionabili automaticamente.
 * - **BOTTOM**: Pulsanti operativi.
 * * **Responsive Design:**
 * Le colonne (Matricola, Nome, Cognome, Email) sono vincolate (bound) per occupare
 * esattamente 1/4 della larghezza disponibile ciascuna.
 */
public class UtentiView extends BorderPane {

    private TableView<Utente> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Utente");
    private Button btnElimina = new Button("🗑 Elimina Utente"); // <--- NUOVO

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca");

    /**
     * @brief Costruttore della UtentiView.
     * * Configura la tabella collegando le colonne alle proprietà della classe `Utente`.
     * Imposta i binding matematici per dividere equamente lo spazio tra le colonne.
     * * @param utenti La lista osservabile degli utenti da mostrare.
     */
    public UtentiView(ObservableList<Utente> utenti) {
        this.setPadding(new Insets(15));

        TableColumn<Utente, String> colMatr = new TableColumn<>("Matricola");
        colMatr.setCellValueFactory(new PropertyValueFactory<>("matricola"));

        TableColumn<Utente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Utente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn<Utente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Utente, Integer> colDisp = new TableColumn<>("Prestiti Disp.");
        colDisp.setCellValueFactory(cell -> new SimpleObjectProperty<>(3 - cell.getValue().getPrestitiAttivi().size()));

        // Styling identico a LibriView (Copia/Incolla logica colori)
        colDisp.setCellFactory(column -> new TableCell<Utente, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    setText(item.toString());

                    // LOGICA COLORI:
                    if (item > 0) {
                        setTextFill(Color.GREEN);
                        // Allineamento al centro + font normale
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: normal;");
                    } else {
                        setTextFill(Color.RED);
                        // Allineamento al centro + font GRASSETTO per evidenziare il blocco
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Aggiungo tutte e 5 le colonne
        tabella.getColumns().addAll(colMatr, colNome, colCognome, colEmail, colDisp);
        tabella.setItems(utenti);

        // --- BINDING MATEMATICO AGGIORNATO ---
        // Ora dividiamo per 5 colonne
        colMatr.prefWidthProperty().bind(tabella.widthProperty().divide(5).subtract(1));
        colNome.prefWidthProperty().bind(tabella.widthProperty().divide(5).subtract(1));
        colCognome.prefWidthProperty().bind(tabella.widthProperty().divide(5).subtract(1));
        colEmail.prefWidthProperty().bind(tabella.widthProperty().divide(5).subtract(1));
        colDisp.prefWidthProperty().bind(tabella.widthProperty().divide(5).subtract(1)); // Binding nuova colonna

        // Blocca il ridimensionamento manuale e allinea a sinistra le colonne di testo
        for (TableColumn<?, ?> col : tabella.getColumns()) {
            col.setResizable(false);
            if (col != colDisp) { // La colonna disp ha già il suo stile centrato
                col.setStyle("-fx-alignment: CENTER-LEFT;");
            }
        }




        // Top Layout
        HBox navBar = new HBox(15, btnIndietro, new Label("ANAGRAFICA UTENTI"));
        navBar.setAlignment(Pos.CENTER_LEFT);

        txtRicerca.setPromptText("Testo da cercare...");
        cmbCriterio.getItems().addAll("Cognome", "Matricola", "Email");
        cmbCriterio.setValue("Cognome");

        HBox searchBar = new HBox(10, new Label("Filtra per:"), cmbCriterio, txtRicerca, btnCerca);
        searchBar.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(navBar);
        topPane.setRight(searchBar);
        topPane.setPadding(new Insets(0,0,15,0));

        // Bottom Layout
        btnElimina.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        HBox bottomBar = new HBox(15, btnNuovo, btnElimina);
        bottomBar.setPadding(new Insets(15,0,0,0));

        this.setTop(topPane);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    /**
     * @brief Restituisce il pulsante Indietro.
     * @return Button navigazione.
     */
    public Button getBtnIndietro() { return btnIndietro; }

    /**
     * @brief Restituisce il pulsante Nuovo Utente.
     * @return Button apertura form creazione.
     */
    public Button getBtnNuovo() { return btnNuovo; }

    /**
     * @brief Restituisce il pulsante Elimina.
     * @return Button cancellazione utente selezionato.
     */
    public Button getBtnElimina() { return btnElimina; } // <--- Getter

    /**
     * @brief Restituisce la tabella utenti.
     * @return TableView per la gestione della selezione.
     */
    public TableView<Utente> getTabella() { return tabella; }

    /**
     * @brief Restituisce il campo di ricerca testo.
     * @return TextField input utente.
     */
    public TextField getTxtRicerca() { return txtRicerca; }

    /**
     * @brief Restituisce il filtro di ricerca selezionato.
     * @return ComboBox criteri (Cognome, Matricola, Email).
     */
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }

    /**
     * @brief Restituisce il pulsante di ricerca.
     * @return Button azione filtro.
     */
    public Button getBtnCerca() { return btnCerca; }

    /**
     * @brief Forza l'aggiornamento grafico della tabella.
     */
    public void refresh() {
        tabella.refresh();
    }
}