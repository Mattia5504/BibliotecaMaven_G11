package it.unisa.biblioteca.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * @brief Form per la registrazione di un nuovo utente.
 * * Questa schermata presenta un modulo di inserimento dati strutturato su un GridPane.
 * Include logiche di feedback visivo, come il contatore dei caratteri per la matricola.
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

    /**
     * @brief Costruttore della view.
     * * Inizializza i campi di testo e li dispone in una griglia.
     * Posiziona accanto al campo Matricola una label per il conteggio dei caratteri.
     */
    public AggiungiUtenteView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setPadding(new Insets(30));

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // --- ASSEGNAZIONE ID PER TESTFX ---
        txtNome.setId("txtNome");           // <--- NUOVO
        txtCognome.setId("txtCognome");     // <--- NUOVO
        txtMatricola.setId("txtMatricola"); // <--- NUOVO
        txtEmail.setId("txtEmail");         // <--- NUOVO
        btnSalva.setId("btnSalva");         // <--- NUOVO
        // ----------------------------------

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

    /**
     * @brief Restituisce il campo d'inserimento nome.
     * @return TextField per l'inserimento.
     */
    public TextField getTxtNome() { return txtNome; }

    /**
     * @brief Restituisce il campo d'inserimento cognome.
     * @return TextField per l'inserimento.
     */
    public TextField getTxtCognome() { return txtCognome; }

    /**
     * @brief Restituisce il campo d'inserimento matricola.
     * @return TextField per l'inserimento.
     */
    public TextField getTxtMatricola() { return txtMatricola; }

    /**
     * @brief Restituisce la label contatore per la matricola.
     * * Il controller userà questa label per mostrare quanti caratteri sono stati digitati (es. "5 su 10")
     * e cambiarne il colore (Verde/Rosso).
     * @return Label di feedback.
     */
    public Label getLblContatoreMatr() { return lblContatoreMatr; } // <--- Getter Fondamentale

    /**
     * @brief Restituisce il campo d'inserimento email.
     * @return TextField per l'inserimento.
     */
    public TextField getTxtEmail() { return txtEmail; }

    /**
     *
     * @brief Restituisce il bottone per effettuare il salvataggio
     * @return Button per il salvataggio
     */
    public Button getBtnSalva() { return btnSalva; }

    /**
     *
     * @brief Restituisce il bottone per annullare la scelta
     * @return Button per annullare
     */
    public Button getBtnAnnulla() { return btnAnnulla; }
}