# Escenarios BDD (Given – When – Then)

Cada test unitario tiene su equivalente narrativo, alineando el lenguaje
de negocio con la verificación automatizada. Los mismos textos están en
los `@DisplayName` de `RegistryTest.java`.

| Test (JUnit) | Escenario BDD |
|---|---|
| `shouldReturnInvalidWhenPersonIsNull` | **Given** la persona es `null`; **When** intento registrarla; **Then** el resultado debe ser `INVALID` |
| `shouldRejectWhenIdIsZeroOrNegative` | **Given** la persona tiene `id = 0` (o `id = -5`), edad 25 y está viva; **When** intento registrarla; **Then** el resultado debe ser `INVALID` |
| `shouldRejectDeadPerson` | **Given** la persona no está viva; **When** intento registrarla; **Then** el resultado debe ser `DEAD` |
| `shouldReturnDeadBeforeUnderageWhenBothApply` | **Given** la persona no está viva y además es menor de edad; **When** intento registrarla; **Then** el resultado debe ser `DEAD`, no `UNDERAGE` |
| `shouldRejectInvalidAgeBelowZero` | **Given** la persona está viva, tiene id válido y edad `-1`; **When** intento registrarla; **Then** el resultado debe ser `INVALID_AGE` |
| `shouldRejectInvalidAgeOver120` | **Given** la persona está viva, tiene id válido y edad `121`; **When** intento registrarla; **Then** el resultado debe ser `INVALID_AGE` |
| `shouldRejectUnderageAt17` | **Given** la persona tiene 17 años, está viva y su id es válido; **When** intento registrarla; **Then** el resultado debe ser `UNDERAGE` |
| `shouldAcceptAdultAt18` | **Given** la persona tiene 18 años, está viva y su id es válido; **When** intento registrarla; **Then** el resultado debe ser `VALID` |
| `shouldAcceptMaxAge120` | **Given** la persona tiene 120 años, está viva y su id es válido; **When** intento registrarla; **Then** el resultado debe ser `VALID` |
| `shouldRejectDuplicatedId` | **Given** ya existe una persona registrada con ese `id`; **When** intento registrar a otra persona con el mismo `id`; **Then** el resultado debe ser `DUPLICATED` |
| `shouldRegisterValidPerson` | **Given** una persona viva, mayor de edad, con id único; **When** intento registrarla; **Then** el resultado debe ser `VALID` |

> Regla general del taller: todos los escenarios BDD se enfocan
> exclusivamente en el **dominio** (`Registry`, `Person`), sin mencionar
> frameworks, HTTP ni base de datos — coherente con Clean Architecture.
