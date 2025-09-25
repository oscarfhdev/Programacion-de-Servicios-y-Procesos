package productor_consumidor.ejemplo2;

public class EjemploComunicacion{
    public static void main(String[] args) {
        Recurso recurso = new Recurso();
        new ProductorCom(recurso).start();
        new ConsumidorCom(recurso).start();
    }
}
