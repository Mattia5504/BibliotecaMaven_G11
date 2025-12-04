package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * @brief Vista per la gestione del Catalogo Libri.
 * * Questa schermata visualizza l'elenco dei libri in una tabella e fornisce
 * i controlli per la ricerca, l'inserimento, l'eliminazione e la gestione delle copie.
 * * @details
 * **Layout:**
 * - **TOP**: Barra di navigazione (Indietro) e Barra di Ricerca (ComboBox + TextField).
 * - **CENTER**: `TableView` contenente i dati dei libri.
 * - **BOTTOM**: Pulsanti operativi (Nuovo, Elimina, +Copie, -Copie).
 * * **Data Binding:**
 * Le colonne della tabella sono configurate per leggere direttamente le proprietà
 * degli oggetti `Libro`. Campi complessi come la lista autori vengono formattati in stringa.
 * * **Responsive Design:**
 * Le colonne della tabella sono dimensionate percentualmente rispetto alla larghezza
 * della finestra per adattarsi a diverse risoluzioni.
 */

public class LibriView extends BorderPane {

    private TableView<Libro> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Aggiungi Libro");
    private Button btnElimina = new Button("🗑 Elimina Selezionato"); // <--- NUOVO
    private Button btnPiu = new Button("+ Copie");
    private Button btnMeno = new Button("- Copie");

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca");

    /**
     * @brief Costruttore della LibriView.
     * * Inizializza l'interfaccia grafica e collega la tabella ai dati.
     * * Configura le colonne:
     * - **Titolo, ISBN, Disponibilità**: Mappatura diretta.
     * - **Autori**: Concatena la lista di autori in una stringa separata da virgole.
     * - **Anno**: Estrae l'anno dall'oggetto `LocalDate`.
     * * Imposta inoltre i binding per il ridimensionamento automatico delle colonne
     * e lo stile dei componenti (es. bottone elimina rosso).
     * * @param libri La lista osservabile (ObservableList) dei libri da visualizzare nella tabella.
     */
    public LibriView(ObservableList<Libro> libri) {
        this.setPadding(new Insets(15));

        // Colonne
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        //colTitolo.setPrefWidth(250);

        TableColumn<Libro, String> colAutori = new TableColumn<>("Autore/i");
        colAutori.setCellValueFactory(cell -> new SimpleStringProperty(String.join(", ", cell.getValue().getAutori())));
        //colAutori.setPrefWidth(200);

        TableColumn<Libro, Integer> colAnno = new TableColumn<>("Anno");
        colAnno.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDataPubblicazione().getYear()));

        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        //colIsbn.setPrefWidth(120);

        TableColumn<Libro, Integer> colDisp = new TableColumn<>("Disp.");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        tabella.getColumns().addAll(colTitolo, colAutori, colAnno, colIsbn, colDisp);
        tabella.setItems(libri);

        colTitolo.prefWidthProperty().bind(tabella.widthProperty().multiply(0.35).subtract(15));

        // Autori: 30% dello spazio
        colAutori.prefWidthProperty().bind(tabella.widthProperty().multiply(0.30).subtract(15));

        // ISBN: 15% dello spazio
        colIsbn.prefWidthProperty().bind(tabella.widthProperty().multiply(0.15).subtract(15));

        // Anno: 10% dello spazio
        colAnno.prefWidthProperty().bind(tabella.widthProperty().multiply(0.10).subtract(15));

        // Disponibilità: 10% dello spazio
        colDisp.prefWidthProperty().bind(tabella.widthProperty().multiply(0.10).subtract(15));



        tabella.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        for (TableColumn<?, ?> col : tabella.getColumns()) {
            col.setResizable(false);
            col.setStyle("-fx-alignment: CENTER-LEFT;");
        }

        colAnno.setStyle("-fx-alignment: CENTER;");
        colDisp.setStyle("-fx-alignment: CENTER;");

        // Top Bar
        HBox navBar = new HBox(15, btnIndietro, new Label("CATALOGO LIBRI"));
        navBar.setAlignment(Pos.CENTER_LEFT);

        txtRicerca.setPromptText("Testo da cercare...");
        txtRicerca.setPrefWidth(200);
        cmbCriterio.getItems().addAll("Titolo", "ISBN", "Autore", "Anno");
        cmbCriterio.setValue("Titolo");

        HBox searchBar = new HBox(10, new Label("Cerca per:"), cmbCriterio, txtRicerca, btnCerca);
        searchBar.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(navBar);
        topPane.setRight(searchBar);
        topPane.setPadding(new Insets(0,0,15,0));

        // --- BOTTOM BAR AGGIORNATA ---
        // Ho aggiunto btnElimina e l'ho stilizzato rosso
        btnElimina.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");

        HBox bottomBar = new HBox(10, btnNuovo, btnElimina, new Separator(), new Label("Gestione Copie:"), btnPiu, btnMeno);
        bottomBar.setPadding(new Insets(15,0,0,0));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        this.setTop(topPane);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    /**
     * @brief Restituisce la tabella dei libri.
     * * Utile al controller per gestire la selezione (SelectionModel).
     * @return L'oggetto TableView contenente i libri.
     */
    public TableView<Libro> getTabella() { return tabella; }

    /**
     * @brief Restituisce il pulsante Indietro.
     * @return Button per tornare alla Home.
     */
    public Button getBtnIndietro() { return btnIndietro; }

    /**
     * @brief Restituisce il pulsante Nuovo Libro.
     * @return Button per aprire la form di inserimento.
     */
    public Button getBtnNuovo() { return btnNuovo; }

    /**
     * @brief Restituisce il pulsante Elimina.
     * @return Button per rimuovere il libro selezionato.
     */
    public Button getBtnElimina() { return btnElimina; } // <--- Getter

    /**
     * @brief Restituisce il pulsante Incrementa Copie.
     * @return Button per aggiungere disponibilità al libro selezionato.
     */
    public Button getBtnPiu() { return btnPiu; }

    /**
     * @brief Restituisce il pulsante Decrementa Copie.
     * @return Button per ridurre la disponibilità del libro selezionato.
     */
    public Button getBtnMeno() { return btnMeno; }

    /**
     * @brief Restituisce il campo di testo per la ricerca.
     * @return TextField dove l'utente digita la query.
     */
    public TextField getTxtRicerca() { return txtRicerca; }

    /**
     * @brief Restituisce il menu a tendina per il criterio di ricerca.
     * @return ComboBox contenente i filtri (Titolo, ISBN, Autore...).
     */
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }

    /**
     * @brief Restituisce il pulsante di avvio ricerca.
     * @return Button che scatena il filtro sulla tabella.
     */
    public Button getBtnCerca() { return btnCerca; }

    /**
     * @brief Forza l'aggiornamento grafico della tabella.
     * * Da chiamare quando i dati cambiano in modo che la tabella
     * potrebbe non rilevare automaticamente.
     */
    public void refresh() { tabella.refresh(); }
}