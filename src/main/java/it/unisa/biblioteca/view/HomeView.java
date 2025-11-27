package it.unisa.biblioteca.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Vista Principale (Dashboard).
 * Usiamo un VBox per impilare titolo e pulsanti al centro.
 */
public class HomeView extends VBox {

    // Bottoni pubblici per essere accessibili dal Controller
    private Button btnLibri = new Button("📚 Gestione Libri");
    private Button btnUtenti = new Button("👤 Gestione Utenti");
    private Button btnPrestiti = new Button("🔄 Gestione Prestiti");
    private Button btnInfo = new Button("ℹ️ Info Gruppo 11");
    private Button btnSalva = new Button("💾 Salva Dati"); // <--- ECCOLO!

    public HomeView() {
        // Configurazione Layout
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20); // Spazio tra i bottoni
        this.setStyle("-fx-background-color: #ecf0f1;"); // Grigio chiaro moderno

        // Titolo
        Label title = new Label("Biblioteca Ingegneria - Gruppo 11");
        title.setFont(new Font("Arial", 32));
        title.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");

        // Configuro lo stile dei bottoni
        applicaStile(btnLibri, "#3498db");   // Blu
        applicaStile(btnUtenti, "#e67e22");  // Arancione
        applicaStile(btnPrestiti, "#27ae60"); // Verde
        applicaStile(btnInfo, "#7f8c8d");    // Grigio
        applicaStile(btnSalva, "#8e44ad");   // Viola per il salvataggio

        // Aggiungo tutto al layout
        this.getChildren().addAll(title, btnLibri, btnUtenti, btnPrestiti, btnInfo, btnSalva);
    }

    // Metodo helper per non ripetere il CSS
    private void applicaStile(Button b, String colorHex) {
        b.setPrefSize(280, 55); // Leggermente più compatti
        b.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 10;");
    }

    // --- GETTERS (Fondamentali per il Controller) ---
    public Button getBtnLibri() { return btnLibri; }
    public Button getBtnUtenti() { return btnUtenti; }
    public Button getBtnPrestiti() { return btnPrestiti; }
    public Button getBtnInfo() { return btnInfo; }
    public Button getBtnSalva() { return btnSalva; } // <--- Ora il metodo esiste!
}