package it.unisa.biblioteca.controller;

import it.unisa.biblioteca.model.Libro;
import it.unisa.biblioteca.model.Prestito;
import it.unisa.biblioteca.model.Utente;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Classe di utilità per il salvataggio e caricamento dati su file CSV.
 * Gruppo 11 - Ingegneria del Software
 */
public class GestoreFile {

    private static final String FILE_LIBRI = "libri.csv";
    private static final String FILE_UTENTI = "utenti.csv";
    private static final String FILE_PRESTITI = "prestiti.csv";
    private static final String SEPARATOR = ";";

    // --- SALVATAGGIO ---

    public void salvaTutto(ObservableList<Libro> libri, ObservableList<Utente> utenti, ObservableList<Prestito> prestiti) {
        try {
            salvaLibri(libri);
            salvaUtenti(utenti);
            salvaPrestiti(prestiti);
            System.out.println("Salvataggio completato con successo.");
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    private void salvaLibri(ObservableList<Libro> libri) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_LIBRI))) {
            for (Libro l : libri) {
                // Formato: Titolo;Autore1,Autore2;Data;ISBN;Copie
                String autoriString = String.join(",", l.getAutori());
                String linea = l.getTitolo() + SEPARATOR +
                        autoriString + SEPARATOR +
                        l.getDataPubblicazione() + SEPARATOR +
                        l.getIsbn() + SEPARATOR +
                        l.getDisponibilita();
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    private void salvaUtenti(ObservableList<Utente> utenti) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_UTENTI))) {
            for (Utente u : utenti) {
                // Formato: Nome;Cognome;Matricola;Email
                String linea = u.getNome() + SEPARATOR +
                        u.getCognome() + SEPARATOR +
                        u.getMatricola() + SEPARATOR +
                        u.getEmail();
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    private void salvaPrestiti(ObservableList<Prestito> prestiti) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PRESTITI))) {
            for (Prestito p : prestiti) {
                // Formato: MatricolaUtente;IsbnLibro;DataInizio
                // Salviamo solo le CHIAVI (Matricola e ISBN) per ricostruire il collegamento dopo
                String linea = p.getUtente().getMatricola() + SEPARATOR +
                        p.getLibro().getIsbn() + SEPARATOR +
                        p.getDataInizio();
                writer.write(linea);
                writer.newLine();
            }
        }
    }

    // --- CARICAMENTO ---

    public void caricaTutto(ObservableList<Libro> catalogo, ObservableList<Utente> anagrafica, ObservableList<Prestito> prestiti) {
        try {
            caricaLibri(catalogo);
            caricaUtenti(anagrafica);
            // I prestiti vanno caricati per ultimi perché dipendono da Libri e Utenti esistenti!
            caricaPrestiti(prestiti, catalogo, anagrafica);
            System.out.println("Caricamento completato.");
        } catch (IOException e) {
            System.err.println("Nessun dato precedente trovato o errore di lettura: " + e.getMessage());
        }
    }

    private void caricaLibri(ObservableList<Libro> catalogo) throws IOException {
        File file = new File(FILE_LIBRI);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] parti = linea.split(SEPARATOR);
                if (parti.length >= 5) {
                    String titolo = parti[0];
                    List<String> autori = Arrays.asList(parti[1].split(","));
                    LocalDate data = LocalDate.parse(parti[2]);
                    String isbn = parti[3];
                    int copie = Integer.parseInt(parti[4]);

                    catalogo.add(new Libro(titolo, autori, data, isbn, copie));
                }
            }
        }
    }

    private void caricaUtenti(ObservableList<Utente> anagrafica) throws IOException {
        File file = new File(FILE_UTENTI);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] parti = linea.split(SEPARATOR);
                if (parti.length >= 4) {
                    anagrafica.add(new Utente(parti[0], parti[1], parti[2], parti[3]));
                }
            }
        }
    }

    private void caricaPrestiti(ObservableList<Prestito> prestiti, ObservableList<Libro> catalogo, ObservableList<Utente> anagrafica) throws IOException {
        File file = new File(FILE_PRESTITI);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] parti = linea.split(SEPARATOR);
                if (parti.length >= 3) {
                    String matricola = parti[0];
                    String isbn = parti[1];
                    LocalDate dataInizio = LocalDate.parse(parti[2]);

                    // Ricerca degli oggetti reali tramite ID
                    Utente u = trovaUtente(anagrafica, matricola);
                    Libro l = trovaLibro(catalogo, isbn);

                    if (u != null && l != null) {
                        Prestito p = new Prestito(u, l, dataInizio);
                        prestiti.add(p);
                        u.aggiungiPrestito(p); // Importante: sincronizzare lo stato dell'utente!
                        // Nota: non decrementiamo la disponibilità qui perché presumiamo
                        // che il numero salvato nel file libri sia già quello aggiornato (post-prestito).
                    }
                }
            }
        }
    }

    // Metodi helper per ritrovare gli oggetti dalle liste
    private Utente trovaUtente(ObservableList<Utente> utenti, String matricola) {
        for (Utente u : utenti) {
            if (u.getMatricola().equals(matricola)) return u;
        }
        return null;
    }

    private Libro trovaLibro(ObservableList<Libro> libri, String isbn) {
        for (Libro l : libri) {
            if (l.getIsbn().equals(isbn)) return l;
        }
        return null;
    }
}