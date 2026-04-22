
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String nick;
    private int contadorMensajes;
    private boolean conectado;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.contadorMensajes = 0;
        this.conectado = true;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Error al crear flujos del cliente.");
        }
    }

    public void run() {
        try {
            nick = in.readLine();
            System.out.println("Cliente [" + nick + "] conectado al servidor");
            Servidor.enviarATodos("[" + nick + "]" + "se ha unido al chat", this);

            String mensaje;
            while (conectado && (mensaje = in.readLine()) != null) {

                // Desconexión del cliente
                if (mensaje.equalsIgnoreCase("ADIOS")) {
                    out.println("Te has desconectado del servidor.");
                    conectado = false;
                    System.out.println(this.getNick() + "se ha desconectado del servidor");
                    break;
                }

                // Mensaje privado
                if (mensaje.startsWith("/privado")) {
                    procesarMensajePrivado(mensaje);
                } else {
                    // Mensaje público
                    contadorMensajes++;
                    Servidor.actualizarSpammer(nick, contadorMensajes);
                    Servidor.enviarATodos("[" + nick + "]: " + mensaje, this);
                }
            }

        } catch (Exception e) {
            System.out.println("Se ha desconectado el cliente " + nick);
        } finally {
            desconectar();
        }
    }

    public void procesarMensajePrivado(String mensaje) {
        try {
            String[] mensajeSeparado = mensaje.split(" ");
            ClientHandler destino;
            String mensajeJunto = "";
            if ((destino = Servidor.buscarClientePorNick(mensajeSeparado[1])) != null){
                for (int i = 2; i < mensajeSeparado.length; i++) {
                    mensajeJunto += mensajeSeparado[i] + " ";
                }
                destino.enviarMensaje("Mensaje privado de " + this.getNick() + " :" + mensajeJunto);
                enviarMensaje("Mensaje privado enviado correctamente a " + destino.getNick());
            }
            // Formatear mensaje + buscar edstinatario + enviar mensaje destinatario

        } catch (Exception e) {
            out.println("Error al enviar mensaje privado.");
        }
    }

    public void enviarMensaje(String mensaje) {
            out.println(mensaje);
    }

    public void cerrarConexionDesdeServidor() {
        try {
            conectado = false;
            out.println("Servidor cerrado.");
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión del cliente " + nick);
        }
    }

    public void desconectar() {
        try {
            conectado = false;

            Servidor.removeCliente(this);

            if (nick != null) {
                Servidor.enviarATodos("[" + nick + "] se ha desconectado", null);
            }

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }

        } catch (Exception e) {
            System.out.println("Error al desconectar cliente.");
        }
    }

    public String getNick() {
        return nick;
    }

    public int getContadorMensajes() {
        return contadorMensajes;
    }
}
