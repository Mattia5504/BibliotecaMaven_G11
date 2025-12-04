package it.unisa.biblioteca.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Dashboard Principale - Gruppo 11.
 * Layout pulito a 4 pulsanti centrali.
 */
public class HomeView extends VBox {

    private Button btnLibri = new Button("📚 Gestione Libri");
    private Button btnUtenti = new Button("👤 Gestione Utenti");
    private Button btnPrestiti = new Button("🔄 Gestione Prestiti");
    private Button btnInfo = new Button("Informazioni");
    // Il pulsante salva è stato rimosso come richiesto (gestito in automatico)

    public HomeView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        this.setStyle("-fx-background-color: #ecf0f1;");

        Label title = new Label("GESTIONALE BIBLIOTECA");

        // Ho aumentato la size a 36px e cambiato il font in Verdana per farti vedere la differenza.
        // Se ti piace, puoi rimettere 'Segoe UI' al posto di 'Verdana'.
        title.setStyle("-fx-font-family: 'Segoe UI', sans-serif; " +
                "-fx-font-size: 45px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #2c3e50; " +
                "-fx-padding: 0 0 20 0;");


        // Stile uniforme per i 4 bottoni
        configuraBottone(btnLibri, "#3498db");
        configuraBottone(btnUtenti, "#e67e22");
        configuraBottone(btnPrestiti, "#27ae60");
        configuraBottone(btnInfo, "#7f8c8d");

        HBox prima = new HBox();
        prima.setAlignment(Pos.CENTER);
        prima.setSpacing(10);

        HBox seconda = new HBox();
        seconda.setAlignment(Pos.CENTER);
        seconda.setSpacing(10);

        prima.getChildren().addAll(btnLibri,btnUtenti);
        seconda.getChildren().addAll(btnPrestiti, btnInfo);


        this.getChildren().addAll(title, prima, seconda);
    }

    private void configuraBottone(Button b, String colorHex) {
        b.setPrefSize(400, 120); // Grandi e cliccabili
        b.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
    }

    public Button getBtnLibri() { return btnLibri; }
    public Button getBtnUtenti() { return btnUtenti; }
    public Button getBtnPrestiti() { return btnPrestiti; }
    public Button getBtnInfo() { return btnInfo; }
}