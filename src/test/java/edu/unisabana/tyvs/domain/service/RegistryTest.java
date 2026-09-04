package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas unitarias de Registry.registerVoter(Person), escritas con el
 * patron AAA (Arrange - Act - Assert). Cada metodo cubre una clase de
 * equivalencia / valor limite de la matriz de pruebas (ver matriz-pruebas.md).
 */
class RegistryTest {

    private Registry registry;

    @BeforeEach
    void setUp() {
        // instancia limpia para cada prueba: evita que el estado
        // (ids ya registrados) de una prueba contamine a otra
        registry = new Registry();
    }

    @Test
    @DisplayName("Given una persona viva, mayor de edad y con id unico, " +
                 "When se registra, Then el resultado es VALID")
    void shouldRegisterValidPerson() {
        // Arrange
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    @DisplayName("Given una persona no viva, When se registra, Then el resultado es DEAD")
    void shouldRejectDeadPerson() {
        // Arrange
        Person dead = new Person("Carlos", 2, 40, Gender.MALE, false);

        // Act
        RegisterResult result = registry.registerVoter(dead);

        // Assert
        assertEquals(RegisterResult.DEAD, result);
    }

    @Test
    @DisplayName("Given la persona es null, When se registra, Then el resultado es INVALID")
    void shouldReturnInvalidWhenPersonIsNull() {
        // Arrange: el propio null es el caso de prueba, no hay objeto que preparar

        // Act
        RegisterResult result = registry.registerVoter(null);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

    @ParameterizedTest(name = "id = {0}")
    @ValueSource(ints = {0, -5})
    @DisplayName("Given un id cero o negativo, When se registra, Then el resultado es INVALID")
    void shouldRejectWhenIdIsZeroOrNegative(int id) {
        // Arrange
        Person person = new Person("Luis", id, 25, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

    @Test
    @DisplayName("Given una persona de 17 anios, viva y con id valido, " +
                 "When se registra, Then el resultado es UNDERAGE")
    void shouldRejectUnderageAt17() {
        // Arrange
        Person person = new Person("Sofia", 3, 17, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.UNDERAGE, result);
    }

    @Test
    @DisplayName("Given una persona de 18 anios, viva y con id valido, " +
                 "When se registra, Then el resultado es VALID")
    void shouldAcceptAdultAt18() {
        // Arrange
        Person person = new Person("Mateo", 4, 18, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    @DisplayName("Given una persona de 120 anios, viva y con id valido, " +
                 "When se registra, Then el resultado es VALID")
    void shouldAcceptMaxAge120() {
        // Arrange
        Person person = new Person("Rosa", 5, 120, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    @DisplayName("Given una persona de 121 anios, viva y con id valido, " +
                 "When se registra, Then el resultado es INVALID_AGE")
    void shouldRejectInvalidAgeOver120() {
        // Arrange
        Person person = new Person("Jorge", 6, 121, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    @DisplayName("Given una persona con edad negativa, viva y con id valido, " +
                 "When se registra, Then el resultado es INVALID_AGE")
    void shouldRejectInvalidAgeBelowZero() {
        // Arrange
        Person person = new Person("Elena", 7, -1, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    @DisplayName("Given un id ya registrado, When se intenta registrar de nuevo, " +
                 "Then el resultado es DUPLICATED")
    void shouldRejectDuplicatedId() {
        // Arrange
        Person first = new Person("Pedro", 8, 25, Gender.MALE, true);
        Person duplicate = new Person("Pedro Impostor", 8, 30, Gender.MALE, true);
        registry.registerVoter(first);

        // Act
        RegisterResult result = registry.registerVoter(duplicate);

        // Assert
        assertEquals(RegisterResult.DUPLICATED, result);
    }

    @Test
    @DisplayName("Given una persona muerta y ademas menor de edad, When se registra, " +
                 "Then el resultado es DEAD y no UNDERAGE (R3 se evalua antes que R5)")
    void shouldReturnDeadBeforeUnderageWhenBothApply() {
        // Arrange
        Person deadMinor = new Person("Nino", 9, 15, Gender.MALE, false);

        // Act
        RegisterResult result = registry.registerVoter(deadMinor);

        // Assert
        assertEquals(RegisterResult.DEAD, result);
    }
}
