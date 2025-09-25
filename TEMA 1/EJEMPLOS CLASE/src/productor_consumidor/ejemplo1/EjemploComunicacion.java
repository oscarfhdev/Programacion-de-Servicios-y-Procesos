package productor_consumidor.ejemplo1;

public class EjemploComunicacion{
    public static void main(String[] args) {
    Compartido recurso = new Compartido();


    Productor p = new Productor(recurso);
    Consumidor c = new Consumidor(recurso);

    p.start();
    c.start();
    }
}
