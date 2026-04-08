package Ejercicio1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("--- Iniciar conexion con el servidor ---");
        System.out.println("--- 1 : SI ");
        System.out.println("--- 2 : NO ");
        System.out.print("--- : ");
        int op = sc.nextInt();
        switch (op){
            case 1:
                try(Socket socket = new Socket("localhost",1234);
                    PrintWriter out = new PrintWriter(
                            socket.getOutputStream(),true
                    );
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                ){

                    System.out.println("Conexion correcta ... ");

                    String opMenu = "";
                    while(!opMenu.equals("Q")){
                        System.out.println("--- CIFRAR : Cifrado ");
                        System.out.println("--- DESCIFRAR : Descifrado ");
                        System.out.println("--- Q : Terminar Conexion ");
                        System.out.print("Inserta la opcion --- : ");
                        sc.nextLine();
                        out.println(sc.nextLine());

                        if(in.readLine().equals("y")){
                            System.out.print("Inserta la cadena --- : ");
                            opMenu = sc.nextLine();
                            out.println(opMenu);
                        } else {
                            socket.close();
                            break;
                        }

                        String cadenaTrabajada = in.readLine();
                        if(cadenaTrabajada != null){
                            System.out.println("Cadena: " + cadenaTrabajada);
                        }
                    }

                    if(opMenu.equals("Q")){
                        out.println("Q");
                    }

                }catch (Exception e){

                }
                break;
            case 2:
                System.out.println("Saliendo del programa ...");
        }
    }
}