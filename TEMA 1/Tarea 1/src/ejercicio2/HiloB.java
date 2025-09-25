package ejercicio2;

public class HiloB extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 2 ; i++) {
            System.out.println("Procesando...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
