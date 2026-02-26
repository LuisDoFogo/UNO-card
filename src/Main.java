import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Game juego = new Game();
        Scanner sc = new Scanner(System.in);
        boolean juegoTerminado = false;

        System.out.println("¡Bienvenido a UNO !");

        while (!juegoTerminado) {
            Carta mesa = juego.getTopCard();
            System.out.println("\n------------------------------");
            System.out.println("MESA: " + mesa);
            System.out.println("Tu mano:");
            juego.getManoJugador().mostrarMano();
            System.out.println("Cartas del Bot: " + juego.getManoBot().getCartas().size());
            
            // --- TURNO JUGADOR ---
            boolean turnoValido = false;
            while (!turnoValido) {
                System.out.print("Elige índice de carta (o -1 para robar): ");
                int eleccion = sc.nextInt();

                if (eleccion == -1) {
                    juego.robarDeMazo(juego.getManoJugador());
                    System.out.println("Has robado una carta.");
                    turnoValido = true;
                } else if (eleccion >= 0 && eleccion < juego.getManoJugador().getCartas().size()) {
                    Carta elegida = juego.getManoJugador().getCartas().get(eleccion);
                    if (elegida.esJugableSobre(mesa)) {
                        juego.jugarCarta(juego.getManoJugador(), eleccion);
                        turnoValido = true;
                    } else {
                        System.out.println("¡No puedes jugar esa carta!");
                    }
                }
            }

            if (juego.getManoJugador().estaVacia()) {
                System.out.println("¡FELICIDADES! Ganaste.");
                juegoTerminado = true;
                break;
            }

            // --- TURNO BOT ---
            System.out.println("\nTurno de la máquina...");
            boolean botJugo = false;
            for (int i = 0; i < juego.getManoBot().getCartas().size(); i++) {
                if (juego.getManoBot().getCartas().get(i).esJugableSobre(juego.getTopCard())) {
                    System.out.println("===== La máquina jugó =====: " + juego.getManoBot().getCartas().get(i));
                    juego.jugarCarta(juego.getManoBot(), i);
                    botJugo = true;
                    break;
                }
            }

            if (!botJugo) {
                System.out.println("La máquina no tiene cartas y roba.");
                juego.robarDeMazo(juego.getManoBot());
            }

            if (juego.getManoBot().estaVacia()) {
                System.out.println("La máquina ha ganado. Mejor suerte la próxima.");
                juegoTerminado = true;
            }
        }
        sc.close();
    }
}
