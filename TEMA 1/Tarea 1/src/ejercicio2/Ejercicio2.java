package ejercicio2;

public class Ejercicio2{
    public static void main(String[] args) {

        HiloA hiloA = new HiloA();
        HiloB hiloB = new HiloB();
        HiloC hiloC = new HiloC();

        hiloA.start();
        hiloB.start();
        hiloC.start();

        try {
            hiloA.join();
            hiloB.join();
            hiloC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Todas las taras finalizadas");
    }
}
