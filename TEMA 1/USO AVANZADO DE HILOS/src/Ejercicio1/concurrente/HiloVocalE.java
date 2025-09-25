package Ejercicio1.concurrente;

import java.io.*;

public class HiloVocalE extends Thread{
    String ficheroLeer;
    String ficheroEscribir;

    public HiloVocalE(String ficheroLeer, String ficheroEscribir) {
        this.ficheroLeer = ficheroLeer;
        this.ficheroEscribir = ficheroEscribir;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.ficheroLeer));
            String linea;
            int contador = 0;
            while ((linea = br.readLine()) != null){
                String[] lineaSeparada = linea.split("\\s");
                //Recorremos el array de linea separada para separar las palabras en letras
                for (int i = 0; i < lineaSeparada.length; i++) {
                    // Con toCharArray devolvemos una palabra como un array de Char
                    char[] palabraSeparada = lineaSeparada[i].toCharArray();

                    // Ahora recorremos cada letra de la palabra
                    for (int j = 0; j < palabraSeparada.length; j++) {
                        char palabraMinusculas = Character.toLowerCase(palabraSeparada[j]);
                        // Comparamos la palabra con las vocales, contemplando tildes y minúsculas
                        if (palabraMinusculas == 'e' || palabraMinusculas == 'e') contador++;
                    }
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(this.ficheroEscribir));
            bw.write("Total de apariciones 'e': " + contador);
            bw.close();
            System.out.println(this.getName() + " Escrito en " + ficheroEscribir + " correctamente");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
