# Registro de defectos

## Defecto 01

- **Caso:** persona viva, id valido, edad = -1
- **Esperado:** `INVALID_AGE`
- **Obtenido:** `VALID`
- **Version afectada:** implementacion minima resultante de la Iteracion 2 (Red-Green-Refactor), que solo validaba `p == null` y `!p.isAlive()`. Cualquier otra combinacion, incluida una edad de -1, caia directo en el `return RegisterResult.VALID;` que quedo como implementacion por defecto tras esa iteracion.
- **Causa probable:** en TDD la implementacion solo crece hasta donde las pruebas existentes la obligan. Como todavia no existia una prueba para edades fuera del rango biologicamente posible, no habia ninguna guarda que la rechazara.
- **Prueba que lo detecto (una vez agregada):** `RegistryTest.shouldRejectInvalidAgeBelowZero`
- **Solucion aplicada:** se agrego la regla R4 en `Registry.registerVoter`, evaluada antes que R5 (mayoria de edad):
  ```java
  if (p.getAge() < MIN_AGE || p.getAge() > MAX_AGE) {
      return RegisterResult.INVALID_AGE;
  }
  ```
- **Estado:** Cerrado

---

## Defecto 02 (opcional - a completar por el equipo)

- **Caso:**
- **Esperado:**
- **Obtenido:**
- **Causa probable:**
- **Estado:** Abierto / Cerrado

> Sugerencia: corran `mvn test-compile org.pitest:pitest-maven:mutationCoverage` y revisen si algun mutante sobreviviente revela un defecto real (no solo una linea sin cubrir). Documentenlo aqui con el mismo formato.
