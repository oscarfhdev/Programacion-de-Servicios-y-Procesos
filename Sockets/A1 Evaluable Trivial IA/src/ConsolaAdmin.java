import java.util.Scanner;

public class ConsolaAdmin extends Thread {
    @Override
    public void run() {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Escribe START para comenzar la partida");

        while (true) {
            String comando = teclado.nextLine();
            if (comando.equalsIgnoreCase("START")) {
                if (Servidor.getNumJugadores() == 0) {
                    System.out.println("No hay jugadores conectados. Espera a que se conecte alguien.");
                } else {
                    Servidor.iniciarJuego();
                    break;
                }
            }
        }
        teclado.close();
    }
}
