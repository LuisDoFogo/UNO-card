import java.util.Scanner;

public class Player {
    private String nombre;
    private Hand mano = new Hand();
    private boolean esHumano;
    private boolean gritoUno = false;

    public Player(String nombre, boolean esHumano) {
        this.nombre = nombre;
        this.esHumano = esHumano;
    }

    public int decidirJugada(Carta mesa, Scanner sc) {
        if (esHumano) {
            System.out.print("--> " + nombre + ", elige índice o -1 para robar: ");
            while (!sc.hasNextInt()) {
                System.out.println(" Opción inválida. Ingresa un número.");
                sc.next();
            }
            return sc.nextInt();
        } else {
            // IA Estratégica: Elige el color que más tiene
            int mejorIdx = -1;
            int maxColor = -1;
            for (int i = 0; i < mano.getCartas().size(); i++) {
                Carta c = mano.getCartas().get(i);
                if (RuleEngine.esJugadaValida(c, mesa)) {
                    int cuenta = 0;
                    for (Carta a : mano.getCartas()) if (a.getColor().equals(c.getColor())) cuenta++;
                    if (cuenta > maxColor) { maxColor = cuenta; mejorIdx = i; }
                }
            }
            return mejorIdx;
        }
    }

    public void verificarUno(Scanner sc) {
        if (esHumano) {
            System.out.println(" ¡ULTIMA CARTA! ¿Gritar UNO? (1: SÍ / 2: NO)");
            while (!sc.hasNextInt()) { sc.next(); }
            gritoUno = (sc.nextInt() == 1);
        } else {
            gritoUno = true;
            System.out.println(" " + nombre + " grita: ¡UNO!");
        }
    }

    public String elegirColor(Scanner sc) {
        String[] col = {"Rojo", "Azul", "Verde", "Amarillo"};
        if (!esHumano) return col[(int)(Math.random()*4)];
        System.out.println("Elige color (1:Rojo, 2:Azul, 3:Verde, 4:Amarillo):");
        while (!sc.hasNextInt()) { sc.next(); }
        return col[Math.max(0, Math.min(sc.nextInt() - 1, 3))];
    }

    public boolean haGritadoUno() { return gritoUno; }
    public String getNombre() { return nombre; }
    public Hand getMano() { return mano; }
    public boolean esHumano() { return esHumano; }
}
