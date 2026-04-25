import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<Carta> cartas;
    
    public Deck() {
        this.cartas = new ArrayList<>();
        inicializarMazo();
        barajar();
    }

    private void inicializarMazo() {
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        for (String c : colores) {
            // Cartas Numéricas
            for (int i = 0; i <= 9; i++) {
                cartas.add(new Carta(c, i, "NUMERO"));
                // El 0 es la única carta que no se repite por color
                if (i != 0) cartas.add(new Carta(c, i, "NUMERO"));
            }
            // Cartas Especiales (2 de cada una por color)
            for (int j = 0; j < 2; j++) {
                cartas.add(new Carta(c, -1, "REVERSA"));
                cartas.add(new Carta(c, -1, "BLOQUEO"));
                cartas.add(new Carta(c, -1, "MAS2"));
            }
        }
        // Comodines Negros (4 de cada uno)
        for (int i = 0; i < 4; i++) {
            cartas.add(new Carta("Negro", -1, "CAMBIO_COLOR"));
            cartas.add(new Carta("Negro", -1, "MAS4"));
        }
    }
    
    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta robarCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }

    // Nuevo: Necesario para que Game sepa cuándo rellenar el mazo
    public int getCartasRestantes() {
        return cartas.size();
    }

    // Nuevo: Para meter las cartas del descarte de golpe
    public void agregarCartas(List<Carta> nuevasCartas) {
        cartas.addAll(nuevasCartas);
    }

    public void agregarCarta(Carta c) {
        cartas.add(c);
    }
}
