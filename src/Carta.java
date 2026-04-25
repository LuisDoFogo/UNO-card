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
        // 1. Los comodines negros en la mano siempre son jugables
        if (this.color.equals("Negro")) return true;

        // 2. Similitud por color
        if (this.color.equals(otra.getColor())) return true;

        // 3. Similitud por número (solo si ambas son numéricas)
        if (this.tipo.equals("NUMERO") && otra.getTipo().equals("NUMERO")) {
            if (this.numero == otra.getNumero()) return true;
        }

        // 4. Similitud por tipo de carta especial (SALTO sobre SALTO, MAS2 sobre MAS2, etc.)
        // Nota: Agregamos !tipo.equals("NUMERO") para no confundir con el caso anterior
        if (!this.tipo.equals("NUMERO") && this.tipo.equals(otra.getTipo())) {
            return true;
        }

        return false;
    }
}
