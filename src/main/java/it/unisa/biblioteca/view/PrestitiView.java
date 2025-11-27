package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Prestito;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class PrestitiView extends BorderPane {

    private TableView<Prestito> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Prestito");

    // Ricerca
    private TextField txtRicerca = new TextField();
    private ComboBox<String> cmbCriterio = new ComboBox<>();

    public PrestitiView(ObservableList<Prestito> prestiti) {
        this.setPadding(new Insets(15));

        TableColumn<Prestito, String> colUtente = new TableColumn<>("Utente");
        colUtente.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getUtente().getNome() + " " + cell.getValue().getUtente().getCognome()
        ));
        colUtente.setPrefWidth(200);

        TableColumn<Prestito, String> colLibro = new TableColumn<>("Libro");
        colLibro.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLibro().getTitolo()));
        colLibro.setPrefWidth(250);

        TableColumn<Prestito, String> colDataFine = new TableColumn<>("Scadenza");
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFinePrevista"));

        tabella.getColumns().addAll(colUtente, colLibro, colDataFine);
        tabella.setItems(prestiti);

        // --- TOP LAYOUT ---
        HBox navBar = new HBox(15, btnIndietro, new Label("REGISTRO PRESTITI"));
        navBar.setAlignment(Pos.CENTER_LEFT);

        txtRicerca.setPromptText("Cerca prestito...");
        cmbCriterio.getItems().addAll("Utente (Cognome)", "Libro (Titolo)");
        cmbCriterio.setValue("Utente (Cognome)");

        HBox searchBar = new HBox(10, new Label("Cerca in:"), cmbCriterio, txtRicerca);
        searchBar.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(navBar);
        topPane.setRight(searchBar);
        topPane.setPadding(new Insets(0,0,15,0));

        HBox bottomBar = new HBox(15, btnNuovo);
        bottomBar.setPadding(new Insets(15,0,0,0));

        this.setTop(topPane);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public TableView<Prestito> getTabella() { return tabella; }
    public TextField getTxtRicerca() { return txtRicerca; }
    public ComboBox<String> getCmbCriterio() { return cmbCriterio; }
}