package cifrar_descifrar_concurrente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {

        try (
                Socket socket = new Socket("192.168.15.127", 1234); // Conexión
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true); // Instancia para enviar
                BufferedReader in = new BufferedReader( // Instancia para leer
                        new InputStreamReader(socket.getInputStream()));
                Scanner teclado = new Scanner(System.in); // Scanner para leer por teclado
        ) {

            System.out.println("Cliente conectado");

            // Menú inicial
            System.out.println("\n--- MENÚ ---");
            System.out.println("Opciones: CIFRAR, DESCIFRAR, EXIT");

            // Bucle para que no cierre
            while (true) {
                System.out.print("Escribe tu orden: ");
                String orden = teclado.nextLine(); // Leemos la orden
                out.println(orden); // Se la mandamos al servidor

                // Si es EXIT, leemos el ADIOS y rompemos el bucle
                if (orden.equals("EXIT")) {
                    System.out.println(in.readLine());
                    break;
                }

                // Lógica para pasar la cadena
                String respuesta = in.readLine();
                if (respuesta.equals("Pasame la cadena")){
                    System.out.print("Ingresa la cadena: ");
                    out.println(teclado.nextLine()); // Leemos cadena y la mandamos
                    System.out.println("Resultado: " + in.readLine());
                } else {
                    System.out.println(respuesta);
                }
            }

        } catch (IOException e){
            System.err.println("Error en conexión");
        }
    }
}