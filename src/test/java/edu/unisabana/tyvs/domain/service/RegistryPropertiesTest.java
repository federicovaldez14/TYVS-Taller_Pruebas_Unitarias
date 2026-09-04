package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.IntRange;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Pruebas basadas en propiedades (property-based testing) sobre
 * Registry.registerVoter(Person), usando jqwik.
 *
 * Se incluyen:
 *  - Propiedades que traducen reglas de negocio (R3, R5, R7).
 *  - Propiedades estructurales: totalidad, determinismo e invariante
 *    de particion, que no dependen de una regla en particular sino
 *    de contratos generales que el metodo debe cumplir siempre.
 */
class RegistryPropertiesTest {

    @Provide
    Arbitrary<Gender> generos() {
        return Arbitraries.of(Gender.values());
    }

    /** R3: una persona no viva se rechaza SIEMPRE, sin importar lo demas. */
    @Property
    void unaPersonaNoVivaSiempreEsRechazada(
            @ForAll @IntRange(min = 1, max = 100_000) int id,
            @ForAll @IntRange(min = 0, max = 120) int edad,
            @ForAll("generos") Gender genero) {

        Person muerta = new Person("X", id, edad, genero, false);

        assertEquals(RegisterResult.DEAD, new Registry().registerVoter(muerta));
    }

    /** R5: toda persona viva, con id valido, y menor de 18 anios es UNDERAGE. */
    @Property
    void todoMenorDeEdadEsRechazado(
            @ForAll @IntRange(min = 1, max = 100_000) int id,
            @ForAll @IntRange(min = 0, max = 17) int edad,
            @ForAll("generos") Gender genero) {

        Person menor = new Person("X", id, edad, genero, true);

        assertEquals(RegisterResult.UNDERAGE, new Registry().registerVoter(menor));
    }

    /** R7: todo adulto vivo, con id valido y unico en su registro, queda VALID. */
    @Property
    void todoAdultoValidoSeRegistra(
            @ForAll @IntRange(min = 1, max = 100_000) int id,
            @ForAll @IntRange(min = 18, max = 120) int edad,
            @ForAll("generos") Gender genero) {

        Person adulto = new Person("X", id, edad, genero, true);

        assertEquals(RegisterResult.VALID, new Registry().registerVoter(adulto));
    }

    /**
     * Propiedad estructural - Totalidad: registerVoter nunca devuelve
     * null ni lanza una excepcion, sea cual sea la entrada (incluyendo
     * valores fuera de rango de edad e id).
     */
    @Property
    void registerVoterNuncaDevuelveNullNiLanzaExcepcion(
            @ForAll @IntRange(min = -10, max = 100_000) int id,
            @ForAll @IntRange(min = -10, max = 130) int edad,
            @ForAll("generos") Gender genero,
            @ForAll boolean vivo) {

        Person p = new Person("X", id, edad, genero, vivo);

        assertDoesNotThrow(() -> assertNotNull(new Registry().registerVoter(p)));
    }

    /**
     * Propiedad estructural - Determinismo: la misma entrada, evaluada
     * sobre registros nuevos e independientes, produce siempre el
     * mismo resultado. Se rompe si alguien introduce estado compartido
     * (por ejemplo un Set estatico de ids en vez de uno de instancia).
     */
    @Property
    void mismaEntradaProduceMismoResultado(
            @ForAll @IntRange(min = 1, max = 100_000) int id,
            @ForAll @IntRange(min = -5, max = 130) int edad,
            @ForAll("generos") Gender genero,
            @ForAll boolean vivo) {

        Person p1 = new Person("X", id, edad, genero, vivo);
        Person p2 = new Person("X", id, edad, genero, vivo);

        RegisterResult r1 = new Registry().registerVoter(p1);
        RegisterResult r2 = new Registry().registerVoter(p2);

        assertEquals(r1, r2);
    }
}
