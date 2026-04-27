import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class MessageManager {

    public static void enviarMensajeTodos(String msg, ClienteHandler remitente) {
        for (ClienteHandler clienteHandler : Servidor.clientes) {
            if (clienteHandler.nombreCliente != null && clienteHandler != remitente) {
                clienteHandler.enviarMensaje(msg);
            }
        }
    }

    public static ClienteHandler mostrarSpammer() {
        ClienteHandler clienteSpammer = null;
        int max = 0;
        for (ClienteHandler clienteHandler : Servidor.clientes) {
            if (clienteHandler.numeroMensajes != null) {
                if (max < clienteHandler.numeroMensajes) {
                    max = clienteHandler.numeroMensajes;
                    clienteSpammer = clienteHandler;
                }
            }
        }
        return clienteSpammer;
    }

    public static void enviarPrivado(String mensaje, ClienteHandler remitente) {
        String[] mensajeSeparado = mensaje.split(" ");
        ClienteHandler destinatario;
        if ((destinatario = obtenerUsuario(mensajeSeparado[1], remitente)) != null) {
            String mensajeCompleto = "";
            for (int i = 2; i < mensajeSeparado.length; i++) {
                mensajeCompleto += mensajeSeparado[i] + " ";
            }
            destinatario.enviarMensaje("Mensaje privado de " + "[" + remitente.nombreCliente + "] : " + mensajeCompleto);
        }
        remitente.enviarMensaje("Mensaje enviado correctamente a " + destinatario.nombreCliente);
    }


    private static ClienteHandler obtenerUsuario(String nick, ClienteHandler remitente){
        if (nick == null){
            remitente.enviarMensaje("El formato del mensaje es incorrecto");
        }
        for (ClienteHandler clienteHandler : Servidor.clientes){
            if (clienteHandler.nombreCliente != null && clienteHandler != remitente){
                if (clienteHandler.nombreCliente.equals(nick)){
                    return clienteHandler;
                }
            }
        }
        return null;
    }

    }
