package ejercicio1;

import java.io.IOException;

public class Ejercicio1 {
    public static void main(String[] args) {
        try {
            String app = "firefox";
            ProcessBuilder pb = new ProcessBuilder(app);
            Process proceso = pb.start();

            System.out.println("Se ha iniciado " + app);

            int codigoSalida = proceso.waitFor();

            System.out.println("La aplicación " + app + " se ha cerrado");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
