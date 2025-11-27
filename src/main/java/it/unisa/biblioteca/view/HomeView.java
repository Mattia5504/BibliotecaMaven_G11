package it.unisa.biblioteca.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeView extends VBox {
    private Button btnLibri = new Button("Gestione Libri");
    private Button btnUtenti = new Button("Gestione Utenti");
    private Button btnPrestiti = new Button("Gestione Prestiti");

    public HomeView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        Label title = new Label("Biblioteca Ingegneria");
        title.setFont(new Font(30));

        btnLibri.setPrefSize(200, 60);
        btnUtenti.setPrefSize(200, 60);
        btnPrestiti.setPrefSize(200, 60);

        this.getChildren().addAll(title, btnLibri, btnUtenti, btnPrestiti);
    }

    public Button getBtnLibri() { return btnLibri; }
    public Button getBtnUtenti() { return btnUtenti; }
    public Button getBtnPrestiti() { return btnPrestiti; }
}