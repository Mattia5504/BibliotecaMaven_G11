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

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();
    private Button btnCerca = new Button("🔍 Cerca"); // <--- NUOVO

    public UtentiView(ObservableList<Utente> utenti) {
        this.setPadding(new Insets(15));

        // Colonne
        TableColumn<Utente, String> colMatr = new TableColumn<>("Matricola");
        colMatr.setCellValueFactory(new PropertyValueFactory<>("matricola"));

        TableColumn<Utente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Utente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn<Utente, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEmail.setPrefWidth(220);

        tabella.getColumns().addAll(colMatr, colNome, colCognome, colEmail);
        tabella.setItems(utenti);

        // --- TOP LAYOUT ---
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

        // Bottom
        HBox bottomBar = new HBox(15, btnNuovo);
        bottomBar.setPadding(new Insets(15,0,0,0));

        this.setTop(topPane);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public TableView<Utente> getTabella() { return tabella; }
    public TextField getTxtRicerca() { return txtRicerca; }
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }
    public Button getBtnCerca() { return btnCerca; } // <--- Getter aggiunto
}