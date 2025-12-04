package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * @brief Rappresenta un prestito attivo di un libro a un utente.
 * * Questa classe gestisce l'associazione tra un Utente e un Libro, tenendo traccia
 * delle date di inizio e di scadenza.
 * La classe implementa la logica di calcolo automatico della data di restituzione
 * e fornisce metodi di utilità per verificare lo stato del prestito (scaduto o giorni mancanti).
 */

public class Prestito implements Serializable {
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInizio;
    private final LocalDate dataFinePrevista; // Ecco la data che cercavi!


    //Costruttore
    /**
     * @brief Crea un nuovo prestito e calcola la data di scadenza.
     * * Inizializza l'associazione tra utente e libro.
     * La data di scadenza (`dataFinePrevista`) viene calcolata automaticamente
     * aggiungendo **60 giorni** alla data di inizio specificata.
     * * @param utente L'utente che richiede il prestito.
     * @param libro Il libro oggetto del prestito.
     * @param dataInizio La data in cui inizia il prestito.
     * @throws IllegalArgumentException Se utente, libro o dataInizio sono nulli.
     */
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio) {
        //Controllo validità imput
        if (utente == null) throw new IllegalArgumentException("L'utente del prestito non può essere nullo.");
        if (libro == null) throw new IllegalArgumentException("Il libro del prestito non può essere nullo.");
        if (dataInizio == null) throw new IllegalArgumentException("La data di inizio non può essere nulla.");

        //inizializzazione
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;
        this.dataFinePrevista = dataInizio.plusDays(60); // Regola di business: il prestito dura 60 giorni fissi
    }

    // Getter
    /**
     * @brief Restituisce il libro prestato.
     * @return Oggetto Libro associato a questo prestito.
     */
    public Libro getLibro() {
        return libro;
    }

    /**
     * @brief Restituisce l'utente che ha effettuato il prestito.
     * @return Oggetto Utente associato a questo prestito.
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * @brief Restituisce la data prevista per la restituzione.
     * * Questa data è calcolata al momento della creazione (Data Inizio + 60 giorni).
     * @return LocalDate rappresentante la scadenza.
     */
    public LocalDate getDataFinePrevista() {
        return dataFinePrevista;
    }

    /**
     * @brief Restituisce la data di inizio del prestito.
     * @return LocalDate rappresentante l'inizio del prestito.
     */
    public LocalDate getDataInizio() {
        return dataInizio;
    }


    //Gestione stato ---
    /**
     * @brief Verifica se il prestito è scaduto rispetto alla data odierna.
     * * Utile per logiche di visualizzazione (es. evidenziare in rosso nella GUI).
     * Confronta la data corrente (`LocalDate.now()`) con la `dataFinePrevista`.
     * * @return true se oggi è successivo alla data di scadenza, false altrimenti.
     */
    public boolean isScaduto() {
        return LocalDate.now().isAfter(dataFinePrevista);
    }


    /**
     * @brief Calcola i giorni mancanti alla scadenza o i giorni di ritardo.
     * * Calcola la differenza in giorni tra la data odierna e la scadenza.
     * * @return Un valore long:
     * - **Positivo**: Giorni mancanti alla scadenza.
     * - **Zero**: Scade oggi.
     * - **Negativo**: Giorni di ritardo (es. -5 significa scaduto da 5 giorni).
     */
    public long giorniAllaScadenza() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataFinePrevista);
    }

    /**
     * @brief Restituisce una rappresentazione testuale del prestito.
     * * Formato: "Prestito: 'Titolo' a Nome Cognome (Scadenza: YYYY-MM-DD)"
     * @return Stringa descrittiva del prestito.
     */
    @Override
    public String toString() {
        return String.format("Prestito: '%s' a %s %s (Scadenza: %s)",
                libro.getTitolo(), utente.getNome(), utente.getCognome(), dataFinePrevista);
    }
}