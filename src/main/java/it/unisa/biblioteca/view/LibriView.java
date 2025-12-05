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
import javafx.scene.paint.Color;

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

        // Impostiamo il CellFactory per colorare il testo in base al valore
        colDisp.setCellFactory(column -> new TableCell<Libro, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(""); // Reset stile
                } else {
                    setText(item.toString());

                    // LOGICA COLORI:
                    if (item > 0) {
                        setTextFill(Color.GREEN);
                        // Mantengo allineamento CENTER + font normale
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: normal;");
                    } else {
                        setTextFill(Color.RED);
                        // Mantengo allineamento CENTER + font GRASSETTO per evidenziare l'esaurimento
                        setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
                    }
                }
            }
        });

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

    public TableView<Libro> getTabella() { return tabella; }
    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public Button getBtnElimina() { return btnElimina; } // <--- Getter
    public Button getBtnPiu() { return btnPiu; }
    public Button getBtnMeno() { return btnMeno; }
    public TextField getTxtRicerca() { return txtRicerca; }
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }
    public Button getBtnCerca() { return btnCerca; }
    public void refresh() { tabella.refresh(); }
}