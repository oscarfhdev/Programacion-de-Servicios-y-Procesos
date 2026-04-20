package Ejercicio1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        System.out.println("Cliente conectado al servidor...");

        try (
            Socket socket = new Socket("localhost", 1234);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner teclado = new Scanner(System.in);
        ) {
            boolean logueado = false;

            // Bucle de login
            while (!logueado) {
                String peticion = in.readLine();
                
                if (peticion == null || peticion.equals("LOGIN_FAIL")) {
                    System.out.println("Sesión cerrada por agotar intentos.");
                    return; // Terminamos
                }

                if (peticion.equals("LOGIN_OK")) {
                    logueado = true;
                    System.out.println("Login correcto!");
                    break;
                }

                System.out.println("Servidor: " + peticion);
                
                // Si el servidor nos pide algo como "Usuario:" o "Contraseña:", el cliente envía
                if (peticion.equals("Usuario:") || peticion.equals("Contraseña:")) {
                    System.out.print("-> ");
                    out.println(teclado.nextLine());
                }
            }

            // Confirmamos menú
            String aviso = in.readLine();
            if (aviso == null || !aviso.equals("MOSTRAMOS_MENU")) {
                return;
            }

            // Bucle del menú
            while (true) {
                System.out.println("\n--- MENÚ ---");
                System.out.println("1. Sumar");
                System.out.println("2. Contar vocales");
                System.out.println("3. Invertir cadena");
                System.out.println("4. Primo");
                System.out.println("5. Salir");
                
                System.out.print("Escribe tu orden: ");
                String orden = teclado.nextLine();
                out.println(orden); // Mandamos la opción

                if (orden.equals("5")) {
                    System.out.println(in.readLine()); // Leemos el ADIOS
                    break;
                }

                // Según la opción, pedimos los datos necesarios
                if (orden.equals("1")) {
                    // Nos pide el primer número
                    System.out.println("Servidor: " + in.readLine());
                    out.println(teclado.nextLine());
                    
                    // Nos pide el segundo número
                    System.out.println("Servidor: " + in.readLine());
                    out.println(teclado.nextLine());
                    
                    // Resultado final
                    System.out.println("Respuesta: " + in.readLine());

                } else if (orden.equals("2") || orden.equals("3") || orden.equals("4")) {
                    // Nos pide la cadena o número
                    System.out.println("Servidor: " + in.readLine());
                    out.println(teclado.nextLine());
                    
                    // Leemos el resultado final
                    System.out.println("Respuesta: " + in.readLine());

                } else {
                    // Operación no permitida u otra respuesta fallida
                    System.out.println("Servidor: " + in.readLine());
                }
            }

        } catch (IOException e) {
            System.err.println("Error en conexión");
        }
    }
}
