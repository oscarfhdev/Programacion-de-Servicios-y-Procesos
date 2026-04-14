import java.io.*;

public class HiloEscucha extends Thread {
    private BufferedReader entrada;

    public HiloEscucha(BufferedReader entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        try {
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                System.out.println(mensaje);

                // Si recibimos el mensaje de fin, paramos
                if (mensaje.equals("FIN | La partida ha terminado")) {
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            System.err.println("Conexión con el servidor cerrada.");
        }
    }
}
