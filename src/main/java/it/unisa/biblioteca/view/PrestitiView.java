package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Prestito;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Vista Storico Prestiti.
 * Mostra chi ha preso cosa e quando scade.
 */
public class PrestitiView extends BorderPane {

    private TableView<Prestito> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Nuovo Prestito");

    public PrestitiView(ObservableList<Prestito> prestiti) {
        this.setPadding(new Insets(15));

        // Colonne Custom (per mostrare nome invece dell'oggetto Utente)
        TableColumn<Prestito, String> colUtente = new TableColumn<>("Utente");
        colUtente.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getUtente().getNome() + " " + cell.getValue().getUtente().getCognome()
        ));
        colUtente.setPrefWidth(200);

        TableColumn<Prestito, String> colLibro = new TableColumn<>("Libro");
        colLibro.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLibro().getTitolo()));
        colLibro.setPrefWidth(250);

        TableColumn<Prestito, String> colDataFine = new TableColumn<>("Scadenza Prevista");
        colDataFine.setCellValueFactory(new PropertyValueFactory<>("dataFinePrevista"));

        tabella.getColumns().addAll(colUtente, colLibro, colDataFine);
        tabella.setItems(prestiti);

        // Layout
        HBox topBar = new HBox(15, btnIndietro, new Label("REGISTRO PRESTITI"));
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