package Ejercicio1.concurrente;

import java.io.*;

public class HiloVocalI extends Thread{
    String ficheroLeer;
    String ficheroEscribir;

    public HiloVocalI(String ficheroLeer, String ficheroEscribir) {
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
                        if (palabraMinusculas == 'i' || palabraMinusculas == 'í') contador++;
                    }
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(this.ficheroEscribir));
            bw.write("Total de apariciones 'i': " + contador);
            bw.close();
            System.out.println(this.getName() + " Escrito en " + ficheroEscribir + " correctamente");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
