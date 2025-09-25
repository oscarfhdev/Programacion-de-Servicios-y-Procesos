package ejercicio2;

public class HiloC extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 5 ; i++) {
            System.out.println("Guardando...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
