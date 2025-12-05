package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * @brief Schermata avanzata per la creazione di un prestito.
 * * Utilizza un layout a "Vista Divisa" (Split View):
 * - **Sinistra**: Elenco Utenti ricercabile.
 * - **Destra**: Catalogo Libri ricercabile.
 * * L'operatore seleziona un elemento da entrambe le liste e conferma l'associazione.
 */
public class AggiungiPrestitoView extends BorderPane {

    // --- LATO SINISTRO: UTENTI ---
    private TableView<Utente> tableUtenti = new TableView<>();
    private TextField txtSearchUtente = new TextField();
    private ComboBox<String> cmbFilterUtente = new ComboBox<>();
    private Button btnCercaUtente = new Button("🔍");

    // --- LATO DESTRO: LIBRI ---
    private TableView<Libro> tableLibri = new TableView<>();
    private TextField txtSearchLibro = new TextField();
    private ComboBox<String> cmbFilterLibro = new ComboBox<>();
    private Button btnCercaLibro = new Button("🔍");

    // --- BOTTONI AZIONE ---
    private Button btnSalva = new Button("✅ Conferma Prestito");
    private Button btnAnnulla = new Button("❌ Annulla");

    /**
     * @brief Costruttore della AggiungiPrestitoView.
     * * Inizializza i due pannelli laterali e la barra di azione inferiore.
     * Configura la visualizzazione condizionale per la disponibilità dei libri (rosso se 0).
     * * @param utenti Lista osservabile utenti.
     * @param libri Lista osservabile libri.
     */
    public AggiungiPrestitoView(ObservableList<Utente> utenti, ObservableList<Libro> libri) {
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f4f6f7;"); // Sfondo leggero

        // --- TITOLO ---
        Label lblTitolo = new Label("NUOVO PRESTITO: Seleziona Utente e Libro");
        lblTitolo.setFont(new Font("Arial", 24));
        lblTitolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        BorderPane.setAlignment(lblTitolo, Pos.CENTER);
        this.setTop(lblTitolo);

        // --- COSTRUZIONE PANNELLI ---
        VBox panelUtenti = creaPannelloUtenti(utenti);
        VBox panelLibri = creaPannelloLibri(libri);

        // Layout Orizzontale che contiene i due pannelli
        HBox splitLayout = new HBox(20, panelUtenti, panelLibri);
        splitLayout.setAlignment(Pos.CENTER);
        splitLayout.setPadding(new Insets(20, 0, 20, 0));

        // Faccio in modo che i due pannelli occupino il 50% dello spazio ciascuno
        HBox.setHgrow(panelUtenti, Priority.ALWAYS);
        HBox.setHgrow(panelLibri, Priority.ALWAYS);

        this.setCenter(splitLayout);

        // --- BARRA IN BASSO (BOTTONI) ---
        HBox bottomBar = new HBox(20, btnAnnulla, btnSalva);
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(10));

        // Stile bottoni "Call to Action"
        btnSalva.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 10 30;");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 10 30;");

        this.setBottom(bottomBar);
    }

    // --- HELPER PER CREARE IL PANNELLO UTENTI (SINISTRA) ---
    private VBox creaPannelloUtenti(ObservableList<Utente> data) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label header = new Label("1. Seleziona Studente");
        header.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold; -fx-font-size: 16px;");

        // Barra Ricerca
        txtSearchUtente.setPromptText("Cerca studente...");
        cmbFilterUtente.getItems().addAll("Cognome", "Matricola");
        cmbFilterUtente.setValue("Cognome");
        HBox searchBox = new HBox(5, cmbFilterUtente, txtSearchUtente, btnCercaUtente);

        // Tabella Mini
        TableColumn<Utente, String> colMatr = new TableColumn<>("Matr.");
        colMatr.setCellValueFactory(new PropertyValueFactory<>("matricola"));

        TableColumn<Utente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn<Utente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        tableUtenti.getColumns().addAll(colMatr, colCognome, colNome);
        tableUtenti.setItems(data);
        tableUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Adatta colonne
        VBox.setVgrow(tableUtenti, Priority.ALWAYS); // Occupa tutto lo spazio verticale

        box.getChildren().addAll(header, searchBox, tableUtenti);
        return box;
    }

    // --- HELPER PER CREARE IL PANNELLO LIBRI (DESTRA) ---
    private VBox creaPannelloLibri(ObservableList<Libro> data) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label header = new Label("2. Seleziona Libro");
        header.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold; -fx-font-size: 16px;");

        // Barra Ricerca
        txtSearchLibro.setPromptText("Cerca libro...");
        cmbFilterLibro.getItems().addAll("Titolo", "ISBN");
        cmbFilterLibro.setValue("Titolo");
        HBox searchBox = new HBox(5, cmbFilterLibro, txtSearchLibro, btnCercaLibro);

        // Tabella Mini
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));

        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Libro, Integer> colDisp = new TableColumn<>("Disp.");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));
        // Coloro di rosso se non disponibile (opzionale, ma chicca da designer)
        colDisp.setCellFactory(column -> new TableCell<Libro, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setTextFill(item > 0 ? Color.GREEN : Color.RED);
                    setStyle(item > 0 ? "-fx-font-weight: normal;" : "-fx-font-weight: bold;");
                }
            }
        });

        tableLibri.getColumns().addAll(colTitolo, colIsbn, colDisp);
        tableLibri.setItems(data);
        tableLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableLibri, Priority.ALWAYS);

        box.getChildren().addAll(header, searchBox, tableLibri);
        return box;
    }

    /**
     * @brief Restituisce la tabella degli utenti.
     * @return TableView utenti.
     */
    public TableView<Utente> getTableUtenti() { return tableUtenti; }

    /**
     * @brief Restituisce il campo di ricerca utente.
     * @return TextField.
     */
    public TextField getTxtSearchUtente() { return txtSearchUtente; }

    /**
     * @brief Restituisce il filtro di ricerca utente.
     * @return ComboBox.
     */
    public ComboBox<String> getCmbFilterUtente() { return cmbFilterUtente; }

    /**
     * @brief Restituisce il pulsante di ricerca utente.
     * @return Button.
     */
    public Button getBtnCercaUtente() { return btnCercaUtente; }

    // --- GETTERS (Lato Destro) ---

    /**
     * @brief Restituisce la tabella dei libri.
     * @return TableView libri.
     */
    public TableView<Libro> getTableLibri() { return tableLibri; }

    /**
     * @brief Restituisce il campo di ricerca libro.
     * @return TextField.
     */
    public TextField getTxtSearchLibro() { return txtSearchLibro; }

    /**
     * @brief Restituisce il filtro di ricerca libro.
     * @return ComboBox.
     */
    public ComboBox<String> getCmbFilterLibro() { return cmbFilterLibro; }

    /**
     * @brief Restituisce il pulsante di ricerca libro.
     * @return Button.
     */
    public Button getBtnCercaLibro() { return btnCercaLibro; }

    // --- GETTERS (Azioni) ---

    /**
     * @brief Restituisce il pulsante di conferma.
     * @return Button conferma.
     */
    public Button getBtnSalva() { return btnSalva; }

    /**
     * @brief Restituisce il pulsante di annullamento.
     * @return Button annulla.
     */
    public Button getBtnAnnulla() { return btnAnnulla; }

}