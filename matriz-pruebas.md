# Matriz de pruebas — `registerVoter(Person)`

## Clases de equivalencia y valores límite

| # | Atributo / clase | Entrada representativa | Resultado esperado | Test que lo cubre |
|---|---|---|---|---|
| 1 | Persona nula | `person = null` | `INVALID` | `shouldReturnInvalidWhenPersonIsNull` |
| 2 | Id inválido (borde `0`) | `id = 0`, edad 25, vivo | `INVALID` | `shouldRejectWhenIdIsZeroOrNegative[id=0]` |
| 3 | Id inválido (negativo) | `id = -5`, edad 25, vivo | `INVALID` | `shouldRejectWhenIdIsZeroOrNegative[id=-5]` |
| 4 | Id válido y único | `id = 1`, edad 30, vivo | `VALID` | `shouldRegisterValidPerson` |
| 5 | Id duplicado | mismo `id` registrado dos veces | `DUPLICATED` | `shouldRejectDuplicatedId` |
| 6 | No vivo | `alive = false`, edad 40 | `DEAD` | `shouldRejectDeadPerson` |
| 7 | No vivo + menor de edad (prioridad R3 sobre R5) | `alive = false`, edad 15 | `DEAD` | `shouldReturnDeadBeforeUnderageWhenBothApply` |
| 8 | Edad inválida (borde inferior `-1`) | `edad = -1`, vivo, id válido | `INVALID_AGE` | `shouldRejectInvalidAgeBelowZero` |
| 9 | Edad inválida (borde superior `121`) | `edad = 121`, vivo, id válido | `INVALID_AGE` | `shouldRejectInvalidAgeOver120` |
| 10 | Menor de edad (borde `17`) | `edad = 17`, vivo, id válido | `UNDERAGE` | `shouldRejectUnderageAt17` |
| 11 | Adulto (borde inferior `18`) | `edad = 18`, vivo, id válido | `VALID` | `shouldAcceptAdultAt18` |
| 12 | Adulto (borde superior `120`) | `edad = 120`, vivo, id válido | `VALID` | `shouldAcceptMaxAge120` |

## Propiedades (jqwik) que refuerzan estas clases sobre rangos completos

| Propiedad | Rango explorado | Qué garantiza |
|---|---|---|
| `unaPersonaNoVivaSiempreEsRechazada` | id 1–100000, edad 0–120, todos los géneros | Regla R3 sobre **todo** el rango, no solo un ejemplo |
| `todoMenorDeEdadEsRechazado` | edad 0–17 | Regla R5 sobre todo el rango de menores |
| `todoAdultoValidoSeRegistra` | edad 18–120 | Regla R7 sobre todo el rango de adultos válidos |
| `registerVoterNuncaDevuelveNullNiLanzaExcepcion` | id -10–100000, edad -10–130 | Propiedad estructural de **totalidad** |
| `mismaEntradaProduceMismoResultado` | id 1–100000, edad -5–130 | Propiedad estructural de **determinismo** |

> Nota: las clases #2 y #3 corresponden a un único método parametrizado (`@ParameterizedTest`) que cubre ambos valores límite del borde de `id`.
