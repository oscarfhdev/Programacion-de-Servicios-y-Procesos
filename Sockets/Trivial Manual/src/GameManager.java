import java.util.ArrayList;

public class GameManager {
    private ArrayList<ClienteHandler> clientes = new ArrayList<>();
    private static boolean rondaAbierta = false;
    private ArrayList<Pregunta> preguntas = new ArrayList<>();


    public GameManager(ArrayList<ClienteHandler> clientes) {
        this.clientes = clientes;
        this.preguntas.add((new Pregunta("Enunciado 1", "1", "2", "3", "4", "b")));
        this.preguntas.add((new Pregunta("Enunciado 2", "1", "2", "3", "4", "c")));
    }

    public static boolean isRondaAbierta() {
        return rondaAbierta;
    }

    public void setRondaAbierta(boolean rondaAbierta) {
        this.rondaAbierta = rondaAbierta;
    }

    public void iniciarPartida() throws InterruptedException {
        try {

            for (Pregunta p : preguntas) {
                enviarTodos(p.getEnunciado());
                rondaAbierta = true;
                Thread.sleep(15000);
                rondaAbierta = false;
                corregirRespuestas(p.getRespuestaCorrecta());

            }
        }
        catch (Exception e){
            System.out.println("Error al inicar la partida");
        }
    }

    public void enviarTodos(String msg){
    }

    public void lanzarPregunta(String pregunta){

    }

    public void corregirRespuestas(String solucion){
        // actualiza el campo de cliente handler
        // para los que tengn la respuesta correcta
    }
}

