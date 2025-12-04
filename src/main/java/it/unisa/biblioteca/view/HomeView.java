package it.unisa.biblioteca.view;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Dashboard Principale - Design "Responsive & Vertical"
 * Layout basato su BorderPane per ancorare gli elementi ai lati e al centro.
 */
public class HomeView extends BorderPane {

    // Pulsanti Gestionali
    private Button btnLibri = new Button("📚 Gestione Libri");
    private Button btnUtenti = new Button("👤 Gestione Utenti");
    private Button btnPrestiti = new Button("🔄 Gestione Prestiti");

    // Pulsanti Utility (Basso Destra)
    private Button btnInfo = new Button("ℹ Info");
    private Button btnEsci = new Button("⏻ Esci");

    private Label title = new Label("GESTIONALE BIBLIOTECA");

    public HomeView() {
        // 1. Configurazione Generale
        this.setStyle("-fx-background-color: linear-gradient(to bottom right, #ecf0f1, #bdc3c7);"); // Sfondo con gradiente leggero
        this.setPadding(new Insets(40)); // Spazio dai bordi dello schermo

        // --- ZONA ALTA (Titolo) ---
        VBox topBox = new VBox(title);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20, 0, 40, 0)); // Spinge il titolo un po' giù

        // STYLING TITOLO: Il font scala dinamicamente (width / 25)
        // Se lo schermo è 1920px -> Font size = 76px. Se 1366px -> Font size = 54px.
        title.styleProperty().bind(Bindings.concat(
                "-fx-font-family: 'Segoe UI'; ",
                "-fx-font-weight: bold; ",
                "-fx-text-fill: #2c3e50; ",
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2); ",
                "-fx-font-size: ", this.widthProperty().divide(30).asString(), "px;"
        ));
        this.setTop(topBox);


        // --- ZONA CENTRALE (Pulsanti Verticali) ---
        VBox centerBox = new VBox(25); // 25px di spazio tra i pulsanti
        centerBox.setAlignment(Pos.CENTER);
        centerBox.getChildren().addAll(btnLibri, btnUtenti, btnPrestiti);

        // Applica lo stile e il binding dimensionale ai 3 pulsanti principali
        styleMainButton(btnLibri, "#3498db"); // Blu
        styleMainButton(btnUtenti, "#e67e22"); // Arancio
        styleMainButton(btnPrestiti, "#27ae60"); // Verde

        this.setCenter(centerBox);


        // --- ZONA BASSA (Info & Esci a Destra) ---
        HBox bottomBox = new HBox(15); // 15px spazio tra i due bottoncini
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.getChildren().addAll(btnInfo, btnEsci);

        // Stile bottoni piccoli
        styleUtilityButton(btnInfo, "#7f8c8d"); // Grigio
        styleUtilityButton(btnEsci, "#c0392b"); // Rosso

        this.setBottom(bottomBox);
    }

    /**
     * Applica stile e logica responsive ai pulsanti principali.
     * Si adattano alla larghezza e altezza della finestra.
     */
    private void configuraResponsiveButton(Button b) {
        // Larghezza: 30% dello schermo (ma mai meno di 300px)
        b.prefWidthProperty().bind(Bindings.max(300, this.widthProperty().multiply(0.3)));

        // Altezza: 10% dell'altezza dello schermo (ma mai meno di 60px)
        b.prefHeightProperty().bind(Bindings.max(60, this.heightProperty().multiply(0.12)));

        // Font size dinamico: Larghezza schermo / 80
        b.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", this.widthProperty().divide(90).asString(), "px;"
        ));
    }

    private void styleMainButton(Button b, String colorHex) {
        // Applichiamo prima la logica di ridimensionamento
        configuraResponsiveButton(b);

        // Poi lo stile CSS base (sovrascriviamo solo il font size nel binding sopra,
        // quindi qui mettiamo le proprietà statiche che non cambiano)
        // Nota: Per combinare CSS statico e dinamico in JavaFX puro servirebbe un CSS esterno o logica complessa.
        // Qui uso un trucco: il binding sopra sovrascrive tutto lo style.
        // Quindi INSERISCO tutto lo stile nel binding dinamico.

        b.styleProperty().bind(Bindings.concat(
                "-fx-background-color: ", colorHex, "; ",
                "-fx-text-fill: white; ",
                "-fx-font-weight: bold; ",
                "-fx-background-radius: 15; ", // Arrotondato
                "-fx-cursor: hand; ",
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 8, 0, 0, 4); ",
                "-fx-font-size: ", this.widthProperty().divide(80).asString(), "px;" // Font responsivo
        ));

        // Effetto Hover (quando passi sopra col mouse)
        b.setOnMouseEntered(e -> b.setOpacity(0.85));
        b.setOnMouseExited(e -> b.setOpacity(1.0));
    }

    private void styleUtilityButton(Button b, String colorHex) {
        b.setPrefSize(120, 50); // Dimensione fissa per questi, devono essere discreti
        b.setStyle(
                "-fx-background-color: " + colorHex + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 16px; " + // Font fisso leggibile
                        "-fx-background-radius: 25; " + // Molto arrotondati (a pillola)
                        "-fx-cursor: hand;"
        );
        b.setOnMouseEntered(e -> b.setOpacity(0.8));
        b.setOnMouseExited(e -> b.setOpacity(1.0));
    }

    // --- GETTERS ---
    public Button getBtnLibri() { return btnLibri; }
    public Button getBtnUtenti() { return btnUtenti; }
    public Button getBtnPrestiti() { return btnPrestiti; }
    public Button getBtnInfo() { return btnInfo; }
    public Button getBtnEsci() { return btnEsci; } // Nuovo Getter
}