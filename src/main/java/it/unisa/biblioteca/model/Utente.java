package it.unisa.biblioteca.model;

import java.io.Serializable;
import java.util.Objects;

public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private String cognome;
    // La matricola è final perché identifica l'utente e non cambia
    private final String matricola;
    private String email;
    
    // Contatore per gestire il requisito "Max 3 libri"
    private int contatorePrestitiAttivi;

    public Utente(String nome, String cognome, String matricola, String email) {
        if (matricola == null || matricola.trim().isEmpty()) {
            throw new IllegalArgumentException("La matricola è obbligatoria.");
        }
        // Aggiungi qui altre validazioni se vuoi (es. email)
        
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.email = email;
        this.contatorePrestitiAttivi = 0;
    }

    /// --- Getter --- ///
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getMatricola() { return matricola; }
    public String getEmail() { return email; }
    public int getContatorePrestitiAttivi() { return contatorePrestitiAttivi; }

    /// --- Setter --- ///
    public void setNome(String nome) { this.nome = nome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public void setEmail(String email) { this.email = email; }
    
    // NON mettiamo setMatricola perché è final (giustamente)

    /// --- Gestione Prestiti (Requisito Business) --- ///
    public void incrementaPrestiti() {
        if (contatorePrestitiAttivi >= 3) {
            throw new IllegalStateException("L'utente ha raggiunto il limite di 3 prestiti.");
        }
        this.contatorePrestitiAttivi++;
    }

    public void decrementaPrestiti() {
        if (contatorePrestitiAttivi > 0) {
            this.contatorePrestitiAttivi--;
        }
    }
    
    // Helper per il controller
    public boolean puoPrendereInPrestito() {
        return contatorePrestitiAttivi < 3;
    }

    /// --- Equals & HashCode (Fondamentale per i Set) --- ///
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
        return cognome + " " + nome + " (" + matricola + ")";
    }
}