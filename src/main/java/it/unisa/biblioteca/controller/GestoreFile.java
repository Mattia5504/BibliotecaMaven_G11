package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestore salvataggio BINARIO (.dat).
 * I file non sono modificabili manualmente con editor di testo.
 */
public class GestoreFile {

    private static final String FILE_DATI = "database_biblioteca.dat";

    /**
     * Costruttore del gestore salvataggio file
     *
     * <p>
     *     Converte le {@link ObservableList} passate come parametri in array "semplici", così da permettere
     *     la serializzazione.
     *     Il salvataggio tramite {@link ObjectOutputStream} avviene nell'ordine dei parametri
     * </p>
     * @param libri
     * @param utenti
     * @param prestiti
     * @throws IOException
     */
    public void salvaTutto(ObservableList<Libro> libri, ObservableList<Utente> utenti, ObservableList<Prestito> prestiti) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_DATI))) {

            // Le ObservableList non sono serializzabili, le convertiamo in ArrayList normali
            oos.writeObject(new ArrayList<>(libri));
            oos.writeObject(new ArrayList<>(utenti));
            oos.writeObject(new ArrayList<>(prestiti));

            System.out.println("Salvataggio binario completato.");
        } catch (IOException e) {
            System.err.println("Errore salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Costruttore del gestore caricamento file
     *
     * <p>
     *     Legge nell'esatto ordine di scrittura e popola le {@link ObservableList} corrispondenti, in modo da ripristinare
     *     l'esatto stato dell'ultimo salvataggio
     * </p>
     * @param catalogo
     * @param anagrafica
     * @param prestiti
     * @throws IOException
     * @throws ClassNotFoundException
     *
     */
    @SuppressWarnings("unchecked")
    public void caricaTutto(ObservableList<Libro> catalogo, ObservableList<Utente> anagrafica, ObservableList<Prestito> prestiti) {
        File file = new File(FILE_DATI);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {

            // Leggiamo nell'ordine esatto di scrittura
            List<Libro> libriCaricati = (List<Libro>) ois.readObject();
            List<Utente> utentiCaricati = (List<Utente>) ois.readObject();
            List<Prestito> prestitiCaricati = (List<Prestito>) ois.readObject();

            // Puliamo e riempiamo le liste osservabili
            catalogo.setAll(libriCaricati);
            anagrafica.setAll(utentiCaricati);
            prestiti.setAll(prestitiCaricati);

            System.out.println("Caricamento binario completato.");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore caricamento: " + e.getMessage());
        }
    }
}