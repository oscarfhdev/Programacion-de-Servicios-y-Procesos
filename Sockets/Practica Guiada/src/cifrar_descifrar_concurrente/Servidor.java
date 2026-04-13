package cifrar_descifrar_concurrente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Servidor arrancado y esperando...");

        try (
            ServerSocket server = new ServerSocket(1234); // Creamos el servidor en el puerto 1234
        ){
           while (true){
               Socket cliente = server.accept(); // Pausa esperando a que se conecte un cliente
               new ClientHandler(cliente).start();
           }

        } catch (IOException e){
            System.err.println("Error de conexión");
        }
    }

}