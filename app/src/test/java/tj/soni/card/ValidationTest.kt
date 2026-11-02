package tj.soni.card

import org.junit.Assert.*
import org.junit.Test
import tj.soni.card.domain.Validation

class ValidationTest {
    @Test fun phoneAcceptsPlausibleDigitLengths() { assertTrue(Validation.phone("+992900000000")); assertFalse(Validation.phone("12")) }
    @Test fun nameRequiresTwoCharacters() { assertTrue(Validation.name("Ali")); assertFalse(Validation.name("A")) }
    @Test fun searchIsTrimmedAndBounded() { assertEquals("card", Validation.search("  card  ")); assertEquals(60, Validation.search("x".repeat(100)).length) }
}
