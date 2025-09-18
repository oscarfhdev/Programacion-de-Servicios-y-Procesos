public class Tarea extends Thread {
    @Override
    public void run(){
        System.out.println("Ejecutando hilo: " + getName());
    }
}
