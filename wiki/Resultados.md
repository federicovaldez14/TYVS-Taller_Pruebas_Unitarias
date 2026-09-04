# Resultados

> Esta página es una plantilla. Corran los comandos indicados en cada
> sección, tomen la captura del reporte HTML correspondiente y péguenla
> aquí antes de entregar.

## Cobertura de código (JaCoCo)

```bash
mvn clean verify
# abrir target/site/jacoco/index.html
```

| Elemento | Instrucciones | Ramas | Líneas | Métodos | Clases |
|---|---|---|---|---|---|
| `edu.unisabana.tyvs.domain.model` | 100% | n/a | 23/23 | 8/8 | 3/3 |
| `edu.unisabana.tyvs.domain.service` | 100% | 100% | 16/16 | 2/2 | 1/1 |
| **Total** | **100%** (0 de 149) | **100%** (0 de 14) | **39/39** | **10/10** | **4/4** |

- **Cobertura global: 100%**, muy por encima del mínimo exigido (≥80% global y ≥80% en dominio).
- No hay líneas sin cubrir que explicar: las 39 líneas del dominio (`model` + `service`) están ejercitadas por `RegistryTest`, `RegistryPropertiesTest` y `PersonTest`.
- El paquete `domain.model` reporta ramas como "n/a" porque `Person`, `Gender` y `RegisterResult` no tienen lógica condicional (son un objeto de datos y dos enums); toda la complejidad ciclomática del dominio vive en `Registry` (`domain.service`), donde las ramas también llegan a 100%.

## Pruebas de mutación (PIT)

```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
# abrir target/pit-reports/index.html
```

**Primera corrida** (antes de `PersonTest`):
- Mutation score: 92% (22/24 mutantes eliminados).
- Line coverage sobre clases mutadas: 93% (26/28).
- 2 mutantes en `NO_COVERAGE`: `EmptyObjectReturnValsMutator` sobre `Person.getName()` y `NullReturnValsMutator` sobre `Person.getGender()`. Ninguna prueba llamaba a esos getters porque `registerVoter` nunca los consulta — no era un defecto de negocio, sino código de `Person` sin ejercitar.

**Segunda corrida** (después de agregar `PersonTest.shouldExposeAllConstructorValuesThroughGetters`):
- **Mutation score: 100% (24/24 mutantes eliminados).**
- **Line coverage sobre clases mutadas: 100% (28/28).**
- Test strength: 100%. `BUILD SUCCESS`, muy por encima del umbral configurado (60%).
- Los 7 mutadores usados (`ConditionalsBoundaryMutator`, `PrimitiveReturnsMutator`, `BooleanTrueReturnValsMutator`, `NullReturnValsMutator`, `EmptyObjectReturnValsMutator`, `BooleanFalseReturnValsMutator`, `NegateConditionalsMutator`) quedaron en 100% cada uno.

**Mutante analizado (ejercicio del punto 2.1 de la guía):** el mutante `NullReturnValsMutator` sobre `Person.getGender()` sobrevivía porque ninguna prueba verificaba el valor de retorno de ese getter — el código se ejecutaba al construir el objeto (por eso JaCoCo ya lo contaba como "cubierto" antes de mutación), pero ningún assert dependía de él. La prueba que lo elimina es `PersonTest.shouldExposeAllConstructorValuesThroughGetters`, que agrega `assertEquals(Gender.FEMALE, person.getGender())`.

## Comparación cobertura vs. mutación

En este proyecto **no hay salto** entre ambas métricas: JaCoCo reporta 100% de cobertura y PIT reporta 100% de mutation score. Esto es coherente porque cada línea del dominio está atada a al menos una aserción que verifica un resultado concreto (`RegisterResult`, o los valores de los getters de `Person`), y no hay pruebas "vacías" que solo ejecuten código sin comprobar nada (lo que la guía llama *coverage theater*).

Vale la pena notar que esto **no es automático**: en la primera corrida de PIT, antes de agregar `PersonTest`, la cobertura de líneas ya rondaba el 93% pero el mutation score se quedó en 92% con 2 mutantes sin ejecutar (`Person.getName()` y `Person.getGender()`, ver arriba). Fue necesario agregar una prueba que llamara explícitamente a esos getters para que ambas métricas convergieran en 100%. Ese es justamente el caso que demuestra por qué la cobertura sola no basta: una línea puede estar "cubierta" (ejecutada al construir el objeto) sin que ningún assert dependa de su valor.

## Pruebas basadas en propiedades (jqwik)

- Propiedades implementadas: 5 (`unaPersonaNoVivaSiempreEsRechazada`,
  `todoMenorDeEdadEsRechazado`, `todoAdultoValidoSeRegistra`,
  `registerVoterNuncaDevuelveNullNiLanzaExcepcion`,
  `mismaEntradaProduceMismoResultado`).

### Contraejemplo reducido (shrinking)

Para obtener un ejemplo real de shrinking, se rompió deliberadamente la
regla R5 en `Registry.java`, cambiando `MIN_VOTING_AGE = 18` por `= 19`,
y se corrió solo `todoAdultoValidoSeRegistra` (que cubre el rango de
edad 18-120):

```
mvn test -Dtest=RegistryPropertiesTest#todoAdultoValidoSeRegistra
```

jqwik encontró el fallo en el intento 28 de 1000 y lo redujo así:

```
Original Sample
---------------
  arg0 (id):     275
  arg1 (edad):   18
  arg2 (genero): UNIDENTIFIED

Shrunk Sample (2 pasos)
------------------------
  arg0 (id):     1
  arg1 (edad):   18
  arg2 (genero): MALE

Original Error
---------------
org.opentest4j.AssertionFailedError: expected: <VALID> but was: <UNDERAGE>
```

**Por qué la versión reducida es más útil que la original:** la entrada
aleatoria inicial (`id=275, edad=18, genero=UNIDENTIFIED`) mezcla tres
valores que no aportan nada a explicar el fallo: el `id` podría ser
cualquiera y el género tampoco influye en la regla rota. jqwik lo redujo
a `id=1, genero=MALE` — los valores "más simples" posibles dentro de sus
rangos — dejando visible que **lo único que importa es `edad=18`**, el
valor límite exacto donde `MIN_VOTING_AGE` decide el resultado. Con el
ejemplo reducido, la causa salta a la vista de inmediato; con el original
habría que descartar manualmente el `id` y el género antes de notar que
el problema es la edad.

Tras confirmar el fallo, se revirtió `MIN_VOTING_AGE` a `18` y se corrió
`mvn clean test`: las 18 pruebas volvieron a pasar sin errores.

## Conclusiones técnicas

- **Cobertura ≠ verificación.** Con solo `RegistryTest` + `RegistryPropertiesTest`, JaCoCo ya marcaba ~93% de líneas cubiertas, pero PIT mostró 2 mutantes sin ejecutar en `Person`. Fue necesario un test dedicado (`PersonTest`) para llevar ambas métricas a 100%, confirmando que "ejecutado" y "verificado" no son lo mismo.
- **Las propiedades encuentran errores que los ejemplos no buscarían.** El experimento de romper `MIN_VOTING_AGE` (18→19) mostró que una propiedad sobre todo el rango 18-120 detecta un error de calibración que una prueba por ejemplo aislada (`shouldAcceptAdultAt18`) también habría detectado, pero con la ventaja de que jqwik prueba automáticamente 1000 combinaciones distintas por corrida, no solo el caso que a alguien se le ocurrió escribir.
- **El shrinking ahorra tiempo de diagnóstico.** Reducir `(id=275, edad=18, UNIDENTIFIED)` a `(id=1, edad=18, MALE)` deja el valor realmente responsable del fallo (la edad) sin ruido de los demás parámetros — el valor de una herramienta de property-based testing no está solo en generar casos, sino en simplificarlos cuando fallan.
- **El orden de las reglas de negocio es una decisión de diseño, no un detalle de implementación.** Documentar explícitamente que R3 (viva) se evalúa antes que R5 (mayoría de edad) — y probarlo con `shouldReturnDeadBeforeUnderageWhenBothApply` — evita ambigüedad sobre qué resultado es "correcto" cuando varias reglas fallan a la vez.
