# Historia TDD (Red → Green → Refactor)

> Enlaces de código: [`Registry.java`](../src/main/java/edu/unisabana/tyvs/domain/service/Registry.java) · [`RegistryTest.java`](../src/test/java/edu/unisabana/tyvs/domain/service/RegistryTest.java)

## Iteración 1 — camino feliz

- **Rojo:** `shouldRegisterValidPerson` falla con `UnsupportedOperationException`
  porque `registerVoter` aún no tiene implementación.
- **Verde:** `registerVoter` devuelve `RegisterResult.VALID` sin importar
  la entrada (implementación mínima, "hace trampa" a propósito).
- **Refactor:** no había nada que limpiar todavía (refactor vacío, también
  válido dentro del ciclo).

## Iteración 2 — persona muerta

- **Rojo:** `shouldRejectDeadPerson` no compila porque `RegisterResult.DEAD`
  no existía. Un error de compilación también cuenta como rojo.
- **Verde:** se agrega la constante `DEAD` al enum y la guarda
  `if (!p.isAlive()) return RegisterResult.DEAD;`.
- **Refactor:** se ordenan las guardas, se agrega la validación defensiva
  de `null`, y se extrae `new Registry()` a un `@BeforeEach` — necesario
  porque `Registry` pasará a tener estado (ids registrados) y las pruebas
  deben quedar aisladas entre sí.

## Iteración 3 — id inválido (R2)

- **Rojo:** `shouldRejectWhenIdIsZeroOrNegative` (parametrizado con `0` y `-5`)
  falla porque cualquier id devuelve `VALID`.
- **Verde:** se agrega `if (p.getId() < MIN_VALID_ID) return RegisterResult.INVALID;`.
- **Refactor:** se extrae la constante `MIN_VALID_ID` en vez de usar el
  número mágico `1`.

## Iteración 4 — edad fuera de rango (R4)

- **Rojo:** `shouldRejectInvalidAgeBelowZero` y `shouldRejectInvalidAgeOver120`
  fallan porque no existe validación de edad.
- **Verde:** se agrega la guarda de rango `MIN_AGE`–`MAX_AGE` devolviendo
  `INVALID_AGE`. Este fue exactamente el defecto documentado en
  `defectos.md` (Defecto 01): antes de esta iteración, `edad = -1` devolvía
  `VALID`.
- **Refactor:** se extraen las constantes `MIN_AGE` y `MAX_AGE`.

## Iteración 5 — menor de edad (R5)

- **Rojo:** `shouldRejectUnderageAt17` falla; `shouldAcceptAdultAt18` y
  `shouldAcceptMaxAge120` sirven de contraprueba para no romper el camino
  válido.
- **Verde:** se agrega `if (p.getAge() < MIN_VOTING_AGE) return RegisterResult.UNDERAGE;`,
  **después** de la validación de rango de edad (R4) y **antes** de marcar
  `VALID`.
- **Refactor:** se extrae `MIN_VOTING_AGE`.

## Iteración 6 — duplicados (R6)

- **Rojo:** `shouldRejectDuplicatedId` falla porque `Registry` no tenía
  memoria de los ids ya vistos.
- **Verde:** se agrega `Set<Integer> registeredIds` de **instancia** (no
  estático) y la guarda de duplicado antes del `return VALID`.
- **Refactor:** se verifica con la propiedad `mismaEntradaProduceMismoResultado`
  (jqwik) que el estado no se filtra entre instancias distintas de `Registry`.

## Orden de evaluación como decisión de diseño

`shouldReturnDeadBeforeUnderageWhenBothApply` documenta explícitamente que
R3 (viva) se evalúa antes que R5 (mayoría de edad): una persona muerta de
15 años da `DEAD`, no `UNDERAGE`. Esta decisión está fijada en el orden de
los `if` dentro de `registerVoter` y es la razón por la que el orden de
las guardas en el código importa tanto como su contenido.
