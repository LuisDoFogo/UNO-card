# UNO-card
El presente repositorio contiene el desarrollo de un sistema programado en lenguaje Java, organizado bajo una metodología de trabajo por sprints. Para garantizar una correcta gestión de versiones y un control adecuado de los cambios realizados durante el desarrollo, se implementó el uso de Git como sistema de control de versiones.

## 📊 Diagrama UML
![Diagrama UML](docs/uml-UNO-card-diagrama.png)

## Sprint 2

### Cambios realizados

- Se agregó el atributo `tipo` a la clase `Carta`
- Se mejoró la lógica del método `esJugableSobre()`
- Se integraron cartas especiales (ej. +2, comodín, etc.)
- Se actualizó el diagrama UML en la carpeta `docs/`

## Actualizacion de Modelo UML

El diseño del sistema se basa en clases principales:

- `Carta`: representa una carta con color, número y tipo
- `Deck`: maneja el mazo de cartas
- `Hand`: representa la mano de un jugador
- `Game`: controla la lógica del juego
- `Main`: punto de entrada del programa

![Diagrama UML](uml_uno-card_sprint2.png)