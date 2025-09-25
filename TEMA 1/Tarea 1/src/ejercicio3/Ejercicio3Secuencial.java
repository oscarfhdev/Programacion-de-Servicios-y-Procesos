package ejercicio3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio3Secuencial {
    public static void main(String[] args) {
        System.out.println("Info del fichero");
        try {
            BufferedReader br = new BufferedReader(new FileReader("Tarea 1/src/ejercicio3/log.txt"));
            String linea;
            int contador = 0;
            int contadorInfo = 0;
            int contadorError = 0;
            int contadorWarn = 0;
            while ((linea = br.readLine()) != null){
                contador++;
                String[] lineaSeparada = linea.split(";");
                if (lineaSeparada[0].equals("INFO")){
                    contadorInfo++;
                }
                else if (lineaSeparada[0].equals("ERROR")){
                    contadorError++;
                }
                else if (lineaSeparada[0].equals("WARN")){
                    contadorWarn++;
                }
            }

            br.close();

            System.out.println("Número total de registros: " + contador); // Podemos también sumar todos los contadores :)
            System.out.println("INFO = " + contadorInfo);
            System.out.println("WARN = " + contadorWarn);
            System.out.println("ERROR = " + contadorError);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
