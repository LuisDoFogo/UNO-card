import java.util.ArrayList;

public class Hand {
    private ArrayList<Carta> cartas = new ArrayList<>();

    public void agregarCarta(Carta c) { if (c != null) cartas.add(c); }
    public Carta quitarDeMano(int i) { return cartas.remove(i); }
    public ArrayList<Carta> getCartas() { return cartas; }
    public boolean estaVacia() { return cartas.isEmpty(); }

    public void mostrarMano() {
    if (cartas.isEmpty()) return;
    
    int anchoCarta = 11; // El ancho de "┌─────────┐"

    // 1. Imprimir Índices perfectamente centrados
    for (int i = 0; i < cartas.size(); i++) {
        // Creamos un formato que centra el número en el espacio de la carta
        String indice = "[" + i + "]";
        int espaciosPre = (anchoCarta - indice.length()) / 2;
        int espaciosPost = anchoCarta - indice.length() - espaciosPre;
        System.out.print(" ".repeat(espaciosPre) + indice + " ".repeat(espaciosPost) + " ");
    }
    System.out.println();

    // 2. Imprimir Borde Superior
    for (Carta c : cartas) {
        System.out.print(getColorANSI(c) + "┌─────────┐ " + "\u001B[0m");
    }
    System.out.println();

    // 3. Imprimir Contenido (Valor)
    for (Carta c : cartas) {
        String valor = (c.getNumero() >= 0) ? String.valueOf(c.getNumero()) : c.getTipo();
        // Ajustamos el printf para que el contenido sea de 9 espacios (11 menos los bordes)
        System.out.print(getColorANSI(c) + String.format("│ %-7.7s │ ", valor) + "\u001B[0m");
    }
    System.out.println();

    // 4. Imprimir Borde Inferior
    for (Carta c : cartas) {
        System.out.print(getColorANSI(c) + "└─────────┘ " + "\u001B[0m");
    }
    System.out.println();
}

    private String getColorANSI(Carta c) {
        switch (c.getColor()) {
            case "Rojo": return "\u001B[31m";
            case "Azul": return "\u001B[34m";
            case "Verde": return "\u001B[32m";
            case "Amarillo": return "\u001B[33m";
            case "Negro": return "\u001B[35m"; // Púrpura
            default: return "\u001B[0m";
        }
    }
}
