package Ejercicio1;

import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
                System.out.println("Cliente conectado");

        try (
                Socket socket = new Socket("localhost", 1234);
                PrintWriter out = new PrintWriter(
                socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

                BufferedReader teclado = new BufferedReader(
                new InputStreamReader(System.in));
            ) {


                                        
        out.println(teclado.readLine());
        String respuesta=in.readLine();
        if (respuesta.equals("Pasame la cadena")){
            out.println(teclado.readLine());
            System.out.println("cifrado: "+in.readLine());
        }else{
            System.out.println(respuesta); 
        }

        // mandarle a Descifrar ABC
        out.println(teclado.readLine());
        respuesta=in.readLine();
        if (respuesta.equals("Pasame la cadena")){
            out.println(teclado.readLine());
            System.out.println("descifrado: "+in.readLine());
        }else{
            System.out.println(respuesta); 
        }

        out.println(teclado.readLine());

        System.out.println(in.readLine());


        
    }catch (IOException e){
        System.err.println("Error en conexión");
    }catch (ArithmeticException a){
            System.err.println("División sobre 0"+a.getMessage());
    }
}
}