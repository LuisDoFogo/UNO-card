public class RuleEngine {
    public static boolean esJugadaValida(Carta jugada, Carta mesa) {
        return jugada.esJugableSobre(mesa);
    }

    public static void aplicarEfecto(Carta carta, Game game, TurnManager turnManager) {
        switch (carta.getTipo()) {
            case "REVERSA": turnManager.cambiarDireccion(); break;
            case "BLOQUEO": turnManager.avanzar(); break;
            case "MAS2": turnManager.avanzar(); game.darCartasAPlayerActual(2); break;
            case "MAS4": turnManager.avanzar(); game.darCartasAPlayerActual(4); break;
        }
    }
}
