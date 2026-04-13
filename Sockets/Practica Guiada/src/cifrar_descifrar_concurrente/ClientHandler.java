package cifrar_descifrar_concurrente;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket cliente;

    public ClientHandler(Socket socket) {
        this.cliente = socket; // Recibe el socket de la conexión
    }

    @Override
    public void run() {
        try(
            BufferedReader in = new BufferedReader( // Instancia para leer
                    new InputStreamReader(cliente.getInputStream()));
            PrintWriter out = new PrintWriter( // Instancia para enviar
                    cliente.getOutputStream(), true)){
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


        } catch (Exception e) { e.printStackTrace(); }
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