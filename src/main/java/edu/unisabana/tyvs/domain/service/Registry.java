package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;

import java.util.HashSet;
import java.util.Set;

/**
 * Caso de uso: registrar votantes para la Registraduria.
 *
 * Las reglas de negocio se evaluan en el orden R1 -> R7 y la primera
 * que falla determina el resultado (decision de diseno documentada en
 * la especificacion del taller). Por ejemplo, una persona muerta de
 * 15 anios devuelve DEAD, no UNDERAGE, porque R3 (viva) se evalua
 * antes que R5 (mayoria de edad).
 */
public class Registry {

    /** R2: el id debe ser positivo (mayor o igual a este valor). */
    public static final int MIN_VALID_ID = 1;

    /** R4: rango de edad biologicamente posible. */
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 120;

    /** R5: edad minima para poder votar. */
    public static final int MIN_VOTING_AGE = 18;

    private final Set<Integer> registeredIds = new HashSet<>();

    public RegisterResult registerVoter(Person p) {
        // R1: la persona no puede ser nula
        if (p == null) {
            return RegisterResult.INVALID;
        }

        // R2: el numero de documento debe ser positivo (id > 0)
        if (p.getId() < MIN_VALID_ID) {
            return RegisterResult.INVALID;
        }

        // R3: la persona debe estar viva
        if (!p.isAlive()) {
            return RegisterResult.DEAD;
        }

        // R4: la edad debe ser biologicamente posible (0 <= edad <= 120)
        if (p.getAge() < MIN_AGE || p.getAge() > MAX_AGE) {
            return RegisterResult.INVALID_AGE;
        }

        // R5: la persona debe ser mayor de edad (edad >= 18)
        if (p.getAge() < MIN_VOTING_AGE) {
            return RegisterResult.UNDERAGE;
        }

        // R6: solo se permite una inscripcion por numero de documento
        if (registeredIds.contains(p.getId())) {
            return RegisterResult.DUPLICATED;
        }

        // R7: si cumple todas las anteriores, queda registrada
        registeredIds.add(p.getId());
        return RegisterResult.VALID;
    }
}
