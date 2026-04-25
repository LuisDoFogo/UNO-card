public class main {
    public static void main(String[] args) {
        System.out.println("=== UNO . DOS . CINCO ===");
        // Cambia el 1 por el número de humanos que quieran jugar (1 a 4)
        Game juego = new Game(1); 
        juego.jugar();
    }
}
