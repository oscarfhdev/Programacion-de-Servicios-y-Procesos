package Ejercicio2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket cliente;

    public ClientHandler(Socket socket) {
        this.cliente = socket; // Recibe el socket de la conexión
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);

            String mensaje;
            
            // Leemos lo que nos manda el cliente hasta que sea null o envíe EXIT
            while ((mensaje = in.readLine()) != null) {
                if (mensaje.equals("EXIT")) {
                    break; // Salimos del bucle si quiere terminar
                }

                // Vemos qué ha pedido
                switch (mensaje) {
                    case "INC":
                        Servidor.incrementar();
                        out.println("Contador incrementado correctamente.");
                        break;
                    case "GET":
                        int valor = Servidor.getContador();
                        out.println("El contador actual es: " + valor);
                        break;
                    case "RESET":
                        Servidor.resetear();
                        out.println("Contador reseteado a 0.");
                        break;
                    default:
                        out.println("Comando incorrecto. Usa INC, GET, RESET o EXIT.");
                }
            }

            // Despedida y cerramos solo su socket
            out.println("¡Adiós!");
            System.out.println("Cliente desconectado: " + cliente.getInetAddress());
            cliente.close();

        } catch (Exception e) {
            System.err.println("Error en el hilo del cliente: " + e.getMessage());
        }
    }
}
