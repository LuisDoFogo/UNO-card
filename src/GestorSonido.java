import java.io.File;
import javax.sound.sampled.*;

public class GestorSonido {
    private static GestorSonido instancia;
    private Clip clipFondo, clipMenu;

    private GestorSonido() {}

    public static GestorSonido getInstance() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    public void reproducirEfecto(String nombre) {
        try {
            File f = new File("sonidos/" + nombre);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip c = AudioSystem.getClip();
            c.open(ais);

            // --- AUMENTAR VOLUMEN DE EFECTOS ---
            if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
                float maximoPermitido = volume.getMaximum();
                float aumentoDeseado = 6.0f; // Sube 6 decibelios (el máximo seguro suele estar cerca de este valor)
                // Usamos Math.min para no pasarnos del límite de la tarjeta de sonido
                volume.setValue(Math.min(aumentoDeseado, maximoPermitido)); 
            }

            c.start();
        } catch (Exception e) { 
            System.out.println("Falla al intentar reproducir el efecto: " + nombre);
            e.printStackTrace(); 
        }
    }

    public void iniciarMusica(boolean esMenu) {
        pararMusica();
        try {
            String archivo = esMenu ? "fondomenu.wav" : "fondojuego.wav";
            File f = new File("sonidos/" + archivo);
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            
            // Usamos un clip temporal para configurarlo antes de asignarlo
            Clip clipTemporal = AudioSystem.getClip();
            clipTemporal.open(ais);

            // --- BAJAR VOLUMEN DE LA MÚSICA (Ambientación) ---
            if (clipTemporal.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volume = (FloatControl) clipTemporal.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-20.0f); // Le restamos 20 decibelios para que sea un fondo suave
            }

            if (esMenu) { 
                clipMenu = clipTemporal; 
                clipMenu.loop(Clip.LOOP_CONTINUOUSLY); 
            } else { 
                clipFondo = clipTemporal; 
                clipFondo.loop(Clip.LOOP_CONTINUOUSLY); 
            }
        } catch (Exception e) { 
            System.out.println("Falla al intentar iniciar la música de fondo");
            e.printStackTrace(); 
        }
    }

    public void pararMusica() {
        if (clipMenu != null && clipMenu.isRunning()) clipMenu.stop();
        if (clipFondo != null && clipFondo.isRunning()) clipFondo.stop();
    }
}