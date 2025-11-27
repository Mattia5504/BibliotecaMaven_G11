package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

/**
 * Schermata per la creazione di un nuovo prestito.
 */
public class AggiungiPrestitoView extends VBox {

    private ComboBox<Utente> comboUtenti = new ComboBox<>();
    private ComboBox<Libro> comboLibri = new ComboBox<>();
    private Button btnSalva = new Button("Conferma Prestito");
    private Button btnAnnulla = new Button("Annulla");

    public AggiungiPrestitoView(ObservableList<Utente> utenti, ObservableList<Libro> libri) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        // Configuro le ComboBox
        comboUtenti.setItems(utenti);
        comboUtenti.setPrefWidth(300);
        comboUtenti.setPromptText("Seleziona lo studente...");
        comboUtenti.setConverter(new StringConverter<Utente>() {
            @Override public String toString(Utente u) {
                return (u == null) ? "" : u.getNome() + " " + u.getCognome() + " (" + u.getMatricola() + ")";
            }
            @Override public Utente fromString(String s) { return null; }
        });

        comboLibri.setItems(libri);
        comboLibri.setPrefWidth(300);
        comboLibri.setPromptText("Seleziona il libro...");
        comboLibri.setConverter(new StringConverter<Libro>() {
            @Override public String toString(Libro l) {
                return (l == null) ? "" : l.getTitolo() + " (Disp: " + l.getDisponibilita() + ")";
            }
            @Override public Libro fromString(String s) { return null; }
        });

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Studente:"), comboUtenti);
        grid.addRow(1, new Label("Libro da prestare:"), comboLibri);

        HBox buttons = new HBox(15, btnAnnulla, btnSalva);
        buttons.setAlignment(Pos.CENTER);

        btnSalva.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        this.getChildren().addAll(new Label("NUOVO PRESTITO"), grid, buttons);
    }

    public ComboBox<Utente> getComboUtenti() { return comboUtenti; }
    public ComboBox<Libro> getComboLibri() { return comboLibri; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}