package it.unisa.biblioteca.model;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {
    @Test
    public void testCostruttoreValido(){
        System.out.println("Test creazione utente valida");
        Utente u = new Utente("Nome", "Cognome","0612709899","pippo@gmail.com");

        assertEquals("Nome",u.getNome());
        assertEquals("Cognome",u.getCognome());
        assertEquals("0612709899",u.getMatricola());
        assertEquals("pippo@gmail.com",u.getEmail());
    }

    @Test
    public void testNomeEmpty(){
        System.out.println("Test nome empty (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("","Cognome","0612709899","pippo@gmail.com");
        });
    }

    @Test
    public void testNomeNull(){
        System.out.println("Test nome null (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente(null,"Cognome","0612709899","pippo@gmail.com");
        });
    }

    @Test
    public void testCognomeEmpty(){
        System.out.println("Test Cognome empty (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","","0612709899","pippo@gmail.com");
        });
    }

    @Test
    public void testCognomeNull(){
        System.out.println("Test cognome null (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo",null,"0612709899","pippo@gmail.com");
        });
    }

    @Test
    public void testMatricolaEmpty(){
        System.out.println("Test Matricola empty (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","Prova","","pippo@gmail.com");
        });
    }

    @Test
    public void testMatricolaNull(){
        System.out.println("Test Matricola null (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","Prova",null,"pippo@gmail.com");
        });
    }

    @Test
    public void testMatricolaMinore10(){
        System.out.println("Test matricola composta da <10 cifre (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","Prova","061270989","pippo@gmail.com");
        });
    }

    @Test
    public void testMatricolaMaggiore10(){
        System.out.println("Test Matricola composta da 10> cifre (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo",null,"06127098999","pippo@gmail.com");
        });
    }

    @Test
    void testEmailNonValida() {
        System.out.println("Test formati Email non validi");

        // Caso 1: Manca la chiocciola
        Exception ex1 = assertThrows(IllegalArgumentException.class, () -> {
            new Utente("Mario", "Rossi", "0512100001", "mariorossi.it");
        });
        System.out.println("1. Senza chiocciola: " + ex1.getMessage());

        // Caso 2: Manca il punto dopo la chiocciola (Il tuo problema attuale)
        Exception ex2 = assertThrows(IllegalArgumentException.class, () -> {
            new Utente("Mario", "Rossi", "0512100001", "mario@libero"); // Senza .it
        });
        System.out.println("2. Senza estensione: " + ex2.getMessage());

        // Caso 3: Email vuota
        assertThrows(IllegalArgumentException.class, () -> {
            new Utente("Mario", "Rossi", "0512100001", "");
        });
    }

    @Test
    public void testEmailEmpty(){
        System.out.println("Test Email empty (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","Prova","0612709896","");
        });
    }

    @Test
    public void testEmailNull(){
        System.out.println("Test Email null (deve fallire)");

        assertThrows(IllegalArgumentException.class, ()->{
            new Utente("Pippo","Prova","0612709896",null);
        });
    }

    @Test
    public void testPrestitoNull(){
        System.out.println("Test prestito null (deve fallire)");

        Utente u = new Utente("Nome", "Cognome","0612709899","pippo@gmail.com");

        assertThrows(IllegalArgumentException.class, ()->{
            u.aggiungiPrestito(null);
        });

    }

    @Test
    public void testPrestitoValido(){
        System.out.println("Test prestito valido, verifica che un nuovo prestito ritorni il giusdto utente e il giusto libro");
        Utente u = new Utente("Nome", "Cognome","0612709899","pippo@gmail.com");
        List<String> autori = Arrays.asList("Autore 1", "Autore 2");
        LocalDate data = LocalDate.of(2023, 1, 1);
        Libro l = new Libro("Carica",autori,data,"1234567891011",5);
        Prestito p = new Prestito(u,l,LocalDate.now());

        assertEquals(p.getUtente(),u);
        assertEquals(p.getLibro(),l);
    }

    @Test

    public void testLimiteMassimoPrestiti() {
        System.out.println("Test in cui provo a registrate un numero maggiore di 3 prestiti (deve fallire)");
        Utente u = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        List<String> autori = Arrays.asList("Autore");
        Libro l = new Libro("Libro", autori, LocalDate.now(), "1234567890123", 10);

        // Aggiungo 3 prestiti (il massimo consentito)
        for (int i = 0; i < 3; i++) {
            u.aggiungiPrestito(new Prestito(u, l, LocalDate.now()));
        }

        // Provo ad aggiungere il 4° prestito: deve fallire
        assertThrows(IllegalStateException.class, () -> {
            u.aggiungiPrestito(new Prestito(u, l, LocalDate.now()));
        });
    }

    @Test
    public void testAggiuntaPrestitoUtenteDiverso() {
        System.out.println("Aggiunta di un prestito a un utente diverso, deve fallire");
        Utente u1 = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        Utente u2 = new Utente("Luigi", "Verdi", "9876543210", "luigi@test.it");

        List<String> autori = Arrays.asList("Autore");
        Libro l = new Libro("Libro", autori, LocalDate.now(), "1234567890123", 5);

        // Creo un prestito intestato a U2
        Prestito prestitoDiU2 = new Prestito(u2, l, LocalDate.now());

        // Cerco di aggiungerlo alla lista di U1: deve fallire
        assertThrows(IllegalArgumentException.class, () -> {
            u1.aggiungiPrestito(prestitoDiU2);
        });
    }

    @Test
    public void testRimozionePrestito() {
        System.out.println("Verifichiamo che il prestito venga logicamente rimosso");
        Utente u = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        List<String> autori = Arrays.asList("Autore");
        Libro l = new Libro("Libro", autori, LocalDate.now(), "1234567890123", 5);
        Prestito p = new Prestito(u, l, LocalDate.now());

        u.aggiungiPrestito(p);
        assertEquals(1, u.getPrestitiAttivi().size());

        u.rimuoviPrestito(p);
        assertEquals(0, u.getPrestitiAttivi().size());
    }

    @Test
    public void testListaPrestitiImmutabileClear() {
        System.out.println("Verifichiamo che la lista prestiti ottenuta tramite getter sia effettivamente immutabile");
        Utente u = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        List<Prestito> lista = u.getPrestitiAttivi();

        // Prova a modificare la lista direttamente dall'esterno
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.clear(); // O lista.add(...)
        });


    }
    @Test
    public void testListaPrestitiImmutabileAdd() {
        System.out.println("Verifichiamo che la lista prestiti ottenuta tramite getter sia effettivamente immutabile");
        Utente u = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        List<Prestito> lista = u.getPrestitiAttivi();
        List<String> autori = Arrays.asList("Autore 1", "Autore 2");
        LocalDate data = LocalDate.of(2023, 1, 1);

        Libro l = new Libro("Titolo", autori, data, "1234567890123", 5);
        Prestito p = new Prestito(u,l,LocalDate.now());
        // Prova a modificare la lista direttamente dall'esterno
        assertThrows(UnsupportedOperationException.class, () -> {
            lista.add(p); // O lista.add(...)
        });


    }

    @Test
    public void testEqualsAndHashCode() {
        // Due oggetti distinti in memoria, ma con la stessa matricola
        Utente u1 = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        Utente u2 = new Utente("Mario", "Rossi", "0123456789", "mario@test.it"); // Dati uguali
        Utente u3 = new Utente("Luigi", "Verdi", "9876543210", "luigi@test.it"); // Matricola diversa

        assertEquals(u1, u2, "Due utenti con la stessa matricola devono essere equals");
        assertNotEquals(u1, u3, "Utenti con matricola diversa non devono essere equals");

        assertEquals(u1.hashCode(), u2.hashCode(), "Se sono equals, devono avere lo stesso hashCode");
    }

    @Test
    public void testSetterNonValidi() {
        Utente u = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");

        assertThrows(IllegalArgumentException.class, () -> u.setNome(""));
        assertThrows(IllegalArgumentException.class, () -> u.setCognome(null));
        assertThrows(IllegalArgumentException.class, () -> u.setEmail("email_sbagliata"));

        // Verifica che l'oggetto sia rimasto valido
        assertEquals("Mario", u.getNome());
    }


}
