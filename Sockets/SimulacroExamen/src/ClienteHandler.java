import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClienteHandler extends Thread {
    
    private Socket cliente;
    private boolean haRespondido;
    private String respuestaActual;
    private Double puntos;
    
    private BufferedReader in;
    private PrintWriter out;
    private String nombreCliente;

    public ClienteHandler(Socket socket) throws Exception {
        this.cliente = socket;
        this.puntos=0.0;
        respuestaActual=null;
        this.in=new BufferedReader(
            new InputStreamReader(cliente.getInputStream()));
        this.out=new PrintWriter(
            cliente.getOutputStream(), true);
        nombreCliente = in.readLine();
        System.out.println(nombreCliente);
    }

    @Override
    public void run() {
            try {
               while (true) {
                        String mensaje = in.readLine(); 
                        // Escucha constante (Bloqueante) - Lee respuesta.
                        
                        // Reglas del juego: solo guardamos si la ronda está abierta
                        if (GameManager.isRondaAbierta() && !haRespondido) {
                            this.respuestaActual = mensaje; // registra la respuesta
                            this.haRespondido = true;
                            out.println("INFO|Respuesta registrada"); // manda al cliente mensaje
                        }
                    }
                }catch (IOException e){
                    System.err.println("Error de conexión");
                }
    }

    public String mostrarNota(){
        String nota= nombreCliente + " ha sacado: " +puntos;
        out.println(nota);
        return nota;
    }

    public void enviarMensaje(String msg){
        System.out.println("mensaje enviado a "+ nombreCliente+" "+msg);
        out.println(msg);
    }

    public void limpiaRespuesta(){
        haRespondido=false;
        respuestaActual=null;
    }

    public void corregirRespuesta(String sol){
        if (respuestaActual!=null && respuestaActual.equalsIgnoreCase(sol)){
            //Respuesta correcta
            puntos++;
            enviarMensaje("Respuesta correcta");
        }else if (!respuestaActual.equalsIgnoreCase(sol)) {
            puntos-=0.3;
            enviarMensaje("Error. La respuesta correcta es: "+sol);
        }else{
            enviarMensaje("Contesta cabron!!!!");
        }
    }


}
