package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Libro implements Serializable {
    private String titolo;
    private List<String> autori;
    private LocalDate dataPubblicazione;
    private int disponibilita;
    //L'ISBN è final: identifica il libro univocamente.
    private final String isbn;


    ///  --- Costruttore ---///
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

        //3. Defensive Copy per la lista (Importante per Ingegneria del Software)
        //Creiamo una NUOVA lista contenente gli autori passati, così se la lista originale fuori da questa classe cambia, il libro non viene corrotto.*/
        this.autori = new ArrayList<>(autori);
    }

    /// --- Getters --- ///

    public String getTitolo() {
        return titolo;
    }
    public String getIsbn() {
        return isbn;
    }
    public LocalDate getDataPubblicazione() {
        return dataPubblicazione;
    }

    //Restituisce una vista non modificabile degli autori.
    //Questo impedisce che qualcuno faccia getAutori().clear() cancellando gli autori dal model.
    public List<String> getAutori() {
        return Collections.unmodifiableList(this.autori);
    }
    public int getDisponibilita() {
        return disponibilita;
    }


    ///  --- Setter --- ///
    public void setTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo non può essere vuoto.");
        }
        this.titolo = titolo;
    }

    public void setDataPubblicazione(LocalDate dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    public void setAutori(List<String> nuoviAutori) {
        if (nuoviAutori == null || nuoviAutori.isEmpty()) {
            throw new IllegalArgumentException("Deve esserci almeno un autore.");
        }
        ///Defensive copy anche qui: scolleghiamo la lista interna da quella passata
        this.autori = new ArrayList<>(nuoviAutori);
    }

    /// --- Gestione Stato ---
    ///Aumenta la disponibilità (es. restituzione o acquisto nuove copie).
    public void incrementaDisponibilita() {
        this.disponibilita++;
    }

    ///Diminuisce la disponibilità (es. prestito).
    //////@throws IllegalStateException se non ci sono copie disponibili.

    public void decrementaDisponibilita() {
        if (this.disponibilita > 0) {
            this.disponibilita--;
        } else {
            throw new IllegalStateException("Impossibile decrementare: copie esaurite.");
        }
    }

    // Utile per confrontare due libri (basato sull'ISBN che è univoco)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
