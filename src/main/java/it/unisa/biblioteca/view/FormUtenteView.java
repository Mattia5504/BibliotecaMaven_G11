package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FormUtenteView extends VBox {
    private TextField txtNome = new TextField();
    private TextField txtCognome = new TextField();
    private TextField txtMatricola = new TextField();
    private TextField txtEmail = new TextField();

    private Button btnSalva = new Button("Salva Utente");
    private Button btnAnnulla = new Button("Annulla");

    public FormUtenteView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(20));

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.addRow(0, new Label("Nome:"), txtNome);
        grid.addRow(1, new Label("Cognome:"), txtCognome);
        grid.addRow(2, new Label("Matricola:"), txtMatricola);
        grid.addRow(3, new Label("Email:"), txtEmail);

        HBox buttons = new HBox(10, btnAnnulla, btnSalva);
        buttons.setAlignment(Pos.CENTER);
        this.getChildren().addAll(new Label("Nuovo Utente"), grid, buttons);
    }

    public TextField getTxtNome() { return txtNome; }
    public TextField getTxtCognome() { return txtCognome; }
    public TextField getTxtMatricola() { return txtMatricola; }
    public TextField getTxtEmail() { return txtEmail; }
    public Button getBtnSalva() { return btnSalva; }
    public Button getBtnAnnulla() { return btnAnnulla; }
}