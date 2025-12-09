package it.unisa.biblioteca.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Prestito.
 * Verifica la creazione, la validazione degli input e la logica temporale (scadenze).
 */
class PrestitoTest {

    private Utente utenteTest;
    private Libro libroTest;

    // Eseguito prima di OGNI test per avere dati puliti
    @BeforeEach
    void setUp() {
        utenteTest = new Utente("Mario", "Rossi", "0123456789", "mario@test.it");
        libroTest = new Libro("Titolo", Collections.singletonList("Autore"), LocalDate.of(2020, 1, 1), "9781234567890", 5);
    }

    // --- 1. TEST COSTRUTTORE E CREAZIONE ---

    @Test
    @DisplayName("Costruttore: Creazione valida e calcolo scadenza (60gg)")
    void testCostruttoreValido() {
        LocalDate dataInizio = LocalDate.of(2023, 1, 1);

        Prestito p = new Prestito(utenteTest, libroTest, dataInizio);

        assertNotNull(p);
        assertEquals(utenteTest, p.getUtente());
        assertEquals(libroTest, p.getLibro());
        assertEquals(dataInizio, p.getDataInizio());

        // Verifica Regola Business: Scadenza deve essere esattamente dopo 60 giorni
        LocalDate scadenzaAttesa = dataInizio.plusDays(60);
        assertEquals(scadenzaAttesa, p.getDataFinePrevista(), "La scadenza deve essere 60 giorni dopo l'inizio");
    }

    // --- 2. TEST VALIDAZIONE INPUT (Null Safety) ---

    @Test
    @DisplayName("Costruttore: Utente null deve fallire")
    void testUtenteNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Prestito(null, libroTest, LocalDate.now())
        );
    }

    @Test
    @DisplayName("Costruttore: Libro null deve fallire")
    void testLibroNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Prestito(utenteTest, null, LocalDate.now())
        );
    }

    @Test
    @DisplayName("Costruttore: DataInizio null deve fallire")
    void testDataInizioNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Prestito(utenteTest, libroTest, null)
        );
    }

    // --- 3. TEST LOGICA DI SCADENZA (Business Logic) ---

    @Test
    @DisplayName("Logica: Prestito NON scaduto (fatto oggi)")
    void testNonScaduto() {
        // Un prestito fatto oggi scade tra 60 giorni, quindi NON è scaduto
        Prestito p = new Prestito(utenteTest, libroTest, LocalDate.now());

        assertFalse(p.isScaduto(), "Un prestito appena fatto non può essere scaduto");
    }

    @Test
    @DisplayName("Logica: Prestito SCADUTO (fatto 61 giorni fa)")
    void testScaduto() {
        // Simuliamo un prestito fatto 61 giorni fa
        // Scadenza prevista = 61 giorni fa + 60 giorni = Ieri
        LocalDate passato = LocalDate.now().minusDays(61);
        Prestito p = new Prestito(utenteTest, libroTest, passato);

        assertTrue(p.isScaduto(), "Il prestito doveva scadere ieri, quindi è scaduto");
    }

    @Test
    @DisplayName("Logica: Prestito che scade OGGI (Caso limite)")
    void testScadeOggi() {
        // Fatto esattamente 60 giorni fa => Scade oggi
        LocalDate passato = LocalDate.now().minusDays(60);
        Prestito p = new Prestito(utenteTest, libroTest, passato);

        // Se scade oggi, tecnicamente non è ancora "scaduto" (isAfter è rigoroso)
        // La logica isScaduto() usa now().isAfter(scadenza). 
        // Se oggi == scadenza, isAfter restituisce false.
        assertFalse(p.isScaduto(), "Se scade oggi, l'utente ha ancora tempo fino a stasera");
    }

    // --- 4. TEST GIORNI MANCANTI ---

    @Test
    @DisplayName("Logica: Giorni alla scadenza corretti")
    void testGiorniAllaScadenza() {
        // Prestito fatto oggi -> Mancano 60 giorni
        Prestito p = new Prestito(utenteTest, libroTest, LocalDate.now());

        assertEquals(60, p.giorniAllaScadenza());
    }

    @Test
    @DisplayName("Logica: Giorni di ritardo (valore negativo)")
    void testGiorniRitardo() {
        // Prestito fatto 70 giorni fa -> Scaduto da 10 giorni
        // Giorni alla scadenza dovrebbe essere -10
        LocalDate passato = LocalDate.now().minusDays(70);
        Prestito p = new Prestito(utenteTest, libroTest, passato);

        assertEquals(-10, p.giorniAllaScadenza());
    }

    // --- 5. TEST TO STRING ---

    @Test
    @DisplayName("ToString: Verifica formato stringa")
    void testToString() {
        Prestito p = new Prestito(utenteTest, libroTest, LocalDate.of(2023, 1, 1));
        String s = p.toString();

        // Verifichiamo che contenga i pezzi importanti
        assertTrue(s.contains("Titolo")); // Titolo libro
        assertTrue(s.contains("Rossi"));  // Cognome utente
        assertTrue(s.contains("2023-03-02")); // Data scadenza (1 Gen + 60gg = 2 Mar, o 1 Mar se bisestile/non bisestile, il check contiene la data calcolata)
        // Nota: per evitare problemi con anni bisestili nel test della stringa esatta, 
        // meglio controllare che contenga i dati di input noti.
    }
}