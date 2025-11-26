package it.unisa.biblioteca.model;

public class Libro {
    private String titolo;
    private String autore;

    public Libro(String titolo, String autore) {
        this.titolo = titolo;
        this.autore = autore;
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
