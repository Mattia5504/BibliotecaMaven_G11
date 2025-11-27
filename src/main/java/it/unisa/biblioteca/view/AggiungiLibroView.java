package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Schermata per l'aggiunta di un nuovo libro.
 * Aggiornata con contatore caratteri per ISBN.
 */
public class AggiungiLibroView extends VBox {

    private TextField txtTitolo = new TextField();
    private TextField txtAutori = new TextField();
    private TextField txtIsbn = new TextField();
    private TextField txtCopie = new TextField();
    private DatePicker datePicker = new DatePicker();

    // Label per il feedback in tempo reale (es. "5 su 13")
    private Label lblContatoreIsbn = new Label("0 su 13");

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

        // Creiamo un contenitore orizzontale per ISBN + Contatore
        HBox boxIsbn = new HBox(10, txtIsbn, lblContatoreIsbn);
        boxIsbn.setAlignment(Pos.CENTER_LEFT);
        lblContatoreIsbn.setTextFill(Color.GRAY); // Colore neutro iniziale

        grid.addRow(0, new Label("Titolo Opera:"), txtTitolo);
        grid.addRow(1, new Label("Autori (virgola):"), txtAutori);
        grid.addRow(2, new Label("Codice ISBN:"), boxIsbn); // Inserisco il box, non solo il testo
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
    public Label getLblContatoreIsbn() { return lblContatoreIsbn; } // <--- Getter Fondamentale
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}