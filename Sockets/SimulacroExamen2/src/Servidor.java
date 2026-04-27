import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Servidor {
    
    public static ArrayList<ClienteHandler> clientes=new ArrayList<>();
    static boolean encendido = true;

    public static void main(String[] args) {
        System.out.println("Servidor arrancado y esperando...");
        System.out.println("Escribe EXIT para salir");
        try{
            ServerSocket server = new ServerSocket(5000);
            CerrarServer cerrarServer = new CerrarServer(new Scanner(System.in), server);
            cerrarServer.start();
            ClienteHandler cl;
            while (Servidor.encendido){
                Socket cliente = server.accept();
                cl = new ClienteHandler(cliente);
                clientes.add(cl);
                cl.start();
            }
        }catch (Exception e){
            if (!Servidor.encendido) {
                System.out.println("Servidor cerrado correctamente.");
            } else {
                // Si encendido sigue siendo TRUE, es un error de red de verdad
                System.err.println("Error de conexión inesperado: " + e.getMessage());
            }        }
    }
    
}
