package it.unisa.biblioteca.model;

import java.time.LocalDate;

public class Prestito {
    private final Utente utente;
    private final Libro libro;
    private final LocalDate dataInizio;
    private final LocalDate dataFinePrevista; // Ecco la data che cercavi!


    ///  ---Costruttore ---///
    public Prestito(Utente utente, Libro libro, LocalDate dataInizio) {
        this.utente = utente;
        this.libro = libro;
        this.dataInizio = dataInizio;

        // Regola di business: il prestito dura 60 giorni fissi
        this.dataFinePrevista = dataInizio.plusDays(60);
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

    @Override
    public String toString() {
        return "Prestito: " + libro.getTitolo() + " scade il " + dataFinePrevista;
    }
}