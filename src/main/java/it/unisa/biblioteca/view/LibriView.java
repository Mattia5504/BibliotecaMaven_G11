package it.unisa.biblioteca.view;

import it.unisa.biblioteca.model.Libro;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Vista Tabellare per i Libri.
 * Usa BorderPane: Tabella al centro, comandi sopra e sotto.
 */
public class LibriView extends BorderPane {

    private TableView<Libro> tabella = new TableView<>();
    private Button btnIndietro = new Button("< Indietro");
    private Button btnNuovo = new Button("+ Aggiungi Libro");
    private Button btnPiu = new Button("+ Copie");
    private Button btnMeno = new Button("- Copie");

    public LibriView(ObservableList<Libro> libri) {
        this.setPadding(new Insets(15));

        // --- DEFINIZIONE COLONNE ---

        // 1. Titolo
        TableColumn<Libro, String> colTitolo = new TableColumn<>("Titolo");
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colTitolo.setPrefWidth(250);

        // 2. Autori (Lista convertita in Stringa per la visualizzazione)
        TableColumn<Libro, String> colAutori = new TableColumn<>("Autore/i");
        colAutori.setCellValueFactory(cell -> new SimpleStringProperty(String.join(", ", cell.getValue().getAutori())));
        colAutori.setPrefWidth(200);

        // 3. Anno (Estratto da LocalDate)
        TableColumn<Libro, Integer> colAnno = new TableColumn<>("Anno");
        colAnno.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDataPubblicazione().getYear()));

        // 4. ISBN e Disponibilità
        TableColumn<Libro, String> colIsbn = new TableColumn<>("ISBN");
        colIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Libro, Integer> colDisp = new TableColumn<>("Disp.");
        colDisp.setCellValueFactory(new PropertyValueFactory<>("disponibilita"));

        tabella.getColumns().addAll(colTitolo, colAutori, colAnno, colIsbn, colDisp);

        // Colleghiamo i dati alla tabella
        tabella.setItems(libri);

        // --- LAYOUT ---
        HBox topBar = new HBox(15, btnIndietro, new Label("CATALOGO LIBRI"));
        topBar.setPadding(new Insets(0,0,15,0));

        // Barra comandi in basso
        HBox bottomBar = new HBox(10, btnNuovo, new Separator(), new Label("Gestione Copie:"), btnPiu, btnMeno);
        bottomBar.setPadding(new Insets(15,0,0,0));

        this.setTop(topBar);
        this.setCenter(tabella);
        this.setBottom(bottomBar);
    }

    public TableView<Libro> getTabella() { return tabella; }
    public Button getBtnIndietro() { return btnIndietro; }
    public Button getBtnNuovo() { return btnNuovo; }
    public Button getBtnPiu() { return btnPiu; }
    public Button getBtnMeno() { return btnMeno; }

    // Metodo utile per aggiornare la grafica quando cambiano i dati interni (es. copie)
    public void refresh() { tabella.refresh(); }
}