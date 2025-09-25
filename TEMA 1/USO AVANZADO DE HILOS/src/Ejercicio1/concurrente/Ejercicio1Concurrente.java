package Ejercicio1.concurrente;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio1Concurrente {
    public static void main(String[] args) {

        String ficheroALeer = "USO AVANZADO DE HILOS/src/Ejercicio1/LoremIpsum.txt";

        // Así podemos acceder más facil
        String[] vocales = {"a", "e", "i", "o", "u"};

        // Guardamos en un array los ficheros que va a escribir cada hilo
        String[] ficherosVocalesEscribir = {
                "USO AVANZADO DE HILOS/src/Ejercicio1/concurrente/FicheroVocalA.txt",
                "USO AVANZADO DE HILOS/src/Ejercicio1/concurrente/FicheroVocalE.txt",
                "USO AVANZADO DE HILOS/src/Ejercicio1/concurrente/FicheroVocalI.txt",
                "USO AVANZADO DE HILOS/src/Ejercicio1/concurrente/FicheroVocalO.txt",
                "USO AVANZADO DE HILOS/src/Ejercicio1/concurrente/FicheroVocalU.txt"
        };
        // Creamos un array para guardar los hilos y trabajar maś sencillo
        ArrayList<Thread> hilosVocales = new ArrayList<>(5);

        // Creamos los hilos
        Thread hiloVocalA = new HiloVocalA(ficheroALeer, ficherosVocalesEscribir[0]);
        Thread hiloVocalE = new HiloVocalE(ficheroALeer, ficherosVocalesEscribir[1]);
        Thread hiloVocalI = new HiloVocalI(ficheroALeer, ficherosVocalesEscribir[2]);
        Thread hiloVocalO = new HiloVocalO(ficheroALeer, ficherosVocalesEscribir[3]);
        Thread hiloVocalU = new HiloVocalU(ficheroALeer, ficherosVocalesEscribir[4]);

        // Metemos los hilos en el array
        hilosVocales.add(hiloVocalA);
        hilosVocales.add(hiloVocalE);
        hilosVocales.add(hiloVocalI);
        hilosVocales.add(hiloVocalO);
        hilosVocales.add(hiloVocalU);

        // Iniciamos el proceso
        for( Thread hilo: hilosVocales){
            hilo.start();
        }

        // Lo unimos al proceso padre
        for( Thread hilo: hilosVocales){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        // Ahora el programa padre debe de recopilar los datos y hacer la suma

        System.out.println("\nDatos recopilados de los archivos: ");

        int[] contadoresObtenidosFicheros = new int[5];
        // También creamos suma para aprovechar la iteración del bucle y sumar
        int suma = 0;
        String linea;
        try {

            // Hacemos un bucle para leer los ficheros
            for (int i = 0; i < ficherosVocalesEscribir.length; i++) {
                // Creamos un buffer que se crea y se cierra en cada iteración
                BufferedReader br = new BufferedReader(new FileReader(ficherosVocalesEscribir[i]));
                while ((linea = br.readLine()) != null) {
                    // Leemos la linea y lo separamos por los :
                    String[] lineaSeparada = linea.split(":");
                    // Ahora guardamos la información del fichero en el array
                    contadoresObtenidosFicheros[i] = Integer.parseInt(lineaSeparada[1].trim());

                    suma += contadoresObtenidosFicheros[i];
                    // Imprimimos de manera eficiente con el array de vocales
                    System.out.println("La vocal " + vocales[i] + " aparece " + contadoresObtenidosFicheros[i] + " veces");
                }

                br.close();
            }

            System.out.println("Suma total de las vocales: " + suma);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
