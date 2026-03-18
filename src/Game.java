import java.util.ArrayList;
import java.util.List;

public class Game {
    private Deck mazo;
    private List<Carta> descarte;
    private Hand manoJugador, manoBot;

    public Game() {
        this.mazo = new Deck();
        this.descarte = new ArrayList<>();
        this.manoJugador = new Hand();
        this.manoBot = new Hand();
        for (int i = 0; i < 7; i++) {
            manoJugador.agregarCarta(mazo.robarCarta());
            manoBot.agregarCarta(mazo.robarCarta());
        }
        Carta inicio = mazo.robarCarta();
        while(inicio.getColor().equals("Negro")) {
            mazo.agregarCarta(inicio);
            inicio = mazo.robarCarta();
        }
        descarte.add(inicio);
    }

    public boolean tieneEfectoEspecial(Carta c) {
        // En el UNO, Cambio de Color no salta turno, solo los de ataque o bloqueo
        String t = c.getTipo();
        return t.equals("MAS2") || t.equals("MAS4") || t.equals("BLOQUEO") || t.equals("REVERSA");
    }

    public void aplicarCastigo(Carta carta, Hand oponente) {
        int cant = 0;
        if (carta.getTipo().equals("MAS2")) cant = 2;
        if (carta.getTipo().equals("MAS4")) cant = 4;
        
        for (int i = 0; i < cant; i++) {
            Carta c = mazo.robarCarta();
            if (c != null) oponente.agregarCarta(c);
        }
        if (cant > 0) System.out.println("¡Se han sumado " + cant + " cartas al oponente!");
    }

    public Carta getTopCard() { return descarte.get(descarte.size() - 1); }
    public Hand getManoJugador() { return manoJugador; }
    public Hand getManoBot() { return manoBot; }
    
    public void jugarCarta(Hand mano, int i) { 
        descarte.add(mano.quitarDeMano(i)); 
    }
    
    public void robarDeMazo(Hand mano) { 
        Carta c = mazo.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }
}
