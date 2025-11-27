package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utente implements Serializable {

    
    //Forse mi va 
    
    // --- CAMPI MUTABILI ---
    // Questi non sono final perché l'utente potrebbe doverli correggere
    private String nome;
    private String cognome;
    private String email;

    // --- CAMPI IMMUTABILI ---
    // La matricola è la mia Primary Key logica: non deve cambiare mai!
    private final String matricola;

    // Uso una lista di Prestiti (e non di Libri) perché mi serve sapere le date di scadenza
    private final List<Prestito> prestitiAttivi;

    // Regex standard per validare la mail (copiata per sicurezza formale)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Costruttore: inizializza lo stato dell'oggetto
    public Utente(String nome, String cognome, String matricola, String email) {
        // Controllo subito la matricola perché è l'unico dato che non potrò cambiare dopo
        if (matricola == null || matricola.trim().isEmpty()) {
            throw new IllegalArgumentException("Errore: La matricola è obbligatoria.");
        }

        this.matricola = matricola;
        this.prestitiAttivi = new ArrayList<>(); // Inizializzo la lista vuota per evitare NullPointer

        // Per gli altri campi uso i setter che ho scritto sotto.
        // In questo modo non duplico i controlli (DRY principle).
        setNome(nome);
        setCognome(cognome);
        setEmail(email);
    }

    // --- SETTER (Logica di modifica) --- //

    public void setNome(String nome) {
        // Controllo base: niente stringhe vuote
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }
        this.nome = nome;
    }

    public void setCognome(String cognome) {
        if (cognome == null || cognome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome non può essere vuoto.");
        }
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        // Controllo che il formato sia tipo "testo@testo"
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Formato email non valido: " + email);
        }
        this.email = email;
    }

    // --- GETTER (Accesso ai dati) ---

    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public String getMatricola() { return matricola; }

    // --- GESTIONE PRESTITI ---

    // Ritorno una lista "unmodifiable" così nessuno può fare getPrestiti().clear() da fuori rompendo tutto
    public List<Prestito> getPrestitiAttivi() {
        return Collections.unmodifiableList(prestitiAttivi);
    }

    public void aggiungiPrestito(Prestito prestito) {
        if (prestito == null) throw new IllegalArgumentException("Non posso aggiungere un prestito nullo.");

        // Controllo di coerenza: sto assegnando a questo utente un prestito intestato a lui?
        if (!prestito.getUtente().equals(this)) {
            throw new IllegalArgumentException("Il prestito non corrisponde a questo utente!");
        }

        // Esempio regola di business: max 3 libri contemporaneamente
        if (prestitiAttivi.size() >= 3) {
            throw new IllegalStateException("Limite prestiti raggiunto (max 3).");
        }

        this.prestitiAttivi.add(prestito);
    }

    public void rimuoviPrestito(Prestito prestito) {
        this.prestitiAttivi.remove(prestito);
    }

    // --- IDENTITÀ DELL'OGGETTO ---
    // Importante: due oggetti Utente sono lo "stesso" utente se hanno la stessa matricola.
    // Serve per far funzionare correttamente le liste e le ricerche.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(matricola, utente.matricola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricola);
    }

    @Override
    public String toString() {
        return nome + " " + cognome + " (" + matricola + ")";
    }
}