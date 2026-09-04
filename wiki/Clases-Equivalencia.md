# Clases de Equivalencia y Valores Límite

El espacio de entradas de `registerVoter(Person)` se particionó en cuatro
atributos del dominio:

## Edad

- Inválida: `edad < 0` → `INVALID_AGE` (límite: `-1`)
- Menor: `0 ≤ edad < 18` → `UNDERAGE` (límites: `17` y `18`)
- Válida: `18 ≤ edad ≤ 120` → contribuye a `VALID` (límites: `18`, `120`)
- Inválida: `edad > 120` → `INVALID_AGE` (límite: `121`)

## Estado de vida

- `alive = false` → `DEAD`, independiente de la edad (prioridad sobre R4/R5).
- `alive = true` → continúa la evaluación de edad y duplicados.

## Identificador (unicidad)

- Inválido: `id ≤ 0` → `INVALID` (límites: `0` y `-1`)
- Duplicado: mismo `id` ya registrado → `DUPLICATED`
- Único: `id` no registrado → continúa la evaluación

## Nulidad

- `person == null` → `INVALID` (validación defensiva, primera guarda)

## Tabla completa con trazabilidad a tests

Ver [`matriz-pruebas.md`](../matriz-pruebas.md) en la raíz del repositorio:
contiene las 12 filas (clases + valores límite) con la entrada
representativa, el resultado esperado y el nombre exacto del método de
prueba que lo cubre, más las 5 propiedades jqwik que refuerzan estas
mismas reglas sobre rangos completos en vez de un único ejemplo.

## Justificación de los bordes elegidos

Los valores límite (`-1`/`0`, `17`/`18`, `120`/`121`) se eligieron porque
son los puntos donde el comportamiento del sistema cambia de una clase a
otra — es exactamente donde, según la literatura de pruebas de caja
negra, es más probable que aparezcan errores de "off-by-one". El defecto
documentado en `defectos.md` (edad `-1` aceptada como `VALID` antes de
implementar R4) confirma en la práctica por qué probar los bordes, y no
solo un valor "cómodo" dentro de cada clase, es indispensable.
