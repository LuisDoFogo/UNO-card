import java.util.*;

public class Deck {
    private ArrayList<Carta> cartas = new ArrayList<>();

    public Deck() {
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        for (String c : colores) {
            for (int i = 0; i <= 9; i++) {
                cartas.add(new Carta(c, i, "NUMERO"));
                if (i != 0) cartas.add(new Carta(c, i, "NUMERO"));
            }
            for (int j = 0; j < 2; j++) {
                cartas.add(new Carta(c, -1, "REVERSA"));
                cartas.add(new Carta(c, -1, "BLOQUEO"));
                cartas.add(new Carta(c, -1, "MAS2"));
            }
        }
        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta("Negro", -1, "CAMBIO_COLOR"));
            cartas.add(new Carta("Negro", -1, "MAS4"));
        }
        barajar();
    }

    public void barajar() { Collections.shuffle(cartas); }
    public Carta robarCarta() { return cartas.isEmpty() ? null : cartas.remove(0); }
    public int getCartasRestantes() { return cartas.size(); }
    public void agregarCartas(List<Carta> nuevas) { cartas.addAll(nuevas); }
}
