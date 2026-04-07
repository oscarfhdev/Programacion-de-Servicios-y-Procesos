package Ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Esperando conexión...");

        // Un solo try que gestiona todos los recursos en cascada
        try (ServerSocket server = new ServerSocket(1234);
             Socket cliente = server.accept(); // Se detiene aquí hasta que alguien entra
             BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)) {

            System.out.println("Cliente conectado!");

            String mensaje = in.readLine();

            String cadena = null;
            if (mensaje != null) {
                switch (mensaje) {
                    case "CIFRAR":
                        break;
                    case "DESCIFRAR":
                        out.println("Pasame la cadena");
                        cadena=in.readLine();
                        break;
                    default:
                        out.println("La operación solicitada no existe");
                }
                out.println(cifrar_descifrar(mensaje, cadena));
            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
        // Al llegar aquí, TODO (server, cliente, in, out) se ha cerrado automáticamente.
    }

    static String cifrar_descifrar(String cadena, String operacion){
        return "hola";
    }
}