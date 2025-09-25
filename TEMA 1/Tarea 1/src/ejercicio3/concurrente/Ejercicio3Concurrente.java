package ejercicio3.concurrente;

public class Ejercicio3Concurrente {
    public static void main(String[] args) {
        String[] rutasFicheros = {
                "Tarea 1/src/ejercicio3/concurrente/log.txt",
                "Tarea 1/src/ejercicio3/concurrente/log2.txt",
                "Tarea 1/src/ejercicio3/concurrente/log3.txt"
        };

        ProcesadorFicheros[] hilos = new ProcesadorFicheros[rutasFicheros.length];
        for (int i = 0; i < rutasFicheros.length; i++) {
            hilos[i] = new ProcesadorFicheros(rutasFicheros[i]);
            hilos[i].start();
        }
        try {
            for (ProcesadorFicheros h : hilos) {
                h.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
