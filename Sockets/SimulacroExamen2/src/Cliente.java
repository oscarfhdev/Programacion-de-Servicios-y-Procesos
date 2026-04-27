import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        

        try (
                Socket socket = new Socket("localhost", 5000);
                PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
                ) {

                    // El hilo MAIN se bloquea aquí esperando al TECLADO del usuario
                    System.out.print("Introduce el nombre de usuario: ");
                    String nombreUsuario = scanner.nextLine();
                    out.println(nombreUsuario);


                    // Hilo paralelo para ESCUCHAR la red constantemente - ESCUCHA LOS MENSAJES DEL SERVIDOR Y LOS VUELCA EN PANTALLA
                    new ReceptorMensajes(in).start(); 


                    while(true) {
                            String mensaje = scanner.nextLine();
                            out.println(mensaje);
                }

        }catch (IOException e){
            System.err.println("Error en conexión");
        }
    }
}
