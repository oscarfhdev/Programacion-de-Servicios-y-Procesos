package productor_consumidor.ejemplo2;

public class Recurso {
    private int valor;
    private boolean disponible = false;

    // Método para que el productor escriba un valor
    public synchronized void producir(int nuevoValor){
        while (disponible) { // Si ya hay datos esperando, el productor espera
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
            valor = nuevoValor;
            disponible = true;
            System.out.println("Productor produjo: " + valor);
            notify(); // Avisa al productor
    }

    // Método para que el consumidor lea un valor
    public synchronized int consumir(){
        while (!disponible){ // Si no hay dato aún el consumidor espera
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        disponible = false;
        System.out.println("Consumidor consumió: " + valor);
        notify();
        return valor;
    }
}
