import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    
    public static ArrayList<ClienteHandler> clientes=new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Servidor arrancado y esperando...");

        try{
            ServerSocket server = new ServerSocket(5000);
            while (clientes.size()<1){
                Socket cliente = server.accept();
                ClienteHandler cl = new ClienteHandler(cliente);
                cl.start();
                clientes.add(cl);
            }
            System.out.println("empieza partida");
            new GameManager(clientes).iniciarPartida();

        }catch (Exception e){
            System.err.println("Error de conexión");
        }
    }
    
}
