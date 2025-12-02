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
}
