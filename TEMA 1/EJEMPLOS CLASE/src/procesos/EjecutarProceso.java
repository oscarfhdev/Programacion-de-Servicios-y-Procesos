package procesos;

import java.io.IOException;

public class EjecutarProceso {

    private static final String NOMBRE_APP = "virtualbox";
    public static void main(String[] args) {
        //ejemplo1();
        //ejemplo2();
        ejemplo3();
    }

    private static void ejemplo1() {
        try {
            ProcessBuilder pb = new ProcessBuilder(NOMBRE_APP);
            Process proceso = pb.start();
            System.out.println("Iniciando " + NOMBRE_APP);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void ejemplo2(){
        try {
            ProcessBuilder pb = new ProcessBuilder(NOMBRE_APP);
            Process proceso = pb.start();
            System.out.println("Se ha iniciado " + NOMBRE_APP + ", cierrra la ventana para continuar");


            int exitCode = proceso.waitFor();
            System.out.println(NOMBRE_APP + " se cerró, con el código: " + exitCode);
            System.out.println("Se cerró " + NOMBRE_APP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ejemplo3(){
        try {
            System.out.println("Ejecución secuencial");
            long inicioSec = System.currentTimeMillis();

            new Contador("Contador 1").secuencial_run();
            new Contador("Contador 2").secuencial_run();

            long finSec = System.currentTimeMillis();
            System.out.println("Tiempo total secuencial: " + (finSec-inicioSec) / 1000.0 + " segundos\n");

            System.out.println("Ejecucion concurrente con hilos");
            long inicioCon = System.currentTimeMillis();

            Contador h1 = new Contador("Contador Concurrente 1");
            Contador h2 = new Contador("Contador Concurrente 2");

            h1.start();
            h2.start();

            h1.join();
            h2.join();

            long finCon = System.currentTimeMillis();
            System.out.println("Tiempo total concurrente: " + (finCon-inicioCon) / 1000.0 + " segundos\n");


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
