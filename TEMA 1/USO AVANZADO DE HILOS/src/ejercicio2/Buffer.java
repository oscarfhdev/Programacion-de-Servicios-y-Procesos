package ejercicio2;

import java.util.LinkedList;

public class Buffer {
    private int tamanoBuffer = 3;
    private LinkedList<Integer> bufferNumeros;
    private int contador;

    public Buffer() {
        this.bufferNumeros = new LinkedList<>();
        this.contador = 0;
    }

    public synchronized void producir(){
        while (bufferNumeros.size() >= tamanoBuffer){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        bufferNumeros.addLast(contador++);
        System.out.println(Thread.currentThread().getName() + " produce: " + bufferNumeros.getLast());
        notify();
   }

   public synchronized void consumir(){
        while (bufferNumeros.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
       System.out.println(Thread.currentThread().getName() + " consume: " + bufferNumeros.getFirst());
        bufferNumeros.removeFirst();
       notifyAll();
    }
}
