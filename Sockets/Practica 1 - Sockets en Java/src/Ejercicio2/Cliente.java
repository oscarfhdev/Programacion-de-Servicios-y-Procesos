package Ejercicio2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Conectando al servidor...");

        try (
            Socket socket = new Socket("localhost", 1234);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner teclado = new Scanner(System.in);
        ) {
            System.out.println("Conexión establecida con éxito.");
            System.out.println("\n--- COMANDOS DISPONIBLES ---");
            System.out.println("INC -> Suma 1 al contador");
            System.out.println("GET -> Devuelve el contador actual");
            System.out.println("RESET -> Pone el contador a 0");
            System.out.println("EXIT -> Cierra la conexión");

            while (true) {
                System.out.print("\nEscribe el comando: ");
                String comando = teclado.nextLine(); // Leemos del teclado

                // Le mandamos el comando al servidor
                out.println(comando.toUpperCase());

                // Si es EXIT leemos la despedida y cerramos
                if (comando.equalsIgnoreCase("EXIT")) {
                    System.out.println("Servidor: " + in.readLine());
                    break;
                }

                // Mostramos lo que responde el servidor a nuestra operación
                String respuesta = in.readLine();
                if (respuesta != null) {
                    System.out.println("Servidor: " + respuesta);
                }
            }

        } catch (Exception e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
