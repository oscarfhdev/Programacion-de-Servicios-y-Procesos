package Ejercicio1;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Servidor arrancado y esperando...");

        try (
            ServerSocket server = new ServerSocket(1234); // Creamos el servidor en el puerto 1234
            Socket cliente = server.accept(); // Pausa esperando a que se conecte un cliente
            BufferedReader in = new BufferedReader( // Instancia para leer
                    new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter( // Instancia para enviar
                    cliente.getOutputStream(), true);
        ){
            String operacion;

            // Bucle para leer órdenes mientras no reciba un null ni mande EXIT
            while((operacion = in.readLine()) != null && !operacion.equals("EXIT")){
                String cadena = null;

                // Comprobamos qué operación quiere hacer
                switch (operacion){
                    case "CIFRAR":
                    case "DESCIFRAR":
                        out.println("Pasame la cadena"); // Pedimos la cadena
                        cadena = in.readLine(); // Leemos la cadena del cliente

                        // Ciframos o desciframos y le mandamos el resultado directamente
                        out.println(cifrar_descifrar(cadena, operacion, 3));
                        break;

                    default:
                        // Si escribe un comando inventado
                        out.println("Operación no permitida");
                }
            }

            // Si sale del bucle lo mandamos
            out.println("ADIOS");

        } catch (IOException e){
            System.err.println("Error de conexión");
        }
    }

    public static String cifrar_descifrar(String cadena,String operacion,int desplazamiento){
        int rango=('z'-'a')+1;

        String resultado="";
        char c;
        switch (operacion){
            case "CIFRAR":

                for (int i=0;i<cadena.length();i++){
                    c=cadena.charAt(i);

                    if (c>='a' && c<='z'){
                        resultado+= (char) (((c-'a')+desplazamiento)%rango + 'a');
                    }else if (c>='A' && c<='Z'){
                        resultado+= (char) (((c-'A')+desplazamiento)%rango + 'A');
                    }else{
                        resultado+=c;
                    }
                }
            break;
            case "DESCIFRAR":
                for (int i=0;i<cadena.length();i++){
                    c=cadena.charAt(i);

                    if (c>='a' && c<='z'){
                        resultado+= (char) (((c-'a')-desplazamiento+rango)%rango + 'a');
                    }else if (c>='A' && c<='Z'){
                        resultado+= (char) (((c-'A')-desplazamiento+rango)%rango + 'A');
                    }else{
                        resultado+=c;
                    }
                }
            break;
        }
        return resultado;
    }
}