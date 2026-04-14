import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            Scanner teclado = new Scanner(System.in);

            // Leemos el mensaje de bienvenida del servidor
            String bienvenida = entrada.readLine(); // "Bienvenido al trivia, introduce tu nick"
            System.out.println(bienvenida);

            // Mandamos el nick
            System.out.print(">> ");
            String nick = teclado.nextLine();
            salida.println(nick);

            // Leemos la confirmación
            String confirmacion = entrada.readLine(); // "Conectado correctamente como ..."
            System.out.println(confirmacion);

            // Arrancamos el hilo que escucha los mensajes del servidor
            HiloEscucha hiloEscucha = new HiloEscucha(entrada);
            hiloEscucha.start();

            // El main se queda leyendo del teclado y enviando al servidor
            while (hiloEscucha.isAlive()) {
                if (teclado.hasNextLine()) {
                    String linea = teclado.nextLine();

                    // Si el usuario escribe solo una letra (a, b, c, d), la formateamos
                    if (linea.length() == 1 && "abcdABCD".contains(linea)) {
                        salida.println("RESPUESTA|" + linea.toLowerCase());
                    } else {
                        salida.println(linea);
                    }
                }
            }

            teclado.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
