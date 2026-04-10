package Ejercicio2;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Servidor levantado y esperando...");
        try (
                ServerSocket serverSocket = new ServerSocket(1234);
                Socket cliente = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter out = new PrintWriter(cliente.getOutputStream(), true)
        ) {
            System.out.println("Cliente conectado");
            
            // Se guarda la lista al estar fuera del bucle
            List<Integer> numeros = new ArrayList<>();
            String mensaje;

            while ((mensaje = in.readLine()) != null && !mensaje.equals("6")) {
                
                // Dividimos el mensaje por si viene con un número
                String[] partes = mensaje.split(" ");
                String operacion = partes[0]; // El comando siempre es la primera parte

                switch (operacion) {
                    case "1":
                        // Si es 1, la segunda parte es el número a guardar
                        if (partes.length > 1) {
                            try {
                                int num = Integer.parseInt(partes[1]);
                                numeros.add(num);
                                out.println("Número " + num + " añadido correctamente.");
                            } catch (NumberFormatException e) {
                                out.println("Error: Formato de número incorrecto.");
                            }
                        } else {
                            out.println("Error: No has enviado el número junto a la opción.");
                        }
                        break;
                        
                    case "2":
                        out.println(numeros.isEmpty() ? "La lista está vacía." : "Lista: " + numeros);
                        break;
                        
                    case "3":
                        if (numeros.isEmpty()) {
                            out.println("No se puede calcular la media porque la lista está vacía.");
                        } else {
                            double total = 0;
                            for (double n : numeros) total += n;
                            out.println("Media de la lista: " + (total / numeros.size()));
                        }
                        break;
                        
                    case "4":
                        if (numeros.isEmpty()) {
                            out.println("No se puede calcular el máximo porque la lista está vacía.");
                        } else {
                            // Usamos Double.MIN_VALUE para buscar el mayor
                            int max = Integer.MIN_VALUE;
                            for (int n : numeros) {
                                if (n > max) max = n;
                            }
                            out.println("Máximo de la lista: " + max);
                        }
                        break;
                        
                    case "5":
                        numeros.clear();
                        out.println("Lista borrada correctamente.");
                        break;
                        
                    default:
                        out.println("Operación no válida, seleccione una opción correcta");
                }
            }
            out.println("Adiós!");

        } catch (IOException e) {
            System.err.println("Error de conexión");
        }
    }
}