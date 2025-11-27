package it.unisa.biblioteca.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Button btnInfo = new Button("ℹ️ Info Gruppo 11");
    // Il pulsante salva è stato rimosso come richiesto (gestito in automatico)

    public HomeView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(25);
        this.setStyle("-fx-background-color: #ecf0f1;");

        Label title = new Label("Biblioteca Ingegneria - Gruppo 11");
        title.setFont(new Font("Arial", 32));
        title.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        // Stile uniforme per i 4 bottoni
        configuraBottone(btnLibri, "#3498db");
        configuraBottone(btnUtenti, "#e67e22");
        configuraBottone(btnPrestiti, "#27ae60");
        configuraBottone(btnInfo, "#7f8c8d");

        this.getChildren().addAll(title, btnLibri, btnUtenti, btnPrestiti, btnInfo);
    }

    private void configuraBottone(Button b, String colorHex) {
        b.setPrefSize(300, 65); // Grandi e cliccabili
        b.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
    }

    public Button getBtnLibri() { return btnLibri; }
    public Button getBtnUtenti() { return btnUtenti; }
    public Button getBtnPrestiti() { return btnPrestiti; }
    public Button getBtnInfo() { return btnInfo; }
}