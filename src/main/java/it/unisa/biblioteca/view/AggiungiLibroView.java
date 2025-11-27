package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Schermata per l'aggiunta di un nuovo libro al catalogo.
 */
public class AggiungiLibroView extends VBox {

    private TextField txtTitolo = new TextField();
    private TextField txtAutori = new TextField();
    private TextField txtIsbn = new TextField();
    private TextField txtCopie = new TextField();
    private DatePicker datePicker = new DatePicker();

    private Button btnSalva = new Button("Salva Libro");
    private Button btnAnnulla = new Button("Annulla");

    public AggiungiLibroView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Titolo Opera:"), txtTitolo);
        grid.addRow(1, new Label("Autori (separati da virgola):"), txtAutori);
        grid.addRow(2, new Label("Codice ISBN:"), txtIsbn);
        grid.addRow(3, new Label("Data Pubblicazione:"), datePicker);
        grid.addRow(4, new Label("Copie Iniziali:"), txtCopie);

        HBox buttons = new HBox(15, btnAnnulla, btnSalva);
        buttons.setAlignment(Pos.CENTER);

        btnSalva.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        this.getChildren().addAll(new Label("AGGIUNGI NUOVO LIBRO"), grid, buttons);
    }

    public TextField getTxtTitolo() { return txtTitolo; }
    public TextField getTxtAutori() { return txtAutori; }
    public TextField getTxtIsbn() { return txtIsbn; }
    public TextField getTxtCopie() { return txtCopie; }
    public DatePicker getDatePicker() { return datePicker; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}