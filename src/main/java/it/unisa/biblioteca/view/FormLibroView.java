package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FormLibroView extends VBox {
    private TextField txtTitolo = new TextField();
    private TextField txtAutori = new TextField(); // "Autore1, Autore2"
    private TextField txtIsbn = new TextField();
    private TextField txtCopie = new TextField();
    private DatePicker datePicker = new DatePicker();

    private Button btnSalva = new Button("Salva Libro");
    private Button btnAnnulla = new Button("Annulla");

    public FormLibroView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Titolo:"), txtTitolo);
        grid.addRow(1, new Label("Autori (separati da virgola):"), txtAutori);
        grid.addRow(2, new Label("ISBN:"), txtIsbn);
        grid.addRow(3, new Label("Data Pubblicazione:"), datePicker);
        grid.addRow(4, new Label("Copie Iniziali:"), txtCopie);

        HBox buttons = new HBox(10, btnAnnulla, btnSalva);
        buttons.setAlignment(Pos.CENTER);

        this.getChildren().addAll(new Label("Nuovo Libro"), grid, buttons);
    }

    // Getters per leggere i dati dal controller
    public TextField getTxtTitolo() { return txtTitolo; }
    public TextField getTxtAutori() { return txtAutori; }
    public TextField getTxtIsbn() { return txtIsbn; }
    public TextField getTxtCopie() { return txtCopie; }
    public DatePicker getDatePicker() { return datePicker; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}