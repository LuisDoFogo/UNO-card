import java.util.ArrayList;

public class Hand {
    private ArrayList<Carta> cartas;

    public Hand() {
        cartas = new ArrayList<>();
    }

    public void agregarCarta(Carta c) {
        cartas.add(c);
    }

    public Carta jugarCarta(int i) {
        return cartas.remove(i);
    }

    public void mostrarMano() {
        for (int i = 0; i < cartas.size(); i++) {
            System.out.print(i + ":" + cartas.get(i) + " ");
        }
        System.out.println();
    }

    public ArrayList<Carta> getCartas() {
        return cartas;
    }

    public boolean estaVacia() {
        return cartas.isEmpty();
    }
}
