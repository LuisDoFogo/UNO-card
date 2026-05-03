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

    public boolean esJugableSobre(Carta otra) {
        if (this.color.equals("Negro")) return true;
        if (this.color.equals(otra.getColor())) return true;
        if (this.tipo.equals("NUMERO") && otra.getTipo().equals("NUMERO")) {
            return this.numero == otra.getNumero();
        }
        return this.tipo.equals(otra.getTipo());
    }

    @Override
    public String toString() {
        String valor = (numero >= 0) ? String.valueOf(numero) : tipo;
        return "[" + color + " " + valor + "]";
    }
}
