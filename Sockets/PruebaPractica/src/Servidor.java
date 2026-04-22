import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

    private static final int PUERTO = 5000;

    // Lista de clientes conectados
    public static ArrayList<ClientHandler> clientes = new ArrayList<>();

    // Control de estado del servidor
    private static boolean activo = true;

    // Para poder cerrar el server socket desde otro hilo
    private static ServerSocket serverSocket;

    // Datos del spammer final
    private static String nickSpammer = "";
    private static int maxMensajes = 0;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            // Hilo que escucha FIN en la consola del servidor
            ReceptorFinServidor receptorFin = new ReceptorFinServidor();
            receptorFin.start();

            while (activo) {
                try {
                    Socket socketCliente = serverSocket.accept();

                    if (!activo) {
                        socketCliente.close();
                        break;
                    }

                    ClientHandler cliente = new ClientHandler(socketCliente);
                    cliente.start();
                    addCliente(cliente);

                } catch (Exception e) {
                    if (activo) {
                        System.out.println("Error al aceptar cliente: " + e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error al iniciar el servidor: " + e.getMessage());
        } finally {
            cerrarServidor();
        }
    }

    // Añadir cliente a la lista compartida
    public static synchronized void addCliente(ClientHandler cliente) {
        clientes.add(cliente);
    }

    // Eliminar cliente de la lista compartida
    public static synchronized void removeCliente(ClientHandler cliente) {
        clientes.remove(cliente);
    }

    // Enviar mensaje a todos los clientes
    public static synchronized void enviarATodos(String mensaje, ClientHandler remitente) {

        for (ClientHandler cl: clientes){
            if (remitente == null){
                cl.enviarMensaje(mensaje);
                return;
            }
            else if (mensaje != null && !remitente.getNick().equals(cl.getNick()))
                cl.enviarMensaje(mensaje);
        }

    }

    // Buscar cliente por nick
    public static synchronized ClientHandler buscarClientePorNick(String nick) {
        for (ClientHandler clientHandler: clientes){
            if (clientHandler.getNick().equals(nick)){
                return clientHandler;
            }
        }
        return null;
    }

    // Actualizar el récord del spammer (Revisar los atributos de la clase arriba para saber que actualizar)
    public static synchronized void actualizarSpammer(String nick, int numMensajes) {
        if (numMensajes > maxMensajes){
            maxMensajes = numMensajes;
            nickSpammer = nick;
        }
    }

    // Apagar servidor al escribir FIN
    public static synchronized void apagarServidor() {
        if (!activo) {
            return;
        }

        activo = false;
        System.out.println("Apagando servidor...");

        enviarATodos("Servidor finalizado por el administrador.", null);

        // Cerrar conexiones cliente
        for (int i = 0; i < clientes.size(); i++) {
            clientes.get(i).cerrarConexionDesdeServidor();
        }

        // Cerrar server socket para desbloquear accept()
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar el ServerSocket.");
        }
    }

    // Cierre final del servidor
    public static synchronized void cerrarServidor() {
        try {
            activo = false;

            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }

        } catch (Exception e) {
            System.out.println("Error cerrando servidor.");
        }

        mostrarSpammerFinal();
        System.out.println("Servidor cerrado.");
    }

    public static synchronized boolean isActivo() {
        return activo;
    }

    public static synchronized void mostrarSpammerFinal() {
        if (maxMensajes == 0) {
            System.out.println("No se han enviado mensajes.");
        } else {
            System.out.println("Spammer final: " + nickSpammer + " con " + maxMensajes + " mensajes.");
        }
    }
}