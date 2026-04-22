
import java.io.BufferedReader;
public class ReceptorMensajesCliente extends Thread {

    private BufferedReader in;

    public ReceptorMensajesCliente(BufferedReader in) {
        this.in = in;
    }

    public void run() {
        try {
            String mensaje;

            while ((mensaje = in.readLine()) != null) {
                System.out.println(mensaje);
            }

        } catch (Exception e) {
            System.out.println("Conexión cerrada con el servidor.");
        }
    }
}