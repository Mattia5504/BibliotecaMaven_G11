package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Schermata per registrare un nuovo utente.
 * Aggiornata con contatore caratteri per Matricola.
 */
public class AggiungiUtenteView extends VBox {

    private TextField txtNome = new TextField();
    private TextField txtCognome = new TextField();
    private TextField txtMatricola = new TextField();
    private TextField txtEmail = new TextField();

    // Label contatore
    private Label lblContatoreMatr = new Label("0 su 10");

    private Button btnSalva = new Button("Salva Utente");
    private Button btnAnnulla = new Button("Annulla");

    public AggiungiUtenteView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // Box per Matricola + Contatore
        HBox boxMatr = new HBox(10, txtMatricola, lblContatoreMatr);
        boxMatr.setAlignment(Pos.CENTER_LEFT);
        lblContatoreMatr.setTextFill(Color.GRAY);

        grid.addRow(0, new Label("Nome:"), txtNome);
        grid.addRow(1, new Label("Cognome:"), txtCognome);
        grid.addRow(2, new Label("Matricola:"), boxMatr); // Box invece del solo TextField
        grid.addRow(3, new Label("Email Istituzionale:"), txtEmail);

        HBox buttons = new HBox(15, btnAnnulla, btnSalva);
        buttons.setAlignment(Pos.CENTER);

        btnSalva.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnAnnulla.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");

        this.getChildren().addAll(new Label("REGISTRAZIONE NUOVO UTENTE"), grid, buttons);
    }

    public TextField getTxtNome() { return txtNome; }
    public TextField getTxtCognome() { return txtCognome; }
    public TextField getTxtMatricola() { return txtMatricola; }
    public Label getLblContatoreMatr() { return lblContatoreMatr; } // <--- Getter Fondamentale
    public TextField getTxtEmail() { return txtEmail; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}