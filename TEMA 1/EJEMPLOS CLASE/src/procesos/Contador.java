package procesos;

public class Contador extends Thread{
    private String nombre;

    public Contador(String nombre) {
        this.nombre = nombre;
    }

    public void secuencial_run(){
        for (int i = 1; i <= 5; i++){
            System.out.println(this.nombre + " cuenta: " + i);
            try {
                Thread.sleep(1000); // Simula trabajo de 1 segundo
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(nombre + " terminó");
    }
    @Override
    public void run(){
        for (int i = 1; i <= 5; i++){
            System.out.println(this.nombre + " cuenta: " + i);
            try {
                Thread.sleep(1000); // Simula trabajo de 1 segundo
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.out.println(nombre + " terminó");
    }
}
