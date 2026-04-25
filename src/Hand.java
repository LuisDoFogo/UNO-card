import java.util.ArrayList;

public class Hand {
    private ArrayList<Carta> cartas;

    public Hand() { 
        this.cartas = new ArrayList<>(); 
    }

    public void agregarCarta(Carta c) { 
        if (c != null) cartas.add(c); 
    }

    public Carta quitarDeMano(int i) { 
        return cartas.remove(i); 
    }

    public void mostrarMano() {
        if (cartas.isEmpty()) {
            System.out.println("[ Mano vacía ]");
            return;
        }

        // 1. Imprimir los índices (IDs) para el usuario
        for (int i = 0; i < cartas.size(); i++) {
            System.out.printf("    [%2d]    ", i);
        }
        System.out.println();

        // 2. Dibujar la parte superior
        for (Carta c : cartas) System.out.print(getColorANSI(c) + " ┌─────────┐ " + "\u001B[0m");
        System.out.println();

        // 3. Dibujar el contenido (Valor/Tipo) con centrado mejorado
        for (Carta c : cartas) {
            String valor = (c.getNumero() >= 0) ? String.valueOf(c.getNumero()) : c.getTipo();
            // Limitamos a 7 caracteres para no romper el borde
            if (valor.length() > 7) valor = valor.substring(0, 7); 
            System.out.print(getColorANSI(c) + String.format(" │ %-7s │ ", valor) + "\u001B[0m");
        }
        System.out.println();

        // 4. Dibujar la parte inferior
        for (Carta c : cartas) System.out.print(getColorANSI(c) + " └─────────┘ " + "\u001B[0m");
        System.out.println();
    }

    private String getColorANSI(Carta c) {
        switch (c.getColor()) {
            case "Rojo":     return "\u001B[31m";
            case "Azul":     return "\u001B[34m";
            case "Verde":    return "\u001B[32m";
            case "Amarillo": return "\u001B[33m";
            case "Negro":    return "\u001B[35m"; // Púrpura para que destaque el comodín
            default:         return "\u001B[0m";
        }
    }

    public ArrayList<Carta> getCartas() { return cartas; }
    
    public int getSize() { return cartas.size(); }
    
    public boolean estaVacia() { return cartas.isEmpty(); }
}
