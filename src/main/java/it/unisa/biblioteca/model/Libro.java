package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @brief Rappresenta un libro nel catalogo della biblioteca.
 * * Questa classe gestisce le informazioni bibliografiche (Titolo, Autori, ISBN)
 * e lo stato della disponibilità (numero di copie fisiche presenti).
 * * L'identità del libro è garantita univocamente dal codice ISBN.
 * La classe implementa la serializzazione per il salvataggio su file.
 */

public class Libro implements Serializable {
    private String titolo;
    private List<String> autori;
    private LocalDate dataPubblicazione;
    private int disponibilita;
    //L'ISBN è final: identifica il libro univocamente.
    private final String isbn;


    ///  --- Costruttore ---///
    /**
     * @brief Costruisce un nuovo oggetto Libro con titolo, autori, data di pubblicazione, ISBN e disponibilità.
     *
     * Questo costruttore applica diversi controlli di validazione sugli argomenti
     * (invarianti di classe) per garantire la creazione di un oggetto coerente.
     * Viene inoltre effettuata una *defensive copy* della lista degli autori per
     * evitare che modifiche esterne possano alterare lo stato interno dell'oggetto.
     *
     * @param titolo             Titolo del libro; non può essere nullo o vuoto.
     * @param autori             Lista degli autori; deve contenere almeno un autore.
     * @param dataPubblicazione  Data di pubblicazione del libro (può essere null se non gestita).
     * @param isbn               Codice ISBN del libro; non può essere nullo o vuoto.
     * @param disponibilita      Numero di copie disponibili; deve essere almeno 1.
     *
     * @throws IllegalArgumentException Se uno degli argomenti non rispetta gli invarianti:
     *                                  - titolo nullo o vuoto
     *                                  - ISBN nullo o vuoto
     *                                  - lista autori nulla o vuota
     *                                  - disponibilità minore di 1
     */

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

    //--- Getters

    /**
     * @brief Restituisce il titolo del libro.
     * @return Stringa contenente il titolo.
     */

    public String getTitolo() {
        return titolo;
    }

    /**
     * @brief Restituisce il codice ISBN.
     * @return Stringa contenente l'ISBN (identificativo univoco).
     */
    public String getIsbn() {
        return isbn;
    }
    /**
     * @brief Restituisce la data di pubblicazione.
     * @return Oggetto LocalDate o null se non impostato.
     */
    public LocalDate getDataPubblicazione() {
        return dataPubblicazione;
    }

    /**
     * @brief Restituisce la lista degli autori.
     * * **Nota:** Restituisce una vista *non modificabile* (unmodifiableList).
     * Questo impedisce modifiche accidentali alla lista interna degli autori.
     * Per modificare gli autori, usare il metodo `setAutori`.
     * * @return List&lt;String&gt; read-only degli autori.
     */
    public List<String> getAutori() {
        return Collections.unmodifiableList(this.autori);
    }
    /**
     * @brief Restituisce il numero di copie attualmente disponibili.
     * @return Intero rappresentante la disponibilità.
     */
    public int getDisponibilita() {
        return disponibilita;
    }


    //  --- Setter ---

    /**
     * @brief Aggiorna il titolo del libro.
     * @param titolo Il nuovo titolo (non deve essere vuoto).
     * @throws IllegalArgumentException Se il titolo è nullo o vuoto.
     */
    public void setTitolo(String titolo) {
        if (titolo == null || titolo.trim().isEmpty()) {
            throw new IllegalArgumentException("Il titolo non può essere vuoto.");
        }
        this.titolo = titolo;
    }

    /**
     * @brief Aggiorna la data di pubblicazione.
     * @param dataPubblicazione La nuova data.
     */
    public void setDataPubblicazione(LocalDate dataPubblicazione) {
        this.dataPubblicazione = dataPubblicazione;
    }

    /**
     * @brief Aggiorna la lista degli autori.
     * * Effettua una **defensive copy** della nuova lista per proteggere l'incapsulamento.
     * * @param nuoviAutori La nuova lista di autori.
     * @throws IllegalArgumentException Se la lista è nulla o vuota.
     */
    public void setAutori(List<String> nuoviAutori) {
        if (nuoviAutori == null || nuoviAutori.isEmpty()) {
            throw new IllegalArgumentException("Deve esserci almeno un autore.");
        }
        ///Defensive copy anche qui: scolleghiamo la lista interna da quella passata
        this.autori = new ArrayList<>(nuoviAutori);
    }

    // --- Gestione Stato --

    /**
     * @brief Incrementa la disponibilità del libro di una unità.
     * * Da utilizzare quando un libro viene restituito o vengono acquistate nuove copie.
     */
    public void incrementaDisponibilita() {
        this.disponibilita++;
    }

    //Diminuisce la disponibilità (es. Prestito).

    /**
     * @brief Decrementa la disponibilità del libro di una unità.
     * * Da utilizzare quando viene effettuato un prestito.
     * * @throws IllegalStateException Se la disponibilità è 0 (copie esaurite).
     */
    public void decrementaDisponibilita() {
        if (this.disponibilita > 0) {
            this.disponibilita--;
        } else {
            throw new IllegalStateException("Impossibile decrementare: copie esaurite.");
        }
    }


    /**
     * @brief Confronta questo libro con un altro oggetto.
     * * Due libri sono considerati uguali se hanno lo stesso ISBN.
     * * @param o Oggetto da confrontare.
     * @return true se gli ISBN coincidono, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return Objects.equals(isbn, libro.isbn);
    }

    /**
     * @brief Calcola l'hash code del libro.
     * * Basato esclusivamente sull'ISBN.
     * @return Valore hash intero.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
