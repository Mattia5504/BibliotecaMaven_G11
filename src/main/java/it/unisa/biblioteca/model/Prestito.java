package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Prestito implements Serializable {
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInizio;
    private final LocalDate dataFinePrevista; // Ecco la data che cercavi!


    ///  ---Costruttore ---///
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

    /// --- Getter --- ///
    public Libro getLibro() {
        return libro;
    }

    public Utente getUtente() {
        return utente;
    }

    public LocalDate getDataFinePrevista() {
        return dataFinePrevista;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }


    /// --- Gestione stato --- ///
    // Verifica se il prestito è scaduto (utile per colorare di rosso la GUI)
    public boolean isScaduto() {
        return LocalDate.now().isAfter(dataFinePrevista);
    }


    //Calcola i giorni mancanti alla restituzione (o i giorni di ritardo se negativo).
    public long giorniAllaScadenza() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dataFinePrevista);
    }

    @Override
    public String toString() {
        return String.format("Prestito: '%s' a %s %s (Scadenza: %s)",
                libro.getTitolo(), utente.getNome(), utente.getCognome(), dataFinePrevista);
    }
}