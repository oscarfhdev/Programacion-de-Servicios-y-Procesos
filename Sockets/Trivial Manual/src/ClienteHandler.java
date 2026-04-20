import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler extends Thread {
    private Socket cliente;
    private boolean haRespondido;
    private  String respuestaActual;
    private Double puntos;
    BufferedReader entrada;
    PrintWriter salida;


    public ClienteHandler(Socket socket) throws IOException {
        this.cliente = socket; // Recibe el socket de la conexión
        this.puntos = 0.0;
         respuestaActual = null;
         this.entrada= new BufferedReader(new InputStreamReader(cliente.getInputStream()));
         this.salida = new PrintWriter(cliente.getOutputStream(), true);
    }

    @Override
    public void run() {
        try /*(
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            salida = new PrintWriter(cliente.getOutputStream(), true);

        )*/{

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


    public void enviarMensaje(String msg){
        System.out.println("Mensaje enviado a " + cliente.getInetAddress() + " " + msg);
        salida.println(msg);
    }

    public void limpiarRespuesta(){
        haRespondido= false;
        respuestaActual = null;
    }

    public void corregirRespuesta(String sol){
        if(respuestaActual == null){
            enviarMensaje("Contesta cabrón!!!!");

        }
        else if(respuestaActual.equalsIgnoreCase(sol)){
            // Respuesta correcta
            puntos++;
            enviarMensaje("Respuesta correcta");
        }
        else {
            puntos -= 0.3;
            enviarMensaje("Error: La respuesta correcta es: " + sol);

        }
    }

    public String mostrarNota() {
        String nota = cliente.getInetAddress() + " ha sacado: " + puntos;
        salida.println(nota);
        return nota;
    }
}