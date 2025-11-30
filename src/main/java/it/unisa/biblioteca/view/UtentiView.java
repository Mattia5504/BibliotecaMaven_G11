package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class UtentiView extends BorderPane {

    private TableView<Utente> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Utente");
    private Button btnElimina = new Button("🗑 Elimina Utente"); // <--- NUOVO

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca");

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






        tabella.getColumns().addAll(colMatr, colNome, colCognome, colEmail);
        tabella.setItems(utenti);

        //--- BINDING MATEMATICO ---
        //forza ogni colonna a essere esattamente 1/4 della larghezza totale
        // subtract(2) serve a evitare la comparsa della scrollbar orizzontale per pochi pixel
        colMatr.prefWidthProperty().bind(tabella.widthProperty().divide(4).subtract(1));
        colNome.prefWidthProperty().bind(tabella.widthProperty().divide(4).subtract(1));
        colCognome.prefWidthProperty().bind(tabella.widthProperty().divide(4).subtract(1));
        colEmail.prefWidthProperty().bind(tabella.widthProperty().divide(4).subtract(1));


        // Blocca il ridimensionamento manuale
        for (TableColumn<?, ?> col : tabella.getColumns()) {
            col.setResizable(false);
            col.setStyle("-fx-alignment: CENTER-LEFT;");
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

    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public Button getBtnElimina() { return btnElimina; } // <--- Getter
    public TableView<Utente> getTabella() { return tabella; }
    public TextField getTxtRicerca() { return txtRicerca; }
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }
    public Button getBtnCerca() { return btnCerca; }
}