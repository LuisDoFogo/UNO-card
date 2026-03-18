public class Carta {
    private String color; 
    private final int numero;   
    private final String tipo; 

    public Carta(String color, int numero, String tipo) {
        this.color = color;
        this.numero = numero;
        this.tipo = tipo;
    }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; } 
    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }

    @Override
    public String toString() {
        String valor = (numero >= 0) ? String.valueOf(numero) : tipo;
        return "[" + color + " " + valor + "]";
    }

    public boolean esJugableSobre(Carta otra) {
        // Comodines negros se pueden tirar siempre
        if (this.color.equals("Negro")) return true;
        // Mismo color
        if (this.color.equals(otra.getColor())) return true;
        // Mismo número
        if (this.tipo.equals("NUMERO") && otra.getTipo().equals("NUMERO")) {
            if (this.numero == otra.getNumero()) return true;
        }
        // Mismo tipo (ej. MAS2 sobre MAS2)
        if (!this.tipo.equals("NUMERO") && this.tipo.equals(otra.getTipo())) return true;

        return false;
    }
}

