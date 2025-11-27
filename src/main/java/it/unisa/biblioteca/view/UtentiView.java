package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class UtentiView extends BorderPane {
    private TableView<Utente> tabella = new TableView<>();
    private Button btnIndietro = new Button("Indietro");
    private Button btnNuovo = new Button("Registra Nuovo Utente");

    public UtentiView(ObservableList<Utente> utenti) {
        TableColumn<Utente, String> colMatr = new TableColumn<>("Matricola");
        colMatr.setCellValueFactory(new PropertyValueFactory<>("matricola"));

        TableColumn<Utente, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Utente, String> colCognome = new TableColumn<>("Cognome");
        colCognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        tabella.getColumns().addAll(colMatr, colNome, colCognome);
        tabella.setItems(utenti);

        this.setPadding(new Insets(10));
        this.setTop(btnIndietro);
        this.setCenter(tabella);

        HBox bottom = new HBox(10, btnNuovo);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        this.setBottom(bottom);
    }

    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
}