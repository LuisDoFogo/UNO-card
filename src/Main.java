import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lanzamos la ventana en el hilo de ejecución de la interfaz gráfica (EDT)
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}