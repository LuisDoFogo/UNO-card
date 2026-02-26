public class Carta {
    private final String color; 
    private final int numero;   

    public Carta(String color, int numero) {
        this.color = color;
        this.numero = numero;
    }

    public String getColor() { return color; }
    public int getNumero() { return numero; }

    @Override
    public String toString() {
        return "[" + color + " " + numero + "]";
    }

    // Método lógico para verificar si se puede tirar sobre otra
    public boolean esJugableSobre(Carta otra) {
        return this.color.equals(otra.getColor()) || this.numero == otra.getNumero();
    }
}
