import java.util.ArrayList;
import java.util.List;

public class Game {
    private Deck mazo;
    private List<Carta> descarte;
    private Hand manoJugador;
    private Hand manoBot;

    public Game() {
        this.mazo = new Deck();
        this.descarte = new ArrayList<>();
        this.manoJugador = new Hand();
        this.manoBot = new Hand();
        
        // Repartir 7 cartas
        for (int i = 0; i < 7; i++) {
            manoJugador.agregarCarta(mazo.robarCarta());
            manoBot.agregarCarta(mazo.robarCarta());
        }
        // Primera carta a la mesa
        descarte.add(mazo.robarCarta());
    }

    public Carta getTopCard() {
        return descarte.get(descarte.size() - 1);
    }

    public Hand getManoJugador() { return manoJugador; }
    public Hand getManoBot() { return manoBot; }

    public void jugarCarta(Hand mano, int indice) {
        descarte.add(mano.jugarCarta(indice));
    }

    public void robarDeMazo(Hand mano) {
        Carta c = mazo.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }
}



