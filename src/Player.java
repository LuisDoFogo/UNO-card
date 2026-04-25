import java.util.Scanner;

public class Player {
    private String nombre;
    private Hand mano;
    private boolean esHumano;

    public Player(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.esHumano = esHumano;
        this.mano = new Hand();
    }

    public String getNombre() { return nombre; }
    public Hand getMano() { return mano; }
    public boolean esHumano() { return esHumano; }

    // El jugador decide qué índice de carta jugar
    public int decidirJugada(Carta mesa, Scanner sc) {
        if (esHumano) {
            System.out.print("[" + nombre + "] Elige índice (-1 para robar): ");
            while (!sc.hasNextInt()) {
                System.out.print("Entrada inválida. Elige un número: ");
                sc.next();
            }
            return sc.nextInt();
        } else {
            // Lógica de IA simple: Juega la primera que encuentre
            for (int i = 0; i < mano.getCartas().size(); i++) {
                if (mano.getCartas().get(i).esJugableSobre(mesa)) {
                    return i;
                }
            }
            return -1; // Robar si no tiene nada
        }
    }

    // El bot elige color aleatorio, el humano por consola
    public String elegirNuevoColor(Scanner sc) {
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        if (esHumano) {
            System.out.println("Elige color (1:Rojo, 2:Azul, 3:Verde, 4:Amarillo): ");
            int op = sc.nextInt();
            return colores[Math.max(0, Math.min(op - 1, 3))];
        }
        return colores[(int)(Math.random() * 4)];
    }
}

