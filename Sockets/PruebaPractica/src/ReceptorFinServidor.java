import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReceptorFinServidor extends Thread {

    public void run() {
        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            String linea;

            while (Servidor.isActivo() && (linea = teclado.readLine()) != null) {
                if (linea.equalsIgnoreCase("FIN")) {
                    System.out.println("Comando FIN recibido.");
                    Servidor.apagarServidor();
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Error en el receptor de FIN del servidor.");
        }
    }
}
