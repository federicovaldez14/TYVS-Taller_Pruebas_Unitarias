# Taller de Pruebas Unitarias — Registraduría

## Resumen del dominio

El sistema modela el registro de votantes de una registraduría. El caso
de uso central, `Registry.registerVoter(Person)`, decide si una persona
puede registrarse como votante evaluando siete reglas de negocio (R1–R7):
nulidad, validez del id, si la persona está viva, si su edad es
biológicamente posible, si es mayor de edad, si el id ya fue registrado,
y finalmente si queda válida.

## Alcance del taller

- Diseño guiado por pruebas (TDD: Red → Green → Refactor) del dominio
  `Registry`, siguiendo Arquitectura Limpia (el dominio no depende de
  frameworks ni infraestructura).
- Pruebas por ejemplo con patrón AAA (Arrange–Act–Assert).
- Clases de equivalencia y valores límite.
- Escenarios BDD (Given–When–Then) trazables a cada test.
- Pruebas basadas en propiedades (jqwik).
- Cobertura de código (JaCoCo) y pruebas de mutación (PIT).
- Gestión de defectos (`defectos.md`).

## Equipo

_(completar con los nombres del equipo — ver `integrantes.txt` en el repo)_

## Índice de la Wiki

- [Historia TDD](Historia-TDD)
- [Patrón AAA](Patron-AAA)
- [Clases de Equivalencia y Valores Límite](Clases-Equivalencia)
- [BDD](BDD)
- [Resultados](Resultados)
