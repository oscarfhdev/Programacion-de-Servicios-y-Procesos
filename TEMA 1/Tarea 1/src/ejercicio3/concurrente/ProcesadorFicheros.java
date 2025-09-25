package ejercicio3.concurrente;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ProcesadorFicheros extends Thread{

    private final String ruta;
    int contador = 0;
    int contadorInfo = 0;
    int contadorError = 0;
    int contadorWarn = 0;

    public ProcesadorFicheros(String ruta) {
        this.ruta = ruta;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.ruta));
            String linea;
            while ((linea = br.readLine()) != null) {
                this.contador++;
                String[] partes = linea.split(";");
                if (partes[0].equals("INFO")) this.contadorInfo++;
                else if (partes[0].equals("WARN")) this.contadorWarn++;
                else if (partes[0].equals("ERROR")) this.contadorError++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(this.getName() + " Número total de registros: " + contador); // Podemos también sumar todos los contadores :)
        System.out.println(this.getName() + "INFO = " + contadorInfo);
        System.out.println(this.getName() +"WARN = " + contadorWarn);
        System.out.println(this.getName() + "ERROR = " + contadorError);
    }
}
