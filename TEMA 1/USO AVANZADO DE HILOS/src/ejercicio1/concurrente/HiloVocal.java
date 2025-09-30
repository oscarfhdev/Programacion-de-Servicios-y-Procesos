package ejercicio1.concurrente;

import java.io.*;

public class HiloVocal extends Thread{
    String ficheroLeer;
    String ficheroEscribir;

    char vocalBuscada;
    char vocalBuscadaTilde;
    char uConDieresis;

    public HiloVocal(String ficheroLeer, String ficheroEscribir, char vocalBuscada, char vocalBuscadaTilde) {
        this.ficheroLeer = ficheroLeer;
        this.ficheroEscribir = ficheroEscribir;
        this.vocalBuscada = vocalBuscada;
        this.vocalBuscadaTilde = vocalBuscadaTilde;
        if (this.vocalBuscada == 'u') this.uConDieresis = 'ü';
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
                        char letraMinusculas = Character.toLowerCase(palabraSeparada[j]);
                        // Comparamos la palabra con las vocales, contemplando tildes y minúsculas
                        if (letraMinusculas == this.vocalBuscada || letraMinusculas == this.vocalBuscadaTilde) contador++;
                        if (this.uConDieresis == 'ü' && letraMinusculas == uConDieresis) contador++;
                    }
                }
            }

            br.close();

            BufferedWriter bw = new BufferedWriter(new FileWriter(this.ficheroEscribir));
            bw.write("Total de apariciones '" + this.vocalBuscada + "': " + contador);
            bw.close();
            System.out.println(this.getName() + " Escrito en " + this.ficheroEscribir + " correctamente");


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
