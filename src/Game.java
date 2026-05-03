import java.util.*;

public class Game {
    private Deck mazo = new Deck();
    private List<Carta> descarte = new ArrayList<>();
    private List<Player> jugadores = new ArrayList<>();
    private TurnManager turnManager;
    private Scanner sc = new Scanner(System.in);

    public Game(int numHumanos) {
        System.out.print("¿Cuántos jugadores totales habrá? (2-4): ");
        int total = Math.max(2, Math.min(sc.nextInt(), 4));
        sc.nextLine(); // Limpiar buffer

        for (int i = 1; i <= total; i++) {
            String nombre;
            boolean esHumano = (i <= numHumanos);
            
            if (esHumano) {
                System.out.print("Nombre para Jugador " + i + ": ");
                nombre = sc.nextLine();
            } else {
                nombre = "Bot " + i;
            }

            Player p = new Player(nombre, esHumano);
            for (int j = 0; j < 7; j++) p.getMano().agregarCarta(mazo.robarCarta());
            jugadores.add(p);
        }
        
        turnManager = new TurnManager(total);
        Carta inicial = mazo.robarCarta();
        // Evitar que la primera carta sea negra para simplificar el inicio
        while(inicial.getColor().equals("Negro")) {
            mazo.agregarCartas(Collections.singletonList(inicial));
            mazo.barajar();
            inicial = mazo.robarCarta();
        }
        descarte.add(inicial);
    }

    public void jugar() {
        while (true) {
            Player p = jugadores.get(turnManager.getIndiceActual());
            System.out.println("\n--- Turno de: " + p.getNombre() + " | Mesa: " + getTopCard() + " ---");
            if (p.esHumano()) p.getMano().mostrarMano();

            int idx = p.decidirJugada(getTopCard(), sc);

            if (idx == -1) {
                robarCarta(p);
                turnManager.avanzar();
            } else if (idx >= 0 && idx < p.getMano().getCartas().size()) {
                Carta c = p.getMano().getCartas().get(idx);
                if (RuleEngine.esJugadaValida(c, getTopCard())) {
                    descarte.add(p.getMano().quitarDeMano(idx));
                    if (c.getColor().equals("Negro")) c.setColor(p.elegirColor(sc));
                    
                    if (p.getMano().estaVacia()) { System.out.println("==== " + p.getNombre() + " GANA!"); break; }
                    
                    // REQUERIMIENTO: Penalización UNO
                    if (p.getMano().getCartas().size() == 1) {
                        p.verificarUno(sc);
                        if (!p.haGritadoUno()) {
                            System.out.println("⚠️ " + p.getNombre() + " no gritó UNO. Penado con 2 cartas.");
                            darCartasAPlayerActual(2);
                        }
                    }
                    
                    RuleEngine.aplicarEfecto(c, this, turnManager);
                    turnManager.avanzar();
                }
            }
        }
    }

    public void darCartasAPlayerActual(int cant) {
        Player p = jugadores.get(turnManager.getIndiceActual());
        for (int i = 0; i < cant; i++) robarCarta(p);
    }

    // REQUERIMIENTO: Lógica de reabastecimiento
    private void robarCarta(Player p) {
        if (mazo.getCartasRestantes() == 0) {
            System.out.println("===== Rebarajando el mazo desde el descarte...");
            Carta actual = descarte.remove(descarte.size() - 1);
            mazo.agregarCartas(new ArrayList<>(descarte));
            mazo.barajar();
            descarte.clear();
            descarte.add(actual);
        }
        p.getMano().agregarCarta(mazo.robarCarta());
    }

    public Carta getTopCard() { return descarte.get(descarte.size() - 1); }
}
