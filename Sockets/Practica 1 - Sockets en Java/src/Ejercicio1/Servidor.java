package Ejercicio1;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        System.out.println("Servidor arrancado y esperando...");

        try (
            ServerSocket server = new ServerSocket(1234);
            Socket cliente = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
        ) {
            System.out.println("Cliente conectado");

            // Variables para el inicio de sesión
            int intentos = 3;
            boolean loginCorrecto = false;

            // Bucle de login
            while (intentos > 0 && !loginCorrecto) {
                out.println("Usuario:");
                String usuarioStr = in.readLine();
                
                out.println("Contraseña:");
                String passStr = in.readLine();

                // Validamos credenciales (el usuario es admin y la pass 1234)
                if (usuarioStr.equals("admin") && passStr.equals("1234")) {
                    loginCorrecto = true;
                    out.println("LOGIN_OK");
                } else {
                    intentos--;
                    if (intentos > 0) {
                        out.println("Credenciales incorrectas. Te quedan " + intentos + " intentos.");
                    } else {
                        out.println("LOGIN_FAIL");
                    }
                }
            }

            // Si ha salido del bucle y no está logueado, cerramos
            if (!loginCorrecto) {
                System.out.println("Demasiados intentos fallidos. Cierra conexión.");
                return;
            }

            // Avisamos al cliente del menú
            out.println("MOSTRAMOS_MENU");

            String opcion;
            while ((opcion = in.readLine()) != null && !opcion.equals("5")) {
                switch (opcion) {
                    case "1":
                        out.println("Introduce primer número:");
                        int n1 = Integer.parseInt(in.readLine());
                        out.println("Introduce segundo número:");
                        int n2 = Integer.parseInt(in.readLine());
                        out.println("Resultado: " + (n1 + n2));
                        break;
                    
                    case "2":
                        out.println("Introduce el texto:");
                        String texto = in.readLine();
                        texto = texto.toLowerCase();
                        int vocales = 0;
                        for (int i = 0; i < texto.length(); i++) {
                            char c = texto.charAt(i);
                            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                                vocales++;
                            }
                        }
                        out.println("El texto tiene " + vocales + " vocales");
                        break;

                    case "3":
                        out.println("Introduce la cadena:");
                        String cadena = in.readLine();
                        String invertida = "";
                        for (int i = cadena.length() - 1; i >= 0; i--) {
                            invertida += cadena.charAt(i);
                        }
                        out.println("Cadena invertida: " + invertida);
                        break;

                    case "4":
                        out.println("Introduce un número entero:");
                        int num = Integer.parseInt(in.readLine());
                        boolean esPrimo = true;
                        if (num <= 1) {
                            esPrimo = false;
                        } else {
                            for (int i = 2; i < num; i++) {
                                if (num % i == 0) {
                                    esPrimo = false;
                                    break;
                                }
                            }
                        }
                        if (esPrimo) {
                            out.println("El número " + num + " es primo");
                        } else {
                            out.println("El número " + num + " NO es primo");
                        }
                        break;

                    default:
                        out.println("Operación no permitida");
                }
            }

            out.println("ADIOS");

        } catch (IOException e) {
            System.err.println("Error de conexión");
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear número, conexión cerrada.");
        }
    }
}
