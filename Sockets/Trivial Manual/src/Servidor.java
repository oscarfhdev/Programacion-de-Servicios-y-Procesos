import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    static ArrayList<ClienteHandler> clientes = new ArrayList<>();
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Servidor concurrente escuchando en el puerto 5000...");

            while (clientes.size() < 2) {
                Socket cliente = server.accept();
                System.out.println("¡Nuevo cliente conectado desde " + cliente.getInetAddress() + "!");
                ClienteHandler cl = new ClienteHandler(cliente);
                cl.start();
                clientes.add(cl);
            }
            new GameManager(clientes).iniciarPartida();
        } catch (Exception e) { e.printStackTrace(); }
    }
}