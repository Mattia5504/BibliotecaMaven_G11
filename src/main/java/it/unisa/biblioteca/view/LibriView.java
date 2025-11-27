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

public class LibriView extends BorderPane {

    private TableView<Libro> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Aggiungi Libro");
    private Button btnPiu = new Button("+ Copie");
    private Button btnMeno = new Button("- Copie");

    // --- ELEMENTI RICERCA ---
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca"); // <--- NUOVO TASTO

    public LibriView(ObservableList<Libro> libri) {
        this.setPadding(new Insets(15));

        // Setup Tabella
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colTitolo.setPrefWidth(250);

        TableColumn<Libro, String> colAutori = new TableColumn<>("Autore/i");
        colAutori.setCellValueFactory(cell -> new SimpleStringProperty(String.join(", ", cell.getValue().getAutori())));
        colAutori.setPrefWidth(200);

        TableColumn<Libro, Integer> colAnno = new TableColumn<>("Anno");
        colAnno.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDataPubblicazione().getYear()));

        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colIsbn.setPrefWidth(120);

        TableColumn<Libro, Integer> colDisp = new TableColumn<>("Disp.");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        tabella.getColumns().addAll(colTitolo, colAutori, colAnno, colIsbn, colDisp);
        tabella.setItems(libri);

        // --- TOP BAR ---
        HBox navBar = new HBox(15, btnIndietro, new Label("CATALOGO LIBRI"));
        navBar.setAlignment(Pos.CENTER_LEFT);

        // Barra Ricerca
        txtRicerca.setPromptText("Testo da cercare...");
        txtRicerca.setPrefWidth(200);
        cmbCriterio.getItems().addAll("Titolo", "ISBN", "Autore", "Anno");
        cmbCriterio.setValue("Titolo");

        // Aggiungo il bottone alla barra
        HBox searchBar = new HBox(10, new Label("Cerca per:"), cmbCriterio, txtRicerca, btnCerca);
        searchBar.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(navBar);
        topPane.setRight(searchBar);
        topPane.setPadding(new Insets(0,0,15,0));

        // --- BOTTOM BAR ---
        HBox bottomBar = new HBox(10, btnNuovo, new Separator(), new Label("Gestione Copie:"), btnPiu, btnMeno);
        bottomBar.setPadding(new Insets(15,0,0,0));
        bottomBar.setAlignment(Pos.CENTER_LEFT);

        this.setTop(topPane);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    public TableView<Libro> getTabella() { return tabella; }
    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public Button getBtnPiu() { return btnPiu; }
    public Button getBtnMeno() { return btnMeno; }
    public TextField getTxtRicerca() { return txtRicerca; }
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }
    public Button getBtnCerca() { return btnCerca; } // <--- Getter aggiunto
    public void refresh() { tabella.refresh(); }
}