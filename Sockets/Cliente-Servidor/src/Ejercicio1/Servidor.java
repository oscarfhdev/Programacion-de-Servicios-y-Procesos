package Ejercicio1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(1234);
             Socket socket = server.accept();
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String op = in.readLine();
            System.out.println(op);

            if (op != null && !op.equals("") && !op.equals("Q")) {
                out.println("y");
            } else if (op.equals("Q")) {
                socket.close();
            }

            String cadena = in.readLine();
            switch (op) {
                case "CIFRAR":
                    cadena = cifrar(cadena);
                    break;
                case "DESCIFRAR":
                    cadena = desCrifrar(cadena);
                    break;
                default:
                    break;
            }

            if (cadena != null) {
                out.println(cadena);
            }

        } catch (Exception e) {

        }
    }

    public static String cifrar(String cadena) {
        String cadenaCifrada = "";
        int rangoA_Z = ('z' - 'a') + 1;

        for (char c : cadena.toCharArray()) {

            if (c >= 'a' && c <= 'z') {

                int pos = (((c - 'a') + 3) + rangoA_Z) % rangoA_Z;
                cadenaCifrada += (char) ('a' + pos);

            } else if (c >= 'A' && c <= 'Z') {

                int pos = (((c - 'A') + 3) + rangoA_Z) % rangoA_Z;
                cadenaCifrada += (char) ('A' + pos);

            }

        }

        return cadenaCifrada;
    }

    public static String desCrifrar(String cadena) {
        String cadenaDescifrada = "";
        int rangoA_Z = ('z' - 'a') + 1;

        for (char c : cadena.toCharArray()) {

            if (c >= 'a' && c <= 'z') {

                int pos = (((c - 'a') - 3) + rangoA_Z) % rangoA_Z;
                cadenaDescifrada += (char) ('a' + pos);

            } else if (c >= 'A' && c <= 'Z') {

                int pos = (((c - 'A') - 3) + rangoA_Z) % rangoA_Z;
                cadenaDescifrada += (char) ('A' + pos);

            }

        }

        return cadenaDescifrada;
    }
}