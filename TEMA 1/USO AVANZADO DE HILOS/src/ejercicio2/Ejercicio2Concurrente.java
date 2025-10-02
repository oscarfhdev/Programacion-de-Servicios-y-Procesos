package ejercicio2;

public class Ejercicio2Concurrente {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Thread productor = new Productor(buffer);
        Thread consumidor = new Consumidor(buffer);
        Thread productor2 = new Productor(buffer);
        Thread consumidor2 = new Consumidor(buffer);

        productor.setName("Productor 1");
        productor2.setName("Productor 2");

        consumidor.setName("Consumidor 1");
        consumidor2.setName("Consumidor 2");

        productor.start();
        productor2.start();

        consumidor.start();
        consumidor2.start();
    }
}
