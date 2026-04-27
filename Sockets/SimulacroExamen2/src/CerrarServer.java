import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class CerrarServer extends Thread{
    private Scanner scannerServidor;
    private ServerSocket socketServidor;

    public CerrarServer(Scanner scannerServidor, ServerSocket socketServidor) {
        this.scannerServidor = scannerServidor;
        this.socketServidor = socketServidor;
    }

    @Override
    public void run() {
        while (true){
            if (scannerServidor.nextLine().equals("EXIT")){
                System.out.println("Apagando servidor...");
                Servidor.encendido = false;

                try {
                    ClienteHandler spammer = MessageManager.mostrarSpammer();
                    if (spammer != null){
                        System.out.println("El usuario spammer fue " + spammer.nombreCliente + " con " + spammer.numeroMensajes + " mensajes");
                    }
                    else{
                        System.out.println("No se ha encontrado al usuario spammer");
                    }
                    socketServidor.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
