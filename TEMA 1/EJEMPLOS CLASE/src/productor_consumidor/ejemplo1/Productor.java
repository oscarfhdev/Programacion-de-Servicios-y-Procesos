package productor_consumidor.ejemplo1;

public class Productor extends Thread{
    private Compartido recurso;

    public Productor(Compartido recurso) {
        this.recurso = recurso;
    }

    @Override
    public void run(){
        for (int i = 1; i <= 5; i++) {
            recurso.setValor(i);
            System.out.println("Productor escribio: " + recurso.getValor());
            try {
                Productor.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
