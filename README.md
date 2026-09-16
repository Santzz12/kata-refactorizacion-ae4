# Sistema de gestión de tutorías — Ae3 Incremento 1

Proyecto de **Diseño de Software (UCOM0310)** evolucionado a partir de Ae1 y Ae2 para el **Ae3 – Incremento 1 del proyecto**.

## Propósito

Gestionar tutorías entre estudiantes y docentes manteniendo separadas las reglas del dominio, la persistencia, la creación de objetos, las políticas variables y las reacciones ante eventos.

## Estado inicial recuperado de Ae1

La base del sistema conserva las entidades `Usuario`, `Estudiante`, `Docente`, `HorarioDisponible`, `Reserva` y `EstadoReserva`, además de `ReservaRepository`, `MemoriaReservaRepository`, `Notificador` y `ServicioReservas`.

La lógica principal continúa dependiendo de abstracciones para evitar acoplamiento directo con tecnologías concretas.

## Patrones recuperados de Ae2

### Builder

Se mantiene `ReservaBuilder` porque `Reserva` maneja datos obligatorios y opcionales:

- `estudiante` y `horario` son obligatorios para construir una reserva.
- `id` se genera por defecto, aunque puede definirse explícitamente.
- `tema`, `observaciones` y `enviarRecordatorio` son opcionales.
- El estado inicial permanece controlado como `SOLICITADA`.

**Beneficio:** evita constructores extensos y permite una construcción progresiva y legible.

**Costo/compromiso:** incorpora una clase adicional y varios métodos de configuración.

### Factory Method

Se mantiene Factory Method para crear distintas implementaciones de `Notificador` sin acoplar al cliente a clases concretas.

Participantes principales:

- `NotificacionFactory`
- `EmailFactory`
- `SmsFactory`
- `WhatsAppFactory`
- `PushFactory`
- `NotificadorEmail`
- `NotificadorSms`
- `NotificadorWhatsApp`
- `NotificadorPush`

**Beneficio:** permite agregar canales sin modificar el código que trabaja con `Notificador`.

**Costo/compromiso:** aumenta el número de clases al existir una fábrica y un producto concreto por variante.

## Problemas de diseño identificados para Ae3

### 1. Política de cancelación variable

**Problema real:** la regla para permitir una cancelación puede cambiar de manera independiente del resto del caso de uso. Mantenerla fija dentro de `ServicioReservas` aumenta el acoplamiento entre coordinación y política.

**Contexto:** una política estándar permite cancelar cualquier reserva que no haya finalizado, mientras que una política restrictiva solo permite cancelar reservas en estado `SOLICITADA`.

**Patrón seleccionado:** **Strategy**.

- `PoliticaCancelacion`
- `CancelacionEstandar`
- `CancelacionSoloSolicitada`

**Qué permanece estable:** `ServicioReservas` continúa coordinando la operación de cancelación y `Reserva` conserva las reglas invariantes de su ciclo de vida.

**Beneficio:** la política puede sustituirse en tiempo de ejecución mediante `cambiarPoliticaCancelacion(...)` sin modificar el servicio.

**Costo/compromiso:** se añaden una interfaz y clases de estrategia, y existe una indirección adicional para comprender el flujo de cancelación.

**Verificación:** las pruebas cambian la estrategia de un mismo `ServicioReservas` y comprueban que el resultado de cancelar cambia según la política seleccionada.

### 2. Múltiples componentes reaccionan a cambios de una reserva

**Problema real:** notificaciones, auditoría u otros componentes pueden necesitar enterarse cuando una reserva se solicita, confirma, cancela, reprograma o completa. Si el servicio conoce cada reacción concreta, aumenta el acoplamiento.

**Contexto:** la notificación al usuario y el registro de auditoría deben reaccionar al mismo evento sin formar parte de la regla de negocio de `Reserva`.

**Patrón seleccionado:** **Observer**.

- `ReservaObserver`
- `NotificacionReservaObserver`
- `AuditoriaReservaObserver`

**Qué permanece estable:** `ServicioReservas` publica cambios de reserva sin conocer cómo cada receptor procesa el evento.

**Beneficio:** se pueden agregar nuevos receptores sin modificar la lógica principal del servicio.

**Costo/compromiso:** el flujo deja de ser completamente directo porque una operación puede producir efectos a través de una colección de observers.

**Verificación:** una prueba registra un observer y comprueba que recibe los eventos de solicitud y confirmación.

## Patrones utilizados

| Patrón | Uso en el proyecto |
|---|---|
| Builder | Construcción progresiva de `Reserva` |
| Factory Method | Creación de canales de notificación |
| Strategy | Políticas intercambiables de cancelación |
| Observer | Reacción desacoplada a cambios de reserva |

## Principios SOLID relacionados

### SRP — Single Responsibility Principle

- `Reserva` protege su ciclo de vida.
- `ReservaBuilder` se ocupa de construir reservas.
- Las fábricas se ocupan de crear notificadores.
- Las estrategias encapsulan políticas de cancelación.
- Los observers encapsulan reacciones ante eventos.
- `ServicioReservas` coordina el caso de uso.

### OCP — Open/Closed Principle

Se pueden agregar nuevos canales de notificación, nuevas políticas de cancelación u observers sin reescribir las clases existentes que dependen de las abstracciones.

### DIP — Dependency Inversion Principle

`ServicioReservas` depende de `ReservaRepository`, `PoliticaCancelacion` y `ReservaObserver`, no de implementaciones concretas de persistencia, políticas o receptores.

## Estructura principal

```text
sistema-tutorias/
├── README.md
├── pom.xml
├── docs/
│   ├── modelo-clases.puml
│   └── uml-incremento1.puml
├── .github/workflows/
│   └── ae3-ci.yml
└── src/
    ├── main/java/edu/uees/tutorias/
    │   ├── App.java
    │   ├── builder/
    │   ├── domain/
    │   ├── factory/
    │   ├── notification/
    │   ├── observer/
    │   ├── repository/
    │   ├── service/
    │   └── strategy/
    └── test/java/edu/uees/tutorias/
        └── Ae3PatternsTest.java
```

## Compilar

Requiere JDK 17 y Maven.

```bash
mvn clean compile
```

## Ejecutar pruebas

```bash
mvn clean test
```

Las pruebas verifican:

- valores por defecto de Builder;
- creación del producto esperado mediante Factory Method;
- intercambio de políticas Strategy dentro de `ServicioReservas`;
- publicación de eventos Observer;
- comportamiento de la política restrictiva.

## Ejecutar la demostración

```bash
java -cp target/classes edu.uees.tutorias.App
```

La demostración crea una reserva, utiliza `EmailFactory`, registra observers, confirma la reserva y posteriormente la cancela utilizando `CancelacionEstandar`.

## Verificación automática

El workflow `.github/workflows/ae3-ci.yml` ejecuta `mvn clean test` y posteriormente la demostración de `App` en Java 17 para comprobar el incremento en cada actualización de la rama y del Pull Request.

## UML del incremento

Fuente PlantUML actualizada:

- [`docs/uml-incremento1.puml`](docs/uml-incremento1.puml)

El diagrama incluye las clases del dominio y las relaciones introducidas por Builder, Factory Method, Strategy y Observer.

## Repositorio

https://github.com/Santzz12/Sistema-Tutorias

Rama de desarrollo del Ae3: `ae3-incremento1`.

## Declaración de uso de inteligencia artificial

Durante el desarrollo se utilizaron herramientas de inteligencia artificial como apoyo para revisar la continuidad con Ae1 y Ae2, proponer e implementar la integración de patrones, revisar coherencia entre responsabilidades, código y UML y mejorar la documentación. El contenido fue revisado en relación con la consigna y debe ser ejecutado, comprendido y defendido por el estudiante antes de su entrega.

## Actividad Ae4 — Kata de Refactorización

### Refactorización aplicada sobre `ServicioReservas.java`:
1. **Extract Constants**: Se reemplazaron cadenas mágicas de eventos por constantes públicas descriptivas (`EVENTO_SOLICITADA`, etc.).
2. **Replace Nested Conditional with Guard Clauses**: Se aplanó el flujo de control aplicando retorno temprano (*fail-fast*) y eliminando bloques `else` tras excepciones.
3. **Extract Method**: Se aislaron las responsabilidades de validación en `validarReprogramacion` y el ciclo de persistencia/notificación en `actualizarYNotificar`.

### Comprobación de no regresión:
- Suite de pruebas ejecutada con `mvn clean test`: 5/5 pruebas aprobadas exitosamente antes y después de los cambios.