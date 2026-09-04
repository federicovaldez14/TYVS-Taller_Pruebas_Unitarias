package edu.unisabana.tyvs.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de Person como objeto de datos puro. No forman parte de las
 * reglas de negocio de Registry, pero cierran los mutantes sin cobertura
 * que PIT reporto sobre getName() y getGender() (ver wiki/Resultados.md):
 * ningun test de Registry necesitaba consultar esos atributos, asi que
 * quedaban sin ejercitar.
 */
class PersonTest {

    @Test
    void shouldExposeAllConstructorValuesThroughGetters() {
        // Arrange
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act & Assert
        assertEquals("Ana", person.getName());
        assertEquals(1, person.getId());
        assertEquals(30, person.getAge());
        assertEquals(Gender.FEMALE, person.getGender());
        assertTrue(person.isAlive());
    }
}
