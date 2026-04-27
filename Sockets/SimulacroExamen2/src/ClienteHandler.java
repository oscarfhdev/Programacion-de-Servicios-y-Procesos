import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler extends Thread {
    
    private Socket cliente;
    private BufferedReader in;
    private PrintWriter out;
    public String nombreCliente;
    public Integer numeroMensajes = 0;

    public ClienteHandler(Socket socket) throws Exception {
        this.cliente = socket;
        this.in=new BufferedReader(
            new InputStreamReader(cliente.getInputStream()));
        this.out=new PrintWriter(
            cliente.getOutputStream(), true);
    }

    @Override
    public void run() {
            try {
                nombreCliente = in.readLine();
                MessageManager.enviarMensajeTodos("El usuario " + nombreCliente + " ha entrado al chat", this);
                while (true) {
                    String mensaje = in.readLine();
                    if (mensaje != null){
                        if (mensaje.startsWith("/privado")){
                            MessageManager.enviarPrivado(mensaje, this);
                            continue;
                        }
                        numeroMensajes++;
                        MessageManager.enviarMensajeTodos("[" + this.nombreCliente + "]: " + mensaje, this);
                    }
                }
            }catch (IOException e){
                System.err.println("Error de conexión");
            }
    }

    void enviarMensaje(String msg){
        out.println(msg);
    }

}
