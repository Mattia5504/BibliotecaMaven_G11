///c'è la possibilità di fare un test delle view TESTFX///

package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @brief Form per l'inserimento di un nuovo libro nel catalogo.
 * * Fornisce campi per titolo, autori, ISBN, copie e un DatePicker per la pubblicazione.
 * Include un contatore visuale per la validazione della lunghezza dell'ISBN.
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

    /**
     * @brief Costruttore della view.
     * * Configura il layout a griglia.
     * Inserisce accanto al campo ISBN una label di stato che verrà controllata dal Controller.
     */
    public AggiungiLibroView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // --- ASSEGNAZIONE ID PER TESTFX ---
        txtTitolo.setId("txtTitolo");       // <--- NUOVO
        txtAutori.setId("txtAutori");       // <--- NUOVO
        txtIsbn.setId("txtIsbn");           // <--- NUOVO
        txtCopie.setId("txtCopie");         // <--- NUOVO
        datePicker.setId("datePicker");     // <--- NUOVO
        btnSalva.setId("btnSalva");         // <--- NUOVO
        // ----------------------------------

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

    /**
     * @brief Restituisce il campo Titolo.
     * @return TextField titolo.
     */
    public TextField getTxtTitolo() { return txtTitolo; }

    /**
     * @brief Restituisce il campo Autori.
     * @return TextField autori.
     */
    public TextField getTxtAutori() { return txtAutori; }

    /**
     * @brief Restituisce il campo ISBN.
     * @return TextField isbn.
     */
    public TextField getTxtIsbn() { return txtIsbn; }

    /**
     * @brief Restituisce il campo Copie.
     * @return TextField copie.
     */
    public TextField getTxtCopie() { return txtCopie; }

    /**
     * @brief Restituisce il selettore di data.
     * @return DatePicker pubblicazione.
     */
    public DatePicker getDatePicker() { return datePicker; }

    /**
     * @brief Restituisce la label contatore per l'ISBN.
     * @return Label per feedback utente.
     */
    public Label getLblContatoreIsbn() { return lblContatoreIsbn; } // <--- Getter Fondamentale

    /**
     * @brief Restituisce il pulsante Salva.
     * @return Button conferma.
     */
    public Button getBtnSalva() { return btnSalva; }

    /**
     * @brief Restituisce il pulsante Annulla.
     * @return Button annulla.
     */
    public Button getBtnAnnulla() { return btnAnnulla; }
}