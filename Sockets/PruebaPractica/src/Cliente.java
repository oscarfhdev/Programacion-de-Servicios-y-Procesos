import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String host = "localhost";
        int puerto = 5000;

        try {
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Introduce tu nombre de usuario: ");
            String nombreUsuario = sc.nextLine();

            Socket socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor.");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(nombreUsuario);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            // Hilo que escucha constantemente los mensajes que llegan del servidor
            ReceptorMensajesCliente receptor = new ReceptorMensajesCliente(in);
            receptor.start();

            String mensaje;

            // El hilo principal queda leyendo teclado y enviando mensajes
            while ((mensaje = teclado.readLine()) != null) {
                out.println(mensaje);

                if (mensaje.equalsIgnoreCase("ADIOS")) {
                    System.exit(0);
                }
            }

            socket.close();
            System.out.println("Cliente cerrado.");

        } catch (Exception e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}