# Patrón AAA (Arrange – Act – Assert)

Todas las pruebas de `RegistryTest` siguen la misma estructura de tres
pasos, separados por comentarios, para que cualquier lector identifique
de inmediato qué se prepara, qué se ejecuta y qué se verifica:

```java
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
```

- **Arrange:** se construye el `Person` de entrada con los atributos
  relevantes para el caso (aquí, `alive = false`).
- **Act:** una única línea que invoca el método bajo prueba.
- **Assert:** una única aserción que compara el resultado esperado contra
  el obtenido.

## Pautas usadas en el proyecto

- `@BeforeEach` centraliza la parte de "Arrange" que se repite en todas
  las pruebas (`registry = new Registry();`), evitando duplicación sin
  romper la separación AAA dentro de cada test.
- Nomenclatura `should<Resultado>When<Condición>` (o descripciones BDD en
  `@DisplayName`) para que el nombre del método ya comunique el escenario.
- Una sola aserción por prueba: cada test verifica un único resultado de
  `RegisterResult`, lo que facilita saber exactamente qué regla se rompió
  cuando falla.
- En `RegistryPropertiesTest`, el mismo patrón se aplica dentro de cada
  `@Property`: Arrange (construir la `Person` con los valores generados
  por jqwik), Act (invocar `registerVoter`), Assert (verificar la
  propiedad).
