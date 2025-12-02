package it.unisa.biblioteca.model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Unitari per la classe Libro.
 */
public class LibroTest {

    // Dati di prova comuni
    private final List<String> autori = Arrays.asList("Autore 1", "Autore 2");
    private final LocalDate data = LocalDate.of(2023, 1, 1);

    @Test
    public void testCostruttoreValido() {
        System.out.println("Test creazione Libro valida");
        Libro l = new Libro("Titolo", autori, data, "1234567890123", 5);

        assertEquals("Titolo", l.getTitolo());
        assertEquals("1234567890123", l.getIsbn());
        assertEquals(5, l.getDisponibilita());
    }

    @Test
    public void testCostruttoreTitoloNull() {
        System.out.println("Test creazione Libro con titolo null (deve fallire)");

        // AssertThrows verifica che il codice lanci l'eccezione specificata
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro(null, autori, data, "123", 5);
        });
    }

    @Test
    public void testCostruttoreCopieNegative() {
        System.out.println("Test creazione Libro con copie negative");
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("Titolo", autori, data, "123", -1);
        });
    }

    @Test
    public void testIncrementaDisponibilita() {
        System.out.println("Test incrementa disponibilità");
        Libro l = new Libro("T", autori, data, "123", 1);
        l.incrementaDisponibilita();
        assertEquals(2, l.getDisponibilita());
    }

    @Test
    public void testDecrementaDisponibilitaOk() {
        System.out.println("Test decrementa disponibilità (caso valido)");
        Libro l = new Libro("T", autori, data, "123", 1);
        l.decrementaDisponibilita();
        assertEquals(0, l.getDisponibilita());
    }

    @Test
    public void testDecrementaDisponibilitaErrore() {
        System.out.println("Test decrementa disponibilità quando è 0 (deve fallire)");
        Libro l = new Libro("T", autori, data, "123", 1);

        l.decrementaDisponibilita();;
        // Se provo a togliere una copia quando ne ho 0, deve lanciare IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            l.decrementaDisponibilita();
        });
    }
}