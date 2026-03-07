package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce il salvataggio e il caricamento persistente dei dati
 * dell'applicazione mediante serializzazione binaria su file.
 */
public class GestoreFile {

    private static final String FILE_DATI = "database_biblioteca.dat";

    /**
     * Salva su file binario tutti i dati del sistema.
     *
     * <p>
     * Il metodo serializza i dati relativi a libri, utenti e prestiti
     * convertendo le liste osservabili in liste standard e scrivendole
     * nel file di persistenza dell'applicazione.
     * </p>
     *
     * @param libri lista osservabile dei libri da salvare
     * @param utenti lista osservabile degli utenti da salvare
     * @param prestiti lista osservabile dei prestiti da salvare
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
     * Carica dal file binario i dati del sistema.
     *
     * <p>
     * Il metodo deserializza i dati relativi a libri, utenti e prestiti
     * e aggiorna le liste osservabili passate come parametro.
     * Se il file di salvataggio non esiste, il caricamento non viene eseguito.
     * </p>
     *
     * @param catalogo lista osservabile in cui caricare i libri
     * @param anagrafica lista osservabile in cui caricare gli utenti
     * @param prestiti lista osservabile in cui caricare i prestiti
     *
     * @see ObjectInputStream
     * @see FileInputStream
     */
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