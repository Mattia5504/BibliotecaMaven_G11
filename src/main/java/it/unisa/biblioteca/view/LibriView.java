package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class LibriView extends BorderPane {
    private TableView<Libro> tabella = new TableView<>();
    private Button btnIndietro = new Button("Indietro");
    private Button btnNuovo = new Button("Aggiungi Nuovo Libro");
    private Button btnPiu = new Button("+ Copie");
    private Button btnMeno = new Button("- Copie");

    public LibriView(ObservableList<Libro> libri) {
        // Setup Tabella
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));

        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Libro, Integer> colDisp = new TableColumn<>("Disp.");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        tabella.getColumns().addAll(colTitolo, colIsbn, colDisp);
        tabella.setItems(libri); // Colleghiamo i dati!

        // Layout
        this.setPadding(new Insets(10));
        this.setTop(btnIndietro);
        this.setCenter(tabella);

        HBox bottom = new HBox(10, btnNuovo, btnPiu, btnMeno);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        this.setBottom(bottom);
    }

    public TableView<Libro> getTabella() { return tabella; }
    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public Button getBtnPiu() { return btnPiu; }
    public Button getBtnMeno() { return btnMeno; }
    public void refresh() { tabella.refresh(); }
}