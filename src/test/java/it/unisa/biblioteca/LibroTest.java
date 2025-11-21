package it.unisa.biblioteca;

// NOTA: Gli import sono diversi rispetto a JUnit 4
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibroTest { // In JUnit 5 la classe non deve per forza essere public

    @Test
    void testCreazioneLibro() {
        // Setup
        Libro libro = new Libro("Ingegneria del Software", "Ian Sommerville");

        // Assert (Verifica)
        // JUnit 5 usa gli stessi metodi ma vengono dal pacchetto 'Assertions'
        assertEquals("Ingegneria del Software", libro.getTitolo());
        assertEquals("Ian Sommerville", libro.getAutore());
    }
}