import java.util.*;

public class Game {
    private Deck mazo = new Deck();
    private List<Carta> descarte = new ArrayList<>();
    private List<Player> jugadores = new ArrayList<>();
    private TurnManager turnManager;

    // CONSTRUCTOR ADAPTADO PARA LA INTERFAZ GRÁFICA
    public Game(String nombreHumano, String[] nombresBots) {
        // Inicializamos al jugador humano
        Player p = new Player(nombreHumano, true);
        for (int j = 0; j < 7; j++) p.getMano().agregarCarta(mazo.robarCarta());
        jugadores.add(p);

        // Inicializamos a los oponentes artificiales
        for (String nombreBot : nombresBots) {
            Player bot = new Player(nombreBot, false);
            for (int j = 0; j < 7; j++) bot.getMano().agregarCarta(mazo.robarCarta());
            jugadores.add(bot);
        }
        
        turnManager = new TurnManager(jugadores.size());
        Carta inicial = mazo.robarCarta();
        
        // Evitar que la primera carta sea negra para simplificar el inicio
        while(inicial.getColor().equals("Negro")) {
            mazo.agregarCartas(Collections.singletonList(inicial));
            mazo.barajar();
            inicial = mazo.robarCarta();
        }
        descarte.add(inicial);
    }

    public void darCartasAPlayerActual(int cant) {
        Player p = jugadores.get(turnManager.getIndiceActual());
        for (int i = 0; i < cant; i++) robarCarta(p);
    }

    public void robarCarta(Player p) {
        if (mazo.getCartasRestantes() == 0) {
            Carta actual = descarte.remove(descarte.size() - 1);
            mazo.agregarCartas(new ArrayList<>(descarte));
            mazo.barajar();
            descarte.clear();
            descarte.add(actual);
        }
        p.getMano().agregarCarta(mazo.robarCarta());
    }

    public Carta getTopCard() { return descarte.get(descarte.size() - 1); }
    public List<Carta> getDescarte() { return descarte; }

    // GETTERS FUNDAMENTALES PARA QUE MESA_PANEL LEA EL ESTADO DEL JUEGO
    public List<Player> getJugadores() { return jugadores; }
    public TurnManager getTurnManager() { return turnManager; }
    public Deck getMazo() { return mazo; }
}

