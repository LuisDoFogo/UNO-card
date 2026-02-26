import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Carta> cartas;
    
    public Deck() {
        cartas = new ArrayList<>();
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        for (String color : colores) {
            for (int i = 0; i <= 9; i++) {
                cartas.add(new Carta(color, i));
                cartas.add(new Carta(color, i)); 
            }
        }
        Collections.shuffle(cartas);
    }
    
    public Carta robarCarta() {
        if (cartas.isEmpty()) return null;
        return cartas.remove(0);
    }
}
