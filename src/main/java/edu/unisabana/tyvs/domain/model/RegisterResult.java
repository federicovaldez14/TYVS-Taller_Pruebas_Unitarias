package edu.unisabana.tyvs.domain.model;

/**
 * Resultado posible de {@code Registry.registerVoter(Person)}.
 *
 * Cada constante corresponde a una regla de negocio (R1-R7) definida
 * en la especificacion del taller:
 *
 *  VALID        (R7) cumple todas las reglas
 *  DUPLICATED   (R6) id ya registrado
 *  INVALID      (R1/R2) persona nula o id <= 0
 *  DEAD         (R3) no esta viva
 *  UNDERAGE     (R5) 0 <= edad < 18
 *  INVALID_AGE  (R4) edad < 0 o edad > 120
 */
public enum RegisterResult {
    VALID,
    DUPLICATED,
    INVALID,
    DEAD,
    UNDERAGE,
    INVALID_AGE
}
