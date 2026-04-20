import java.io.*;
import java.net.*;

public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nick;
    private int puntos;
    private String respuestaActual;
    private long tiempoRespuesta;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.puntos = 0;
        this.respuestaActual = null;
        this.tiempoRespuesta = 0;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            // Pedir nick al conectarse
            salida.println("Bienvenido al trivia, introduce tu nick");
            while (true) {
                String intentoNick = entrada.readLine();
                if (intentoNick == null) return;
                
                if (Servidor.existeNick(intentoNick)) {
                    salida.println("ERROR| Ese nick ya está en uso. Introduce otro:");
                } else {
                    this.nick = intentoNick;
                    break;
                }
            }

            salida.println("Conectado correctamente como " + nick);
            System.out.println("Jugador registrado: " + nick);

            // Bucle de escucha durante el juego
            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.startsWith("RESPUESTA|")) {
                    respuestaActual = mensaje.substring(10).trim().toLowerCase();
                    this.tiempoRespuesta = System.currentTimeMillis();
                    salida.println("INFO| Se ha registrado tu respuesta");
                }
            }

        } catch (IOException e) {
            System.err.println("Cliente desconectado: " + nick);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviar(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
        }
    }

    public String getNick() {
        return nick;
    }

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntos(int cantidad) {
        puntos += cantidad;
    }

    public void restarPunto() {
        puntos--;
    }

    public String getRespuestaActual() {
        return respuestaActual;
    }
    
    public long getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void resetRespuesta() {
        respuestaActual = null;
        tiempoRespuesta = 0;
    }

    public void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
