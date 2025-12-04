package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @brief Rappresenta un utente della biblioteca.
 * * Questa classe definisce l'entità Utente all'interno del sistema.
 * Gestisce i dati anagrafici e mantiene la lista dei prestiti attivi.
 * Implementa Serializable per permettere il salvataggio su file.
 * * L'identità dell'utente è definita univocamente dalla sua matricola.
 */

public class Utente implements Serializable {

    
    //Forse mi va 
    
    // --- CAMPI MUTABILI ---
    // Questi non sono final perché l'utente potrebbe doverli correggere
    private String nome;
    private String cognome;
    private String email;

    // --- CAMPI IMMUTABILI ---
    // La matricola è la Primary Key logica: non deve cambiare mai!
    private final String matricola;

    // Uso una lista di Prestiti (e non di Libri) perché mi serve sapere le date di scadenza
    private final List<Prestito> prestitiAttivi;

    // Regex standard per validare la mail (copiata per sicurezza formale)
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    // Costruttore: inizializza lo stato dell'oggetto
    /**
     * @brief Costruttore della classe Utente.
     * * Inizializza un nuovo utente controllando la validità dei dati.
     * La matricola viene controllata immediatamente in quanto final.
     * * @param nome Il nome dell'utente.
     * @param cognome Il cognome dell'utente.
     * @param matricola La matricola univoca (deve essere di 10 cifre).
     * @param email L'indirizzo email dell'utente.
     * * @throws IllegalArgumentException Se la matricola è nulla, vuota o non composta da 10 cifre.
     * @throws IllegalArgumentException Se nome, cognome o email non rispettano i criteri di validazione.
     */
    public Utente(String nome, String cognome, String matricola, String email) {
        // Controllo subito la matricola perché è l'unico dato che non potrò cambiare dopo
        if (matricola == null || matricola.trim().isEmpty()) {
            throw new IllegalArgumentException("Errore: La matricola è obbligatoria.");
        }

        if(matricola.length() != 10){
            throw new IllegalArgumentException("Errore: la matricola deve essere composta da esattamente 10 cifre");
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

    /**
     * @brief Imposta il nome dell'utente.
     * * @param nome Il nuovo nome da assegnare.
     * @throws IllegalArgumentException Se il nome è nullo o vuoto.
     */
    public void setNome(String nome) {
        // Controllo base: niente stringhe vuote
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il nome non può essere vuoto.");
        }
        this.nome = nome;
    }

    /**
     * @brief Imposta il cognome dell'utente.
     * * @param cognome Il nuovo cognome da assegnare.
     * @throws IllegalArgumentException Se il cognome è nullo o vuoto.
     */
    public void setCognome(String cognome) {
        if (cognome == null || cognome.trim().isEmpty()) {
            throw new IllegalArgumentException("Il cognome non può essere vuoto.");
        }
        this.cognome = cognome;
    }

    /**
     * @brief Imposta l'email dell'utente.
     * * Verifica che la stringa passata rispetti il pattern standard delle email.
     * * @param email La nuova email da assegnare.
     * @throws IllegalArgumentException Se l'email è nulla o non rispetta il formato valido.
     */
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