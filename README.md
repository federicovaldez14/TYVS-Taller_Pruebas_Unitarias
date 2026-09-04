# Taller de Pruebas Unitarias — Registraduría (TDD)

Implementación completa del ejercicio "Registraduría" del taller de TDD /
Clean Architecture: `Registry.registerVoter(Person)` con las 7 reglas de
negocio (R1–R7), pruebas AAA, pruebas basadas en propiedades (jqwik),
cobertura (JaCoCo) y mutación (PIT).

## Estructura

```
src/
 ├─ main/java/edu/unisabana/tyvs/domain/
 │   ├─ model/     Person, Gender, RegisterResult
 │   └─ service/   Registry (registerVoter)
 └─ test/java/edu/unisabana/tyvs/domain/service/
     ├─ RegistryTest.java             (pruebas por ejemplo, AAA)
     └─ RegistryPropertiesTest.java   (pruebas basadas en propiedades, jqwik)
```

Archivos de soporte para la entrega:

- `defectos.md` — registro de defectos encontrados durante el desarrollo TDD.
- `matriz-pruebas.md` — clases de equivalencia, valores límite y test que cubre cada caso.
- `integrantes.txt` — **completar con los nombres reales del equipo**.
- `wiki/` — borradores en Markdown para pegar en la Wiki de GitHub (ver más abajo).

## Cómo correr el proyecto

Requiere JDK 17 (o ajusta `maven.compiler.release` en el `pom.xml` a tu versión) y Maven.

```bash
# Compilar
mvn clean compile

# Correr todas las pruebas (JUnit 5 + jqwik)
mvn clean test

# Cobertura de código (JaCoCo) -> target/site/jacoco/index.html
mvn clean verify

# Pruebas de mutación (PIT) -> target/pit-reports/index.html
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

## Reglas de negocio implementadas (orden de evaluación R1→R7)

| Regla | Condición | Resultado |
|---|---|---|
| R1 | `person == null` | `INVALID` |
| R2 | `id <= 0` | `INVALID` |
| R3 | `!alive` | `DEAD` |
| R4 | `edad < 0` o `edad > 120` | `INVALID_AGE` |
| R5 | `edad < 18` | `UNDERAGE` |
| R6 | `id` ya registrado | `DUPLICATED` |
| R7 | ninguna de las anteriores | `VALID` |

La primera regla que falla determina el resultado (por diseño, una persona
muerta y menor de edad da `DEAD`, no `UNDERAGE`; ver `matriz-pruebas.md`
y `shouldReturnDeadBeforeUnderageWhenBothApply`).
