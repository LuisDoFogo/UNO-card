import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Deck mazo;
    private List<Carta> descarte;
    private List<Player> jugadores;
    private int indiceTurno;
    private boolean sentidoHorario;
    private Scanner sc;

    public Game(int numHumanos) {
        this.mazo = new Deck();
        this.descarte = new ArrayList<>();
        this.jugadores = new ArrayList<>();
        this.sc = new Scanner(System.in);
        this.indiceTurno = 0;
        this.sentidoHorario = true;

        for (int i = 1; i <= 4; i++) {
            boolean esHumano = (i <= numHumanos);
            String nombre = esHumano ? "Jugador " + i : "Bot " + i;
            Player p = new Player(nombre, esHumano);
            for (int j = 0; j < 7; j++) p.getMano().agregarCarta(mazo.robarCarta());
            jugadores.add(p);
        }

        Carta inicio = mazo.robarCarta();
        while(inicio.getColor().equals("Negro")) {
            mazo.agregarCarta(inicio);
            mazo.barajar();
            inicio = mazo.robarCarta();
        }
        descarte.add(inicio);
    }

    public void jugar() {
        System.out.println("--- ¡COMIENZA EL JUEGO! ---");
        while (!hayGanador()) {
            Player pActual = jugadores.get(indiceTurno);
            ejecutarTurno(pActual);
        }
        System.out.println("\n ¡PARTIDA FINALIZADA! ");
    }

    private void ejecutarTurno(Player p) {
        Carta mesa = getTopCard();
        System.out.println("\n========================================");
        System.out.println("TURNO DE: " + p.getNombre() + " | Mesa: " + mesa);
        
        if (p.esHumano()) p.getMano().mostrarMano();

        int idx = p.decidirJugada(mesa, sc);

        if (idx == -1) {
            System.out.println(p.getNombre() + " roba una carta.");
            robarDeMazo(p.getMano());
            avanzarTurno(1);
        } else {
            Carta elegida = p.getMano().getCartas().get(idx);
            if (elegida.esJugableSobre(mesa)) {
                procesarJugada(p, idx, elegida);
            } else {
                if (p.esHumano()) {
                    System.out.println("====XX Movimiento inválido XX====. Intenta de nuevo.");
                    ejecutarTurno(p); 
                }
            }
        }
    }

    private void procesarJugada(Player p, int idx, Carta carta) {
        p.getMano().quitarDeMano(idx);
        descarte.add(carta);
        System.out.println(p.getNombre() + " jugó: " + carta);

        // REGLA DEL UNO: Se activa si después de tirar le queda 1 carta
        if (p.getMano().getCartas().size() == 1) {
            manejarReglaUno(p);
        }

        if (carta.getColor().equals("Negro")) {
            String nuevoColor = p.elegirNuevoColor(sc);
            carta.setColor(nuevoColor);
            System.out.println("El color cambió a: " + nuevoColor);
        }

        aplicarEfectos(carta);
    }

    private void manejarReglaUno(Player p) {
        if (p.esHumano()) {
            System.out.print("¡ÚLTIMA CARTA! Presiona '1' rápido para gritar UNO: ");
            String entrada = sc.next();
            if (!entrada.equals("1")) {
                System.out.println("¡No dijiste UNO! +2 cartas de castigo.");
                robarDeMazo(p.getMano());
                robarDeMazo(p.getMano());
            } else {
                System.out.println("¡" + p.getNombre() + " grita: UNOOOOO! ");
            }
        } else {
            // Los bots siempre dicen UNO automáticamente
            System.out.println("¡" + p.getNombre() + " grita: UNOOOOO! ");
        }
    }

    private void aplicarEfectos(Carta c) {
        String tipo = c.getTipo();
        switch (tipo) {
            case "REVERSA":
                sentidoHorario = !sentidoHorario;
                System.out.println("¡Sentido cambiado!");
                avanzarTurno(1); 
                break;
            case "BLOQUEO":
                System.out.println("¡Salto de turno!");
                avanzarTurno(2); 
                break;
            case "MAS2":
                avanzarTurno(1);
                System.out.println(jugadores.get(indiceTurno).getNombre() + " roba 2 y pierde turno.");
                robarDeMazo(jugadores.get(indiceTurno).getMano());
                robarDeMazo(jugadores.get(indiceTurno).getMano());
                avanzarTurno(1);
                break;
            case "MAS4":
                avanzarTurno(1);
                System.out.println(jugadores.get(indiceTurno).getNombre() + " roba 4 y pierde turno.");
                for(int i=0; i<4; i++) robarDeMazo(jugadores.get(indiceTurno).getMano());
                avanzarTurno(1);
                break;
            default:
                avanzarTurno(1);
                break;
        }
    }

    private void avanzarTurno(int pasos) {
        int n = jugadores.size();
        int sentido = sentidoHorario ? 1 : -1;
        // Fórmula: (posicionActual + (pasos * direccion) + compensacionParaNoNegativos) % total
        indiceTurno = (indiceTurno + (pasos * sentido) + (n * pasos)) % n;
    }

    private boolean hayGanador() {
        for (Player p : jugadores) {
            if (p.getMano().estaVacia()) {
                System.out.println("\n " + p.getNombre().toUpperCase() + " HA GANADO!");
                return true;
            }
        }
        return false;
    }

    public void robarDeMazo(Hand mano) {
        if (mazo.getCartasRestantes() == 0) reabastecerMazo();
        Carta c = mazo.robarCarta();
        if (c != null) mano.agregarCarta(c);
    }

    private void reabastecerMazo() {
        if (descarte.size() <= 1) return;
        Carta actual = descarte.remove(descarte.size() - 1);
        mazo.agregarCartas(descarte);
        descarte.clear();
        descarte.add(actual);
        mazo.barajar();
    }

    public Carta getTopCard() { return descarte.get(descarte.size() - 1); }
}
