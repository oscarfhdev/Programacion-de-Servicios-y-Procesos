package ejercicio1.secuencial;

import java.io.*;

public class Ejercicio1Secuencial {
    public static void main(String[] args) {
        String fichero = "USO AVANZADO DE HILOS/src/ejercicio1/LoremIpsum.txt";

        // Así podemos acceder más facil
        String[] vocales = {"a", "e", "i", "o", "u"};

        // Creamos arrays para recorrerlos y no tener que crear tantos buffers
        String[] ficherosVocales = {
        "USO AVANZADO DE HILOS/src/ejercicio1/secuencial/FicheroVocalA.txt",
        "USO AVANZADO DE HILOS/src/ejercicio1/secuencial/FicheroVocalE.txt",
        "USO AVANZADO DE HILOS/src/ejercicio1/secuencial/FicheroVocalI.txt",
        "USO AVANZADO DE HILOS/src/ejercicio1/secuencial/FicheroVocalO.txt",
        "USO AVANZADO DE HILOS/src/ejercicio1/secuencial/FicheroVocalU.txt"
        };

        // Nos ahorramos variables el orden es a, e, i, o, u
        int[] contadores = {0, 0, 0, 0, 0};

        try {
            BufferedReader br = new BufferedReader(new FileReader(fichero));
            String linea;
            while ((linea = br.readLine()) != null){
                // Dividimos la linea por espacios, podemos utilizar también " "
                String[] lineaSeparada = linea.split("\\s");

                //Recorremos el array de linea separada para separar las palabras en letras
                for (int i = 0; i < lineaSeparada.length; i++) {
                    // Con toCharArray devolvemos una palabra como un array de Char
                    char[] palabraSeparada = lineaSeparada[i].toCharArray();

                    // Ahora recorremos cada letra de la palabra
                    for (int j = 0; j < palabraSeparada.length; j++) {
                        // Comparamos la palabra con las vocales, contemplando tildes y minúsculas
                        if (esA(Character.toLowerCase(palabraSeparada[j]))) contadores[0]++;
                        else if (esE(Character.toLowerCase(palabraSeparada[j]))) contadores[1]++;
                        else if (esI(Character.toLowerCase(palabraSeparada[j]))) contadores[2]++;
                        else if (esO(Character.toLowerCase(palabraSeparada[j]))) contadores[3]++;
                        else if (esU(Character.toLowerCase(palabraSeparada[j]))) contadores[4]++;
                    }
                }
            }

            br.close();
            System.out.println("Fichero leído");

            // Escribimos la información del array de contadores
            for (int i = 0; i < ficherosVocales.length; i++) {
                // Creamos un buffer para cada iteración y lo cerramos para que se guarde el archivo
                BufferedWriter bw = new BufferedWriter(new FileWriter(ficherosVocales[i]));

                // Aprovechamos el bucle para escribir de manera más eficiente
                bw.write("Total de apariciones '" + vocales[i] + "': " + contadores[i]);
                bw.close();
            }

            System.out.println("Escrita la información en los ficheros");

            System.out.println("\nInformación recuperada de los ficheros: ");


            // Creamos un nuevo array para guardar los datos de los ficheros
            int[] contadoresObtenidosFicheros = new int[5];
            // También creamos suma para aprovechar la iteración del bucle y sumar
            int suma = 0;

            // Hacemos un bucle para leer los ficheros
            for (int i = 0; i < ficherosVocales.length; i++){
                // Creamos un buffer que se crea y se cierra en cada iteración
                br = new BufferedReader(new FileReader(ficherosVocales[i]));
                while ((linea = br.readLine()) != null){
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

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // Métodos para comprarar los caracteres
    private static boolean esU(char lineaSeparada) {
        return lineaSeparada == 'u' || lineaSeparada == 'ú' || lineaSeparada == 'ü';
    }

    private static boolean esO(char lineaSeparada) {
        return lineaSeparada == 'o' || lineaSeparada == 'ó';
    }

    private static boolean esI(char lineaSeparada) {
        return lineaSeparada =='i' || lineaSeparada == 'í';
    }

    private static boolean esE(char lineaSeparada) {
        return lineaSeparada== 'e' || lineaSeparada == 'é';
    }

    private static boolean esA(char lineaSeparada) {
        return lineaSeparada == 'a' || lineaSeparada == 'á';
    }
}
