package it.unisa.biblioteca.model;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
}
