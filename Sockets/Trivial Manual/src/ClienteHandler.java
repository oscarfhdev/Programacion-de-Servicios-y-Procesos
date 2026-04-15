import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler extends Thread {
    private Socket cliente;
    private boolean haRespondido;
    private  String respuestaActual;
    private Integer puntos;



    public ClienteHandler(Socket socket) {
        this.cliente = socket; // Recibe el socket de la conexión
    }

    @Override
    public void run() {
        try(
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);
        ){

            while (true){
                String mensaje = entrada.readLine();

                if (GameManager.isRondaAbierta() && !haRespondido) {
                    this.respuestaActual = mensaje;
                    this.haRespondido = true;
                    salida.println("INFO|Respuesta registrada");
                }
            }


        } catch (Exception e) { e.printStackTrace(); }
    }
}