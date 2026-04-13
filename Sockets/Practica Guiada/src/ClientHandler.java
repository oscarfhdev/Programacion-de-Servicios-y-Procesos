import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket cliente;

    public ClientHandler(Socket socket) {
        this.cliente = socket; // Recibe el socket de la conexión
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

            String mensaje = entrada.readLine();
            System.out.println("Hilo atendiendo: Mensaje recibido: " + mensaje);
            salida.println("Mensaje recibido, gracias");

            cliente.close(); // Cerramos solo el socket del cliente
        } catch (Exception e) { e.printStackTrace(); }
    }
}