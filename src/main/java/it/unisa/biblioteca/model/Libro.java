package it.unisa.biblioteca.model;
import java.time.LocalDate;

public class Libro {
    private final String titolo;
    private final List<String> autori;
    private final LocalDate annoPubblicazione;
    private final String ibns;
    private int disponibilità;

    public Libro(String titolo, List<String> autori, LocalDate dataPubblicazione, String isbn, int disponibilita) {
        // 1. Validazione dei dati (Invarianti di classe)
        //Da capire se mantenerlo o meno
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo non può essere vuoto.");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ISBN è obbligatorio.");
        }
        if (autori == null || autori.isEmpty()) {
            throw new IllegalArgumentException("Il libro deve avere almeno un autore.");
        }
        if (disponibilita < 1) {
            throw new IllegalArgumentException("La disponibilità iniziale deve essere almeno 1.");
        }

        // 2. Assegnazione
        this.titolo = titolo;
        this.isbn = isbn;
        this.dataPubblicazione = dataPubblicazione;
        this.disponibilita = disponibilita;

        // 3. Defensive Copy per la lista (Importante per Ingegneria del Software)
        // Creiamo una NUOVA lista contenente gli autori passati, così se la lista
        // originale fuori da questa classe cambia, il libro non viene corrotto.
        this.autori = new ArrayList<>(autori);
    }

    public String getTitolo() {
        return titolo;
    }

    public String getAutore() {
        return autore;
    }

    @Override
    public String toString() {
        return titolo + " di " + autore;
    }
}
