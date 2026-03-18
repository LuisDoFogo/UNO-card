import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Game juego = new Game();
        Scanner sc = new Scanner(System.in);
        boolean turnoDelJugador = true;

        while (!juego.getManoJugador().estaVacia() && !juego.getManoBot().estaVacia()) {
            Carta mesa = juego.getTopCard();
            
            if (turnoDelJugador) {
                System.out.println("\n--- MESA: " + mesa + " | \n*******BOT: " + juego.getManoBot().getCartas().size() + " cartas *******\n");

                juego.getManoJugador().mostrarMano();
                
                System.out.print("\nTu turno (Indice, -1 robar): ");


                
                if (!sc.hasNextInt()) {
                    System.out.println("Error : Solo puedes ingresar  numeros");
                    sc.next(); // Limpia la letra
                     continue; }
                int idx = sc.nextInt();

                if (idx == -1) {
                    juego.robarDeMazo(juego.getManoJugador());
                    turnoDelJugador = false;
                } else if (idx >= 0 && idx < juego.getManoJugador().getCartas().size()) {
                    Carta elegida = juego.getManoJugador().getCartas().get(idx);
                    
                    if (elegida.esJugableSobre(mesa)) {
                        juego.jugarCarta(juego.getManoJugador(), idx);
                        
                        // --- REGLA UNO JUGADOR ---
                        if (juego.getManoJugador().getCartas().size() == 1) {
                            System.out.print("¡Te queda una carta! Escribe '1' para decir UNO rápido: ");
                            String grito = sc.next();
                            if (!grito.equals("1")) {
                                System.out.println("¡No dijiste UNO! Robas 2 cartas de castigo.");
                                juego.robarDeMazo(juego.getManoJugador());
                                juego.robarDeMazo(juego.getManoJugador());
                            } else {
                                System.out.println("¡Cantaste UNO con éxito!");
                            }
                        }

                        // Lógica de Comodín Negro
                        if (elegida.getColor().equals("Negro")) {
                            System.out.print("Elige color (1:Rojo, 2:Azul, 3:Verde, 4:Amarillo): ");
                            int op = sc.nextInt();
                            switch(op) {
                                case 1: elegida.setColor("Rojo"); break;
                                case 2: elegida.setColor("Azul"); break;
                                case 3: elegida.setColor("Verde"); break;
                                case 4: elegida.setColor("Amarillo"); break;
                                default: elegida.setColor("Rojo");
                            }
                        }
                        
                        juego.aplicarCastigo(elegida, juego.getManoBot());

                        if (!juego.tieneEfectoEspecial(elegida)) {
                            turnoDelJugador = false;
                        } else {
                            System.out.println("¡Efecto especial! Sigues tú.");
                        }
                    } else {
                        System.out.println("Esa carta no se puede jugar.");
                    }
                }
            } else {
                // --- TURNO DEL BOT ---
                System.out.println("--------\nTurno del Bot...----------");
                boolean botJugo = false;
                for (int i = 0; i < juego.getManoBot().getCartas().size(); i++) {
                    Carta cb = juego.getManoBot().getCartas().get(i);
                    if (cb.esJugableSobre(juego.getTopCard())) {
                        juego.jugarCarta(juego.getManoBot(), i);
                        
                        // --- REGLA UNO BOT ---
                        if (juego.getManoBot().getCartas().size() == 1) {
                            System.out.println("¡El Bot grita: UNO!");
                        }

                        if (cb.getColor().equals("Negro")) cb.setColor("Verde"); // El bot elige Verde
                        System.out.println("\nEl Bot jugó: " + cb);
                        juego.aplicarCastigo(cb, juego.getManoJugador());
                        
                        if (!juego.tieneEfectoEspecial(cb)) turnoDelJugador = true;
                        botJugo = true;
                        break;
                    }
                }
                if (!botJugo) {
                    System.out.println("++++++++El Bot no tiene cartas y roba:++++++++++");
                    juego.robarDeMazo(juego.getManoBot());
                    turnoDelJugador = true;
                }
            }
        }

        // --- GANADOR ---
        if (juego.getManoJugador().estaVacia()) {
            System.out.println("\n¡FELICIDADES! Ganaste la partida.");
        } else {
            System.out.println("\nEl Bot ha ganado. Más suerte la próxima.");
        }
    }
}
