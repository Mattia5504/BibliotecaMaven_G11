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
     * @brief Salva su file binario tutti i dati del sistema (libri, utenti e prestiti).
     *
     * Questo metodo serializza le liste fornite convertendole prima da
     * {@code ObservableList} a {@code ArrayList}, poiché le ObservableList
     * non sono direttamente serializzabili. I dati vengono scritti nel file
     * indicato da {@code FILE_DATI}.
     *
     * @param libri    Lista osservabile contenente i libri da salvare.
     * @param utenti   Lista osservabile degli utenti registrati.
     * @param prestiti Lista osservabile dei prestiti correnti.
     *
     * @throws IOException Se si verifica un errore durante la scrittura del file.
     *
     * @see ObjectOutputStream
     * @see FileOutputStream
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
     * @brief Carica dal file binario tutti i dati salvati (libri, utenti e prestiti).
     *
     * Questo metodo deserializza il contenuto del file indicato da {@code FILE_DATI}
     * e ripristina le liste osservabili passate come parametro. Se il file non esiste,
     * l'operazione viene semplicemente ignorata. I dati vengono letti nello stesso
     * ordine in cui erano stati precedentemente salvati.
     *
     * @param catalogo    Lista osservabile in cui verranno caricati i libri.
     * @param anagrafica  Lista osservabile degli utenti da ripristinare.
     * @param prestiti    Lista osservabile dei prestiti da ripristinare.
     *
     * @throws IOException Se si verifica un errore durante la lettura del file.
     * @throws ClassNotFoundException Se le classi degli oggetti serializzati non vengono trovate.
     *
     * @see ObjectInputStream
     * @see FileInputStream
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