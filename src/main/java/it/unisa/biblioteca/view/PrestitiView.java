package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Prestito;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color; // Fondamentale per i colori
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * @brief Vista per la gestione del Registro Prestiti.
 * * Visualizza lo storico dei prestiti attivi. Le colonne della tabella non mappano
 * direttamente proprietà semplici, ma estraggono dati combinati dagli oggetti `Utente` e `Libro` associati.
 */
public class PrestitiView extends BorderPane {

    private TableView<Prestito> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Prestito");
    private Button btnElimina = new Button("🗑 Elimina / Concludi");

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca");

    /**
     * @brief Costruttore della PrestitiView.
     * * Configura le colonne della tabella:
     * - **Utente**: Concatena Nome e Cognome dell'utente.
     * - **Libro**: Mostra il titolo del libro.
     * - **Scadenza**: Mostra la data di fine prevista.
     * * @param prestiti Lista osservabile dei prestiti.
     */
    public PrestitiView(ObservableList<Prestito> prestiti) {
        this.setPadding(new Insets(15));

        // 1. Colonna Utente
        TableColumn<Prestito, String> colUtente = new TableColumn<>("Utente");
        colUtente.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getUtente().getNome() + " " + cell.getValue().getUtente().getCognome()
        ));
        colUtente.setPrefWidth(200);

        // 2. Colonna Libro
        TableColumn<Prestito, String> colLibro = new TableColumn<>("Libro");
        colLibro.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLibro().getTitolo()));
        colLibro.setPrefWidth(250);

        // 3. Colonna Scadenza (Data pura)
        TableColumn<Prestito, String> colDataFine = new TableColumn<>("Scadenza");
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFinePrevista"));
        colDataFine.setStyle("-fx-alignment: CENTER;");

        // 4. NUOVA COLONNA: TEMPO RIMANENTE (Con Colori)
        // Passiamo un Long (numero di giorni) al posto di una Stringa, così possiamo fare i calcoli nel CellFactory
        TableColumn<Prestito, Long> colTempo = new TableColumn<>("Stato / Giorni");
        colTempo.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().giorniAllaScadenza()));

        colTempo.setCellFactory(column -> new TableCell<Prestito, Long>() {
            @Override
            protected void updateItem(Long giorni, boolean empty) {
                super.updateItem(giorni, empty);

                if (empty || giorni == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    // LOGICA COLORI E TESTO
                    if (giorni < 0) {
                        // SCADUTO (Numeri negativi) -> ROSSO
                        setText("SCADUTO DA " + Math.abs(giorni) + " gg");
                        setTextFill(Color.RED);
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;"); // Grassetto per evidenziare
                    } else if (giorni == 0) {
                        // SCADE OGGI -> ARANCIONE/ROSSO
                        setText("SCADE OGGI");
                        setTextFill(Color.ORANGERED);
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                    } else {
                        // IN CORSO  non scaduto ->  VERDE
                        setText("Mancano " + giorni + " gg");
                        setTextFill(Color.GREEN);
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: normal;");
                    }
                }
            }
        });
        colTempo.setPrefWidth(180);


        // Aggiungo colonne
        tabella.getColumns().addAll(colUtente, colLibro, colDataFine, colTempo);
        tabella.setItems(prestiti);


        // --- LAYOUT E COMPONENTI (Invariati) ---
        HBox navBar = new HBox(15, btnIndietro, new Label("REGISTRO PRESTITI"));
        navBar.setAlignment(Pos.CENTER_LEFT);

        txtRicerca.setPromptText("Testo da cercare...");
        cmbCriterio.getItems().addAll("Utente (Cognome)", "Libro (Titolo)");
        cmbCriterio.setValue("Utente (Cognome)");

        HBox searchBar = new HBox(10, new Label("Cerca in:"), cmbCriterio, txtRicerca, btnCerca);
        searchBar.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(navBar);
        topPane.setRight(searchBar);
        topPane.setPadding(new Insets(0,0,15,0));

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
     * @brief Restituisce il pulsante Nuovo Prestito.
     * @return Button creazione.
     */
    public Button getBtnNuovo() { return btnNuovo; }

    /**
     * @brief Restituisce il pulsante Elimina/Concludi.
     * @return Button rimozione.
     */
    public Button getBtnElimina() { return btnElimina; }

    /**
     * @brief Restituisce la tabella dei prestiti.
     * @return TableView.
     */
    public TableView<Prestito> getTabella() { return tabella; }

    /**
     * @brief Restituisce il campo di ricerca.
     * @return TextField ricerca.
     */
    public TextField getTxtRicerca() { return txtRicerca; }

    /**
     * @brief Restituisce il selettore del criterio di ricerca.
     * @return ComboBox filtri.
     */
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }

    /**
     * @brief Restituisce il pulsante Cerca.
     * @return Button esecuzione filtro.
     */
    public Button getBtnCerca() { return btnCerca; }

}