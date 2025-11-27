package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Vista Anagrafica Utenti.
 * Simile a LibriView ma per gli oggetti Utente.
 */
public class UtentiView extends BorderPane {

    private TableView<Utente> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Utente");

    public UtentiView(ObservableList<Utente> utenti) {
        this.setPadding(new Insets(15));

        // Colonne Tabella
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

        // Layout
        HBox topBar = new HBox(15, btnIndietro, new Label("ANAGRAFICA UTENTI"));
        topBar.setPadding(new Insets(0,0,15,0));

        HBox bottomBar = new HBox(15, btnNuovo);
        bottomBar.setPadding(new Insets(15,0,0,0));

        this.setTop(topBar);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
}