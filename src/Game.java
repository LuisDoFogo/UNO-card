import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Deck mazo;
    private List<Carta> descarte;
    private Hand manoJugador, manoBot;
    private boolean turnoDelJugador;
    private Scanner sc;

    public Game() {
        this.mazo = new Deck();
        this.descarte = new ArrayList<>();
        this.manoJugador = new Hand();
        this.manoBot = new Hand();
        this.turnoDelJugador = true;
        this.sc = new Scanner(System.in);
        
        // Repartir cartas iniciales
        for (int i = 0; i < 7; i++) {
            manoJugador.agregarCarta(mazo.robarCarta());
            manoBot.agregarCarta(mazo.robarCarta());
        }
        
        // Carta inicial legal (no negra)
        Carta inicio = mazo.robarCarta();
        while(inicio.getColor().equals("Negro")) {
            mazo.agregarCarta(inicio);
            inicio = mazo.robarCarta();
        }
        descarte.add(inicio);
    }

    // EL MOTOR DEL JUEGO
    public void jugar() {
        while (!manoJugador.estaVacia() && !manoBot.estaVacia()) {
            if (turnoDelJugador) {
                ejecutarTurnoJugador();
            } else {
                ejecutarTurnoBot();
            }
        }
        finalizarPartida();
    }

    private void ejecutarTurnoJugador() {
        Carta mesa = getTopCard();
        System.out.println("\n--- MESA: " + mesa + " | BOT: " + manoBot.getCartas().size() + " cartas ---");
        manoJugador.mostrarMano();
        System.out.print("\nTu turno (Indice, -1 robar): ");

        if (!sc.hasNextInt()) {
            System.out.println("Error: Solo números.");
            sc.next();
            return;
        }

        int idx = sc.nextInt();
        if (idx == -1) {
            robarDeMazo(manoJugador);
            turnoDelJugador = false;
        } else if (idx >= 0 && idx < manoJugador.getCartas().size()) {
            Carta elegida = manoJugador.getCartas().get(idx);
            if (elegida.esJugableSobre(mesa)) {
                procesarJugada(manoJugador, idx, elegida);
            } else {
                System.out.println("Carta no válida.");
            }
        }
    }

    private void ejecutarTurnoBot() {
        System.out.println("\nTurno del Bot...");
        boolean botJugo = false;
        for (int i = 0; i < manoBot.getCartas().size(); i++) {
            Carta cb = manoBot.getCartas().get(i);
            if (cb.esJugableSobre(getTopCard())) {
                procesarJugada(manoBot, i, cb);
                botJugo = true;
                break;
            }
        }
        if (!botJugo) {
            System.out.println("El Bot roba carta.");
            robarDeMazo(manoBot);
            turnoDelJugador = true;
        }
    }

    private void procesarJugada(Hand mano, int idx, Carta carta) {
        descarte.add(mano.quitarDeMano(idx));
        
        // Regla del UNO
        if (mano.getCartas().size() == 1) {
            manejarReglaUno(mano == manoJugador);
        }

        // Comodines
        if (carta.getColor().equals("Negro")) {
            elegirColor(carta, mano == manoJugador);
        }

        // Castigos
        Hand oponente = (mano == manoJugador) ? manoBot : manoJugador;
        aplicarCastigo(carta, oponente);

        // Turno especial
        if (!tieneEfectoEspecial(carta)) {
            turnoDelJugador = (mano != manoJugador);
        } else {
            System.out.println("¡Efecto especial! Repite turno.");
        }
    }

    private void manejarReglaUno(boolean esJugador) {
        if (esJugador) {
            System.out.print("¡UNO! Escribe '1' rápido: ");
            if (!sc.next().equals("1")) {
                System.out.println("¡No dijiste UNO! +2 cartas.");
                robarDeMazo(manoJugador); robarDeMazo(manoJugador);
            }
        } else {
            System.out.println("¡El Bot grita: UNO!");
        }
    }

    private void elegirColor(Carta c, boolean esJugador) {
        if (esJugador) {
            System.out.print("Elige color (1:Rojo, 2:Azul, 3:Verde, 4:Amarillo): ");
            int op = sc.nextInt();
            String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
            c.setColor(colores[Math.max(0, Math.min(op - 1, 3))]);
        } else {
            c.setColor("Verde"); // Inteligencia simple del bot
        }
    }

    private void finalizarPartida() {
        if (manoJugador.estaVacia()) System.out.println("\n¡GANASTE!");
        else System.out.println("\nGANÓ EL BOT.");
    }

    // --- MÉTODOS DE APOYO QUE YA TENÍAS ---
    public boolean tieneEfectoEspecial(Carta c) {
        String t = c.getTipo();
        return t.equals("MAS2") || t.equals("MAS4") || t.equals("BLOQUEO") || t.equals("REVERSA");
    }

    public void aplicarCastigo(Carta carta, Hand oponente) {
        int cant = (carta.getTipo().equals("MAS2")) ? 2 : (carta.getTipo().equals("MAS4")) ? 4 : 0;
        for (int i = 0; i < cant; i++) robarDeMazo(oponente);
        if (cant > 0) System.out.println("¡Castigo de +" + cant + " para el oponente!");
    }

    public void robarDeMazo(Hand mano) {
        Carta c = mazo.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }

    public Carta getTopCard() { return descarte.get(descarte.size() - 1); }
}
