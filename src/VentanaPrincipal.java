import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("UNO - Ben 10 Edition");
        setSize(1000, 700); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        // Cargamos y mostramos directamente el menú inicial
        MenuPanel menu = new MenuPanel(this);
        setContentPane(menu);
    }

    public void cambiarAMesa(String nombreHumano, String[] nombresBots) {
        try {
            // 1. Inicializamos la lógica del juego
            Game game = new Game(nombreHumano, nombresBots);
            
            // 2. Inicializamos los gráficos de la mesa
            MesaPanel mesa = new MesaPanel(game);
            
            // 3. CAMBIO DE PANTALLA FORZADO (Método infalible)
            setContentPane(mesa);
            revalidate();
            repaint();
            
        } catch (Exception e) {
            // Si algo falla, ahora sí te lo gritará en una ventana emergente
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error crítico al intentar crear la mesa:\n" + e.getMessage(), 
                "Error en la Matriz", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}