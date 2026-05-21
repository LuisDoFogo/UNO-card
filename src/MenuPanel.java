import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import javax.swing.*;

public class MenuPanel extends JPanel {
    private VentanaPrincipal ventana;
    private Image encabezado;
    private JButton hoveredButton = null;

    public MenuPanel(VentanaPrincipal ventana) {
        this.ventana = ventana;
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Iniciar la música de fondo del menú principal
        GestorSonido.getInstance().iniciarMusica(true);

        // Carga de la imagen de encabezado si existe
        File imgFile = new File("encabezado.jpg");
        if (imgFile.exists()) {
            encabezado = new ImageIcon("encabezado.jpg").getImage();
        }

        // Espaciado superior para que el texto y botones no queden amontonados
        add(Box.createVerticalStrut(250)); 

        JButton btn1Bot = crearBotonNavegacion("VS 1 JUGADOR", Color.GREEN);
        btn1Bot.addActionListener(e -> pedirNombres(1));
        
        JButton btn2Bots = crearBotonNavegacion("VS 2 JUGADORES", Color.GREEN);
        btn2Bots.addActionListener(e -> pedirNombres(2));

        JButton btn3Bots = crearBotonNavegacion("VS 3 JUGADORES", Color.GREEN);
        btn3Bots.addActionListener(e -> pedirNombres(3));

        JButton btnSalir = crearBotonNavegacion("SALIR", Color.RED);
        btnSalir.addActionListener(e -> {
            GestorSonido.getInstance().pararMusica();
            System.exit(0);
        });

        MouseMotionAdapter mma = new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                hoveredButton = (JButton) e.getSource();
                repaint();
            }
        };

        for (JButton btn : new JButton[]{btn1Bot, btn2Bots, btn3Bots, btnSalir}) {
            btn.addMouseMotionListener(mma);
            add(btn);
            add(Box.createVerticalStrut(20));
        }
    }

    private JButton crearBotonNavegacion(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setForeground(color);
        btn.setBackground(Color.BLACK);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setFont(new Font("Monospaced", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();

        // 1. Dibujar el Omnitrix (Encabezado)
        if (encabezado != null) {
            int imgW = 150; 
            int imgH = 150;
            g2d.drawImage(encabezado, (w - imgW) / 2, 40, imgW, imgH, this);
        }

        // 2. Configuración del Título "[ UNO ]"
        String t1 = "[ UNO ]";
        g2d.setFont(new Font("Monospaced", Font.BOLD, 45));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (w - fm.stringWidth(t1)) / 2;
        int y = 110;

        // --- MARGEN BLANCO (Efecto de contorno) ---
        g2d.setColor(Color.WHITE);
        g2d.drawString(t1, x - 2, y - 2);
        g2d.drawString(t1, x + 2, y - 2);
        g2d.drawString(t1, x - 2, y + 2);
        g2d.drawString(t1, x + 2, y + 2);

        // --- TEXTO VERDE ENCIMA ---
        g2d.setColor(Color.GREEN);
        g2d.drawString(t1, x, y);
        
        // 3. Subtítulo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        String t2 = "EDICIÓN BEN 10";
        g2d.drawString(t2, (w - g2d.getFontMetrics().stringWidth(t2)) / 2, 140);

        // 4. Flecha de selección
        if (hoveredButton != null) {
            g2d.setColor(Color.GREEN);
            int arrowX = hoveredButton.getX() - 40;
            int arrowY = hoveredButton.getY() + (hoveredButton.getHeight() / 2);
            g2d.fillPolygon(new int[]{arrowX, arrowX + 20, arrowX}, new int[]{arrowY - 10, arrowY, arrowY + 10}, 3);
        }
    }

    private void pedirNombres(int numBots) {
        String nombreJugador = JOptionPane.showInputDialog(this, "Ingresa tu nombre:");
        if (nombreJugador == null || nombreJugador.trim().isEmpty()) nombreJugador = "Tú";

        String[] nombresBots = new String[numBots];
        for (int i = 0; i < numBots; i++) {
            String n = JOptionPane.showInputDialog(this, "Nombre para el Bot " + (i + 1) + ":");
            nombresBots[i] = (n == null || n.trim().isEmpty()) ? "Bot " + (i + 1) : n;
        }
        ventana.cambiarAMesa(nombreJugador, nombresBots);
    }
}