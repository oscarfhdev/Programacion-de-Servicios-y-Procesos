package Ejercicio2;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    // Contador global compartido por todos los hilos
    private static int contador = 0;

    // Métodos synchronized para gestionar la concurrencia
    public static synchronized void incrementar() {
        contador++;
    }

    public static synchronized int getContador() {
        return contador;
    }

    public static synchronized void resetear() {
        contador = 0;
    }

    public static void main(String[] args) {
        System.out.println("Servidor multihilo arrancado y esperando en el puerto 1234...");

        try {
            ServerSocket server = new ServerSocket(1234);

            // Bucle infinito para ir aceptando clientes
            while (true) {
                Socket cliente = server.accept();
                System.out.println("¡Nuevo cliente conectado desde " + cliente.getInetAddress() + "!");

                // Iniciamos el hilo pasándole el socket y arrancamos
                ClientHandler hilo = new ClientHandler(cliente);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
