package it.unisa.biblioteca.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Unitari per la classe Libro.
 */
/**
 * Suite di test completa per la classe Libro.
 * Copre validazione input, logica di business e integrità dei dati (incapsulamento).
 */
class LibroTest {

    // --- 1. TEST COSTRUTTORE E CREAZIONE ---

    @Test
    @DisplayName("Costruttore: Creazione valida di un libro")
    void testCostruttoreValido() {
        List<String> autori = Arrays.asList("Autore 1", "Autore 2");
        LocalDate data = LocalDate.of(2023, 1, 1);

        Libro l = new Libro("La vita di un Ingegnere", autori, data, "9788845292613", 10);

        assertNotNull(l);
        assertEquals("La vita di un Ingegnere", l.getTitolo());
        assertEquals("9788845292613", l.getIsbn());
        assertEquals(2, l.getAutori().size());
        assertEquals(10, l.getDisponibilita());
        assertEquals(data, l.getDataPubblicazione());
    }

    // --- 2. TEST VALIDAZIONE TITOLO ---

    @Test
    @DisplayName("Costruttore: Titolo null deve fallire")
    void testTitoloNull() {
        List<String> autori = Collections.singletonList("autoreTest");
        assertThrows(IllegalArgumentException.class, () ->
                new Libro(null, autori, LocalDate.now(), "12345", 1)
        );
    }

    @Test
    @DisplayName("Costruttore: Titolo vuoto deve fallire")
    void testTitoloVuoto() {
        List<String> autori = Collections.singletonList("autoreTest");
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("", autori, LocalDate.now(), "12345", 1)
        );
    }

    @Test
    @DisplayName("Costruttore: Titolo solo spazi deve fallire")
    void testTitoloSoloSpazi() {
        List<String> autori = Collections.singletonList("autoreTest");
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("   ", autori, LocalDate.now(), "12345", 1)
        );
    }

    // --- 3. TEST VALIDAZIONE ISBN ---

    @Test
    @DisplayName("Costruttore: ISBN null deve fallire")
    void testIsbnNull() {
        List<String> autori = Collections.singletonList("autoreTest");
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", autori, LocalDate.now(), null, 1)
        );
    }

    @Test
    @DisplayName("Costruttore: ISBN vuoto deve fallire")
    void testIsbnVuoto() {
        List<String> autori = Collections.singletonList("autoreTest");
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", autori, LocalDate.now(), "", 1)
        );
    }

    // --- 4. TEST VALIDAZIONE AUTORI ---

    @Test
    @DisplayName("Costruttore: Lista autori null deve fallire")
    void testAutoriNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", null, LocalDate.now(), "12345", 1)
        );
    }

    @Test
    @DisplayName("Costruttore: Lista autori vuota deve fallire")
    void testAutoriVuoti() {
        List<String> autoriVuoti = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", autoriVuoti, LocalDate.now(), "12345", 1)
        );
    }

    // --- 5. TEST VALIDAZIONE DISPONIBILITÀ ---

    @Test
    @DisplayName("Costruttore: Disponibilità < 1 deve fallire")
    void testDisponibilitaZeroONegativa() {
        List<String> autori = Collections.singletonList("autoreTest");

        // Test con 0
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", autori, LocalDate.now(), "12345", 0)
        );

        // Test con negativo
        assertThrows(IllegalArgumentException.class, () ->
                new Libro("Titolo", autori, LocalDate.now(), "12345", -5)
        );
    }

    // --- 6. TEST INCAPSULAMENTO (Defensive Copy) ---

    @Test
    @DisplayName("Sicurezza: Defensive Copy nel costruttore")
    void testDefensiveCopyCostruttore() {
        // Creiamo una lista mutabile esterna
        List<String> autoriEsterni = new ArrayList<>();
        autoriEsterni.add("Autore 1");

        // Creiamo il libro passandogli la lista
        Libro l = new Libro("Titolo", autoriEsterni, LocalDate.now(), "12345", 5);

        // Modifichiamo la lista esterna DOPO aver creato il libro
        autoriEsterni.clear();

        // Il libro NON deve essere stato influenzato (la sua lista interna deve essere ancora piena)
        assertEquals(1, l.getAutori().size(), "Il libro non deve essere influenzato da modifiche esterne alla lista passata");
        assertEquals("Autore 1", l.getAutori().get(0));
    }

    @Test
    @DisplayName("Sicurezza: GetAutori restituisce lista non modificabile")
    void testGetAutoriImmutabile() {
        List<String> autori = Arrays.asList("Autore 1");
        Libro l = new Libro("Titolo", autori, LocalDate.now(), "12345", 5);

        // Prendo la lista dal getter
        List<String> listaDalGetter = l.getAutori();

        // Provo a modificarla: deve esplodere
        assertThrows(UnsupportedOperationException.class, () -> {
            listaDalGetter.add("Autore Intruso");
        });
    }

    // --- 7. TEST SETTERS ---

    @Test
    @DisplayName("Setter: Modifica valida del titolo")
    void testSetTitoloValido() {
        Libro l = new Libro("Vecchio", Collections.singletonList("A"), LocalDate.now(), "123", 1);
        l.setTitolo("Nuovo Titolo");
        assertEquals("Nuovo Titolo", l.getTitolo());
    }

    @Test
    @DisplayName("Setter: Titolo non valido nel setter")
    void testSetTitoloInvalido() {
        Libro l = new Libro("Ok", Collections.singletonList("A"), LocalDate.now(), "123", 1);
        assertThrows(IllegalArgumentException.class, () -> l.setTitolo(""));
        assertThrows(IllegalArgumentException.class, () -> l.setTitolo(null));
    }

    @Test
    @DisplayName("Setter: Modifica autori con defensive copy")
    void testSetAutoriDefensive() {
        Libro l = new Libro("Ok", Collections.singletonList("A"), LocalDate.now(), "123", 1);

        List<String> nuoviAutori = new ArrayList<>();
        nuoviAutori.add("Nuovo");

        l.setAutori(nuoviAutori);

        // Modifico la lista esterna usata per il set
        nuoviAutori.clear();

        // Il libro deve conservare "Nuovo"
        assertEquals(1, l.getAutori().size());
        assertEquals("Nuovo", l.getAutori().get(0));
    }

    // --- 8. TEST LOGICA DI BUSINESS (Incremento/Decremento) ---

    @Test
    @DisplayName("Logica: Incremento disponibilità")
    void testIncrementaDisponibilita() {
        Libro l = new Libro("Ok", Collections.singletonList("A"), LocalDate.now(), "123", 5);
        l.incrementaDisponibilita();
        assertEquals(6, l.getDisponibilita());
    }

    @Test
    @DisplayName("Logica: Decremento disponibilità valido")
    void testDecrementaDisponibilita() {
        Libro l = new Libro("Ok", Collections.singletonList("A"), LocalDate.now(), "123", 5);
        l.decrementaDisponibilita();
        assertEquals(4, l.getDisponibilita());
    }

    @Test
    @DisplayName("Logica: Decremento impossibile se copie esaurite")
    void testDecrementaDisponibilitaErrore() {
        // Creo libro con 1 copia
        Libro l = new Libro("Ok", Collections.singletonList("A"), LocalDate.now(), "123", 1);

        // Tolgo l'unica copia
        l.decrementaDisponibilita(); // Ora è 0
        assertEquals(0, l.getDisponibilita());

        // Provo a togliere ancora: Errore
        assertThrows(IllegalStateException.class, () -> {
            l.decrementaDisponibilita();
        });
    }

    // --- 9. TEST EQUALS & HASHCODE ---

    @Test
    @DisplayName("Identità: Equals basato su ISBN")
    void testEquals() {
        List<String> a = Collections.singletonList("A");
        // Due libri diversi in memoria ma con STESSO ISBN
        Libro l1 = new Libro("Titolo A", a, LocalDate.now(), "978-12345", 5);
        Libro l2 = new Libro("Titolo B", a, LocalDate.of(2000,1,1), "978-12345", 1); // Anche se attributi diversi

        assertEquals(l1, l2, "Due libri con lo stesso ISBN devono essere uguali");
        assertEquals(l1.hashCode(), l2.hashCode(), "Se uguali, devono avere lo stesso hashcode");
    }

    @Test
    @DisplayName("Identità: Not Equals con ISBN diversi")
    void testNotEquals() {
        List<String> a = Collections.singletonList("A");
        Libro l1 = new Libro("Titolo", a, LocalDate.now(), "ISBN-1", 5);
        Libro l2 = new Libro("Titolo", a, LocalDate.now(), "ISBN-2", 5);

        assertNotEquals(l1, l2);
    }
}