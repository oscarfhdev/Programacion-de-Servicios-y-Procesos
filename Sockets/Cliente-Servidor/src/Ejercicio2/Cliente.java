package Ejercicio2;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Cliente conectado");

        try (
                Socket socket = new Socket("localhost", 1234);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner teclado = new Scanner(System.in);
        ) {
            // Mostramos el menú una vez
            System.out.println("\n--- MENÚ CALCULADORA ---");
            System.out.println("1. Agregar número");
            System.out.println("2. Mostrar lista");
            System.out.println("3. Calcular media");
            System.out.println("4. Buscar máximo");
            System.out.println("5. Borrar lista");
            System.out.println("6. Cerrar sesión");

            while (true) {
                System.out.print("\nElige opción (1-6): ");
                String opcion = teclado.nextLine();

                if (opcion.equals("6")) {
                    out.println("6"); // Avisamos al servidor del exit
                    System.out.println("Servidor: " + in.readLine());
                    break;
                }

                // Si seleccionamos agregar, juntamos el 1 y el número a agregar
                if (opcion.equals("1")) {
                    System.out.print("Introduce el número a guardar: ");
                    String numero = teclado.nextLine();
                    // Juntamos el comando y el número con un espacio y lo mandamos
                    out.println("1 " + numero);
                } else {
                    // Si es cualquier otra opción lo mandamos para que el servidor nos lo muestre
                    out.println(opcion);
                }

                // Mostramos lo que nos devuelva el servidor
                System.out.println("Resultado: " + in.readLine());
            }

        } catch (IOException e) {
            System.err.println("Error de conexión");
        }
    }
}