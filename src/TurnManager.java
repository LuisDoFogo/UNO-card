public class TurnManager {
    private int indiceActual = 0;
    private boolean sentidoHorario = true;
    private final int total;

    public TurnManager(int total) { this.total = total; }
    public int getIndiceActual() { return indiceActual; }
    public void cambiarDireccion() { sentidoHorario = !sentidoHorario; }
    public void avanzar() {
        int paso = sentidoHorario ? 1 : -1;
        indiceActual = (indiceActual + paso + total) % total;
    }
}
