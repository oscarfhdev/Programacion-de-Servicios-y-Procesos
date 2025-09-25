package ejercicio2;

public class HiloA extends Thread{

    @Override
    public void run() {
        for (int i = 0; i < 3 ; i++) {
            System.out.println("Descargando datos...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
