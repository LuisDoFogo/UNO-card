import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MesaPanel extends JPanel {
    private Game game;
    private Image reversoImg;
    private Image reversoNorte;
    private Image reversoOeste;
    private Image reversoEste;
    private Image btnColorImg; 

    private JTextArea txtHistorial;
    private boolean unoPresionado = false;
    private boolean sentidoHorario = true;
    private Carta ultimaCartaTop = null;
    private List<String> historialJugadas = new ArrayList<>();

    public MesaPanel(Game game) {
        this.game = game;
        setBackground(new Color(5, 10, 5)); 
        setLayout(null); 

        // Iniciar la música de fondo de la mesa de juego
        GestorSonido.getInstance().iniciarMusica(false);

        reversoImg = new ImageIcon("Cartas/reverso.png").getImage();
        reversoOeste = new ImageIcon("Cartas/reverso2.png").getImage();  
        reversoNorte = new ImageIcon("Cartas/reverso1.png").getImage();  
        reversoEste = new ImageIcon("Cartas/reverso3.png").getImage();   
        
        if (new File("botoncolor.png").exists()) {
            btnColorImg = new ImageIcon("botoncolor.png").getImage();
        } else {
            btnColorImg = new ImageIcon("Cartas/botoncolor.png").getImage();
        }

        JPanel pnlHistorial = new JPanel(new BorderLayout());
        pnlHistorial.setBackground(new Color(10, 25, 10));
        pnlHistorial.setBorder(BorderFactory.createLineBorder(new Color(0, 200, 0), 2));
        pnlHistorial.setBounds(730, 20, 240, 150); 

        JLabel lblTitulo = new JLabel("Historial de jugadas", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(57, 255, 20));
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 200, 0)));
        pnlHistorial.add(lblTitulo, BorderLayout.NORTH);

        txtHistorial = new JTextArea();
        txtHistorial.setBackground(Color.BLACK);
        txtHistorial.setForeground(new Color(57, 255, 20));
        txtHistorial.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtHistorial.setEditable(false);
        txtHistorial.setLineWrap(true);

        JScrollPane scrollHistorial = new JScrollPane(txtHistorial);
        scrollHistorial.setBorder(null);
        pnlHistorial.add(scrollHistorial, BorderLayout.CENTER);
        add(pnlHistorial);

        txtHistorial.append("¡Sistema: Partida Iniciada!\n");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();

                if (mx >= 20 && mx <= 70 && my >= 20 && my <= 70) {
                    String[] opciones = {"Reiniciar Partida", "Volver al Menú Principal", "Cerrar Juego"};
                    int seleccion = JOptionPane.showOptionDialog(MesaPanel.this,
                            "JUEGO PAUSADO\n¿Qué deseas hacer?", "Ajustes del Sistema",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, opciones, opciones[0]);

                    VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(MesaPanel.this);
                    if (seleccion == 0) { 
                        String nombreHumano = game.getJugadores().get(0).getNombre();
                        String[] nombresBots = new String[game.getJugadores().size() - 1];
                        for(int i = 1; i < game.getJugadores().size(); i++) {
                            nombresBots[i-1] = game.getJugadores().get(i).getNombre();
                        }
                        ventana.cambiarAMesa(nombreHumano, nombresBots);
                    } else if (seleccion == 1) { 
                        ventana.setContentPane(new MenuPanel(ventana));
                        ventana.revalidate();
                        ventana.repaint();
                    } else if (seleccion == 2) { 
                        GestorSonido.getInstance().pararMusica();
                        System.exit(0);
                    }
                    return; 
                }

                if (game.getTurnManager().getIndiceActual() != 0 || hayGanador()) return;
                Player humano = game.getJugadores().get(0);

                int btnUnoX = 30;
                int btnUnoY = getHeight() - 120;
                if (mx >= btnUnoX && mx <= btnUnoX + 110 && my >= btnUnoY && my <= btnUnoY + 70) {
                    if (humano.getMano().getCartas().size() == 1 && !unoPresionado) {
                        unoPresionado = true;
                        GestorSonido.getInstance().reproducirEfecto("clic.wav");
                        registrarJugada(humano.getNombre(), null, "UNO");
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(MesaPanel.this, 
                            "Solo puedes decir UNO si te queda exactamente 1 carta.", 
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    return;
                }

                int centroX = getWidth() / 2;
                int centroY = getHeight() / 2;
                int mazoX = centroX - 100;
                int mazoY = centroY - 80;
                int btnRobarX = centroX - 100;
                int btnRobarY = centroY + 70;

                boolean clickEnMazo = (mx >= mazoX && mx <= mazoX + 90 && my >= mazoY && my <= mazoY + 130);
                boolean clickEnBoton = (mx >= btnRobarX && mx <= btnRobarX + 200 && my >= btnRobarY && my <= btnRobarY + 40);

                if (clickEnMazo || clickEnBoton) {
                    boolean tieneJugadaValida = false;
                    for (Carta c : humano.getMano().getCartas()) {
                        if (RuleEngine.esJugadaValida(c, game.getTopCard())) {
                            tieneJugadaValida = true; break;
                        }
                    }

                    if (tieneJugadaValida) {
                        JOptionPane.showMessageDialog(MesaPanel.this, 
                            "¡No puedes robar! Tienes cartas en tu mano que puedes jugar.", 
                            "Movimiento Inválido", JOptionPane.WARNING_MESSAGE);
                        return; 
                    }

                    game.robarCarta(humano);
                    GestorSonido.getInstance().reproducirEfecto("robacarta.wav");
                    registrarJugada(humano.getNombre(), null, "robo");
                    if (humano.getMano().getCartas().size() > 1) unoPresionado = false;

                    game.getTurnManager().avanzar();
                    repaint();
                    procesarTurnosBots(); 
                    return;
                }

                int numCartas = humano.getMano().getCartas().size();
                int anchoCarta = 90, altoCarta = 130;
                int separacion = Math.min(65, 750 / Math.max(1, numCartas)); 
                int inicioX = (getWidth() - (separacion * (numCartas - 1) + anchoCarta)) / 2;
                int posY = getHeight() - 145;

                for (int i = numCartas - 1; i >= 0; i--) { 
                    int cartaX = inicioX + (i * separacion);
                    int anchoClic = (i == numCartas - 1) ? anchoCarta : separacion;

                    if (mx >= cartaX && mx <= cartaX + anchoClic && my >= posY && my <= posY + altoCarta) {
                        Carta c = humano.getMano().getCartas().get(i);
                        
                        if (RuleEngine.esJugadaValida(c, game.getTopCard())) {
                            if (humano.getMano().getCartas().size() == 1 && !unoPresionado) {
                                JOptionPane.showMessageDialog(MesaPanel.this, "¡Se te olvidó gritar UNO! (+2 Cartas)", "Castigo", JOptionPane.ERROR_MESSAGE);
                                game.robarCarta(humano);
                                game.robarCarta(humano);
                                GestorSonido.getInstance().reproducirEfecto("robacarta.wav");
                                registrarJugada("SISTEMA", null, "Castigó a " + humano.getNombre() + " (+2)");
                                return; 
                            }

                            humano.getMano().quitarDeMano(i);
                            game.getDescarte().add(c);
                            GestorSonido.getInstance().reproducirEfecto("tiracarta.wav");

                            if (c.getTipo().equals("CAMBIO_COLOR") || c.getTipo().equals("MAS4")) {
                                String[] opciones = {"Rojo", "Azul", "Verde", "Amarilla"};
                                String elegido = (String) JOptionPane.showInputDialog(MesaPanel.this, 
                                    "¡COMODÍN! Elige el nuevo color:", "Transformación", 
                                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
                                c.setColor(elegido != null ? elegido : "Rojo");
                            }

                            registrarJugada(humano.getNombre(), c, "tiro");
                            unoPresionado = false; 

                            // --- VERIFICACIÓN INSTANTÁNEA SI EL JUGADOR HUMANO SE QUEDÓ SIN CARTAS ---
                            if (hayGanador()) {
                                repaint();
                                revisarFinDeJuego();
                                return;
                            }

                            RuleEngine.aplicarEfecto(c, game, game.getTurnManager());
                            game.getTurnManager().avanzar();
                            repaint();
                            procesarTurnosBots(); 
                        }
                        return; 
                    }
                }
            }
        });
    }

    private void registrarJugada(String jugador, Carta c, String accion) {
        String msj = "";
        if (accion.equals("robo")) {
            msj = jugador + ": robó carta";
        } else if (accion.equals("UNO")) {
            msj = jugador + ": ¡Gritó UNO!";
        } else if (accion.startsWith("Castigó")) {
            msj = "¡" + accion + "!";
        } else {
            if (c.getTipo().equals("CAMBIO_COLOR") || c.getTipo().equals("MAS4")) {
                 String tipo = c.getTipo().equals("CAMBIO_COLOR") ? "Cambio de Color" : "+4";
                 msj = jugador + ": jugó " + tipo + " (" + c.getColor() + ")";
            } else if (c.getTipo().equals("NUMERO")) {
                 msj = jugador + ": tiró " + c.getNumero() + " " + c.getColor();
            } else {
                 msj = jugador + ": jugó " + c.getTipo() + " " + c.getColor();
            }
        }
        txtHistorial.append(msj + "\n");
        txtHistorial.setCaretPosition(txtHistorial.getDocument().getLength()); 
    }

    private void procesarTurnosBots() {
        Timer timer = new Timer(1500, null); 
        timer.addActionListener(e -> {
            if (hayGanador()) {
                ((Timer)e.getSource()).stop();
                revisarFinDeJuego();
                return;
            }

            int turno = game.getTurnManager().getIndiceActual();

            if (turno == 0) { 
                ((Timer)e.getSource()).stop();
                return;
            }

            Player bot = game.getJugadores().get(turno);
            int jugadaIdx = -1;
            
            for (int i = 0; i < bot.getMano().getCartas().size(); i++) {
                if (RuleEngine.esJugadaValida(bot.getMano().getCartas().get(i), game.getTopCard())) {
                    jugadaIdx = i; break;
                }
            }

            if (jugadaIdx == -1) {
                game.robarCarta(bot);
                GestorSonido.getInstance().reproducirEfecto("robacarta.wav");
                registrarJugada(bot.getNombre(), null, "robo");
            } else {
                Carta c = bot.getMano().quitarDeMano(jugadaIdx);
                game.getDescarte().add(c);
                GestorSonido.getInstance().reproducirEfecto("tiracarta.wav");
                
                if (c.getTipo().equals("CAMBIO_COLOR") || c.getTipo().equals("MAS4")) {
                    String[] colores = {"Rojo", "Azul", "Verde", "Amarilla"};
                    c.setColor(colores[(int)(Math.random()*4)]);
                }
                registrarJugada(bot.getNombre(), c, "tiro");
                
                if (bot.getMano().getCartas().size() == 1) {
                    GestorSonido.getInstance().reproducirEfecto("clic.wav");
                    registrarJugada(bot.getNombre(), null, "UNO");
                }
                RuleEngine.aplicarEfecto(c, game, game.getTurnManager());
            }

            game.getTurnManager().avanzar();
            repaint();
            
            if (hayGanador()) {
                ((Timer)e.getSource()).stop();
                revisarFinDeJuego();
            } else if (game.getTurnManager().getIndiceActual() == 0) {
                ((Timer)e.getSource()).stop();
            }
        });
        timer.start();
    }

    private boolean hayGanador() {
        for (Player p : game.getJugadores()) {
            if (p.getMano().estaVacia()) return true;
        }
        return false;
    }

    private void revisarFinDeJuego() {
        Player ganador = null;
        for (Player p : game.getJugadores()) {
            if (p.getMano().estaVacia()) {
                ganador = p;
                break;
            }
        }

        if (ganador != null) {
            GestorSonido.getInstance().pararMusica();

            boolean esHumano = (ganador == game.getJugadores().get(0));
            String estado = esHumano ? "¡GANASTE!" : "¡PERDISTE!";
            String mensaje = estado + "\n\nGanador de la partida: " + ganador.getNombre() + "\n¿Qué deseas hacer ahora?";

            String[] opciones = {"Repetir Partida", "Menú Principal", "Salir"};
            
            int seleccion = JOptionPane.showOptionDialog(this,
                    mensaje, "Fin de la Partida",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, opciones, opciones[0]);

            VentanaPrincipal ventana = (VentanaPrincipal) SwingUtilities.getWindowAncestor(this);
            
            if (seleccion == 0) { 
                String nombreHumano = game.getJugadores().get(0).getNombre();
                String[] nombresBots = new String[game.getJugadores().size() - 1];
                for(int i = 1; i < game.getJugadores().size(); i++) {
                    nombresBots[i-1] = game.getJugadores().get(i).getNombre();
                }
                ventana.cambiarAMesa(nombreHumano, nombresBots);
            } else if (seleccion == 1) { 
                ventana.setContentPane(new MenuPanel(ventana));
                ventana.revalidate();
                ventana.repaint();
            } else if (seleccion == 2 || seleccion == JOptionPane.CLOSED_OPTION) { 
                System.exit(0);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centroX = getWidth() / 2;
        int centroY = getHeight() / 2;

        if (ultimaCartaTop != game.getTopCard()) {
            if (ultimaCartaTop != null && game.getTopCard().getTipo().equals("REVERSA")) {
                sentidoHorario = !sentidoHorario;
            }
            ultimaCartaTop = game.getTopCard();
        }

        Player humano = game.getJugadores().get(0);
        boolean tieneJugadaValida = false;
        for (Carta c : humano.getMano().getCartas()) {
            if (RuleEngine.esJugadaValida(c, game.getTopCard())) {
                tieneJugadaValida = true; break;
            }
        }
        boolean turnoHumano = (game.getTurnManager().getIndiceActual() == 0 && !hayGanador());

        // --- CÍRCULO CENTRAL ---
        g2d.setColor(new Color(0, 255, 0));
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{10, 5}, 0));
        g2d.drawOval(centroX - 160, centroY - 160, 320, 320);

        // --- FLECHAS CURVAS ---
        g2d.setColor(new Color(57, 255, 20)); 
        g2d.setStroke(new BasicStroke(4));
        int radioArco = 180;
        int diametroArco = radioArco * 2;
        int startX = centroX - radioArco;
        int startY = centroY - radioArco;

        g2d.drawArc(startX, startY, diametroArco, diametroArco, 135, 90);
        g2d.drawArc(startX, startY, diametroArco, diametroArco, -45, 90);

        int dTri = 127; 
        if (sentidoHorario) {
            g2d.fillPolygon(new int[]{centroX - dTri - 15, centroX - dTri + 10, centroX - dTri + 5}, 
                            new int[]{centroY - dTri - 5, centroY - dTri - 15, centroY - dTri + 10}, 3);
            g2d.fillPolygon(new int[]{centroX + dTri + 15, centroX + dTri - 10, centroX + dTri - 5}, 
                            new int[]{centroY + dTri + 5, centroY + dTri + 15, centroY + dTri - 10}, 3);
        } else {
            g2d.fillPolygon(new int[]{centroX - dTri - 15, centroX - dTri + 10, centroX - dTri + 5}, 
                            new int[]{centroY + dTri + 5, centroY + dTri + 15, centroY + dTri - 10}, 3);
            g2d.fillPolygon(new int[]{centroX + dTri + 15, centroX + dTri - 10, centroX + dTri - 5}, 
                            new int[]{centroY - dTri - 5, centroY - dTri - 15, centroY - dTri + 10}, 3);
        }

        // --- MAZO Y DESCARTE CENTRADOS ---
        int mazoX = centroX - 100;
        int descarteX = centroX + 10;
        int cartasY = centroY - 80;

        g2d.drawImage(reversoImg, mazoX, cartasY, 90, 130, this);
        Carta top = game.getTopCard();
        Image imgTop = cargarImagenCarta(top);
        g2d.drawImage(imgTop, descarteX, cartasY, 90, 130, this);

        // --- OMNITRIX CON COLOR DE FONDO ---
        int relojSize = 50;
        int relojX = centroX - (relojSize / 2);
        int relojY = centroY - 150; 
        
        Color colActual = Color.DARK_GRAY;
        if (top.getColor().equalsIgnoreCase("Rojo")) colActual = new Color(220, 0, 0);
        else if (top.getColor().equalsIgnoreCase("Azul")) colActual = new Color(0, 120, 245);
        else if (top.getColor().equalsIgnoreCase("Verde")) colActual = new Color(0, 220, 0);
        else if (top.getColor().toLowerCase().contains("amarill")) colActual = new Color(240, 210, 0);
        
        g2d.setColor(colActual);
        g2d.fillOval(relojX + 10, relojY + 10, relojSize - 20, relojSize - 20); 
        g2d.drawImage(btnColorImg, relojX, relojY, relojSize, relojSize, this);

        // Botón Robar
        int btnRobarX = centroX - 100;
        int btnRobarY = centroY + 70;
        int btnRobarW = 200;
        int btnRobarH = 40;

        if (turnoHumano && !tieneJugadaValida) {
            g2d.setColor(new Color(57, 255, 20)); 
            g2d.fillRect(btnRobarX, btnRobarY, btnRobarW, btnRobarH);
            g2d.setColor(Color.BLACK); 
        } else {
            g2d.setColor(new Color(20, 40, 20)); 
            g2d.fillRect(btnRobarX, btnRobarY, btnRobarW, btnRobarH);
            g2d.setColor(new Color(80, 120, 80)); 
        }
        g2d.setFont(new Font("Monospaced", Font.BOLD, 22));
        g2d.drawString("ROBAR", btnRobarX + (btnRobarW - g2d.getFontMetrics().stringWidth("ROBAR")) / 2, btnRobarY + 28);

        // Botón de Ajustes en esquina superior izquierda
        g2d.setColor(new Color(15, 35, 15));
        g2d.fillRoundRect(20, 20, 50, 50, 15, 15);
        g2d.setColor(new Color(0, 200, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(20, 20, 50, 50, 15, 15);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
        g2d.drawString("⚙", 32, 55);

        // Botón "¡UNO!" en esquina inferior izquierda
        int btnUnoX = 30;
        int btnUnoY = getHeight() - 120;
        if (unoPresionado) g2d.setColor(new Color(255, 50, 50)); 
        else if (turnoHumano && humano.getMano().getCartas().size() == 1) g2d.setColor(new Color(200, 0, 0)); 
        else g2d.setColor(new Color(50, 10, 10)); 
        
        g2d.fillRoundRect(btnUnoX, btnUnoY, 110, 70, 10, 10);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(btnUnoX, btnUnoY, 110, 70, 10, 10);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 30));
        g2d.drawString("UNO!", btnUnoX + 20, btnUnoY + 45);

        // --- PANEL DE SUGERENCIA DE CARTA ---
        int sugW = 160;
        int sugH = 120;
        int sugX = getWidth() - sugW - 30; 
        int sugY = getHeight() - sugH - 30;

        g2d.setColor(new Color(0, 30, 0, 200)); 
        g2d.fillRoundRect(sugX, sugY, sugW, sugH, 15, 15);
        g2d.setColor(new Color(57, 255, 20)); 
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(sugX, sugY, sugW, sugH, 15, 15);

        g2d.setFont(new Font("Monospaced", Font.BOLD, 11));
        g2d.setColor(Color.WHITE);
        g2d.drawString("SUGERENCIA A TIRAR", sugX + 20, sugY + 20);
        g2d.drawLine(sugX, sugY + 25, sugX + sugW, sugY + 25);

        if (turnoHumano) {
            Carta sugerencia = null;
            int maxScore = -1;
            for (Carta c : humano.getMano().getCartas()) {
                if (RuleEngine.esJugadaValida(c, game.getTopCard())) {
                    int score = 0;
                    if (c.getColor().equals(game.getTopCard().getColor())) score = 3;
                    else if (!c.getColor().equals("Negro")) score = 2;
                    else score = 1;
                    
                    if (score > maxScore) {
                        maxScore = score;
                        sugerencia = c;
                    }
                }
            }

            if (sugerencia != null) {
                Image imgSug = cargarImagenCarta(sugerencia);
                g2d.drawImage(imgSug, sugX + 55, sugY + 35, 50, 70, this);
            } else {
                g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
                g2d.setColor(new Color(150, 150, 150));
                g2d.drawString("Robar carta", sugX + 35, sugY + 75);
            }
        } else {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
            g2d.setColor(new Color(100, 150, 100)); 
            g2d.drawString("Esperando...", sugX + 30, sugY + 75);
        }

        // --- DISTRIBUCIÓN GRÁFICA DE JUGADORES ---
        int totalJugadores = game.getJugadores().size();

        int numCartasHumano = humano.getMano().getCartas().size();
        g2d.setColor(game.getTurnManager().getIndiceActual() == 0 ? new Color(57, 255, 20) : Color.WHITE);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString(humano.getNombre() + " (TÚ) - " + numCartasHumano + " CARTAS", centroX - 80, getHeight() - 160);

        if (numCartasHumano > 0) {
            int anchoCarta = 90, altoCarta = 130;
            int separacion = Math.min(65, 750 / numCartasHumano); 
            int inicioX = (getWidth() - (separacion * (numCartasHumano - 1) + anchoCarta)) / 2;
            for (int i = 0; i < numCartasHumano; i++) {
                g2d.drawImage(cargarImagenCarta(humano.getMano().getCartas().get(i)), inicioX + (i * separacion), getHeight() - 145, anchoCarta, altoCarta, this);
            }
        }

        if (totalJugadores > 1) {
            Player bot1 = game.getJugadores().get(1);
            int cartasBot1 = bot1.getMano().getCartas().size();
            int sepBot = 25, anchoCB = 90, altoCB = 60;
            int inicioYBot = (getHeight() - ((cartasBot1 - 1) * sepBot + altoCB)) / 2;
            
            g2d.setColor(game.getTurnManager().getIndiceActual() == 1 ? new Color(57, 255, 20) : Color.WHITE);
            g2d.drawString(bot1.getNombre(), 40 + anchoCB + 15, centroY); 
            
            for (int i = 0; i < cartasBot1; i++) {
                g2d.drawImage(reversoOeste, 40, inicioYBot + (i * sepBot), anchoCB, altoCB, this);
            }
        }

        if (totalJugadores > 2) {
            Player bot2 = game.getJugadores().get(2);
            int cartasBot2 = bot2.getMano().getCartas().size();
            int sepBot = 35, anchoCB = 60, altoCB = 90;
            int inicioXBot = (getWidth() - ((cartasBot2 - 1) * sepBot + anchoCB)) / 2;
            
            g2d.setColor(game.getTurnManager().getIndiceActual() == 2 ? new Color(57, 255, 20) : Color.WHITE);
            g2d.drawString(bot2.getNombre(), centroX - (g2d.getFontMetrics().stringWidth(bot2.getNombre()) / 2), 145);
            
            for (int i = 0; i < cartasBot2; i++) {
                g2d.drawImage(reversoNorte, inicioXBot + (i * sepBot), 30, anchoCB, altoCB, this);
            }
        }

        if (totalJugadores > 3) {
            Player bot3 = game.getJugadores().get(3);
            int cartasBot3 = bot3.getMano().getCartas().size();
            int sepBot = 20, anchoCB = 90, altoCB = 60; 
            
            int altoTotalPila = (cartasBot3 - 1) * sepBot + altoCB;
            int inicioYBot = Math.max(210, (getHeight() - altoTotalPila) / 2 + 30);
            int posX = getWidth() - 130; 
            
            g2d.setColor(game.getTurnManager().getIndiceActual() == 3 ? new Color(57, 255, 20) : Color.WHITE);
            g2d.drawString(bot3.getNombre(), posX - g2d.getFontMetrics().stringWidth(bot3.getNombre()) - 15, inicioYBot + (altoTotalPila / 2));
            
            for (int i = 0; i < cartasBot3; i++) {
                g2d.drawImage(reversoEste, posX, inicioYBot + (i * sepBot), anchoCB, altoCB, this);
            }
        }
    }

    private Image cargarImagenCarta(Carta c) {
        String base = "";
        String color = c.getColor().toLowerCase();
        if (color.equals("amarillo")) color = "amarilla";
        
        if (c.getTipo().equals("CAMBIO_COLOR")) {
            base = "Comodin";
        } else if (c.getTipo().equals("MAS4")) {
            base = "Comodin_Mas4";
        } else if (c.getTipo().equals("NUMERO")) {
            base = color + c.getNumero();
        } else if (c.getTipo().equals("BLOQUEO")) {
            base = "Bloqueo_" + color;
        } else if (c.getTipo().equals("REVERSA")) {
            base = "reversa_" + color;
        } else if (c.getTipo().equals("MAS2")) {
            base = "Mas2_" + color;
        }
        
        File filePng = new File("Cartas/" + base + ".png");
        if (filePng.exists()) return new ImageIcon("Cartas/" + base + ".png").getImage();
        else return new ImageIcon("Cartas/" + base + ".jpg").getImage();
    }
}