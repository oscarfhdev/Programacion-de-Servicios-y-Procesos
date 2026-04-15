import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try(
            Socket socket = new Socket("localhost", 5000);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))){

            new ReceptorMensajes(entrada).start();

            while (true){
                String respuesta = teclado.readLine();
                salida.println(respuesta);
            }

        } catch (IOException e) {
            System.err.println("Error de conexión");;
        }
    }
}