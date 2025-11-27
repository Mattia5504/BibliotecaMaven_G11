package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Schermata per registrare un nuovo utente.
 */
public class AggiungiUtenteView extends VBox {

    private TextField txtNome = new TextField();
    private TextField txtCognome = new TextField();
    private TextField txtMatricola = new TextField();
    private TextField txtEmail = new TextField();

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

        grid.addRow(0, new Label("Nome:"), txtNome);
        grid.addRow(1, new Label("Cognome:"), txtCognome);
        grid.addRow(2, new Label("Matricola:"), txtMatricola);
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
    public TextField getTxtEmail() { return txtEmail; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}