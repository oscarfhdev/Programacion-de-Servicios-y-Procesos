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
            System.out.println("Partida iniciada");
            for (Pregunta p : preguntas) {
                enviarTodos(extraerPregunta(p));
                rondaAbierta = true;
                Thread.sleep(15000);
                rondaAbierta = false;
                corregirRespuestas(p.getRespuestaCorrecta());
                limpiarRespuesta();
            }
            // Se finaliza la partida
            mostrarRanking();
        }
        catch (Exception e){
            System.out.println("Error al inicar la partida" + e.getMessage());
        }
    }

    private void mostrarRanking() {
        String total = "";
        for (ClienteHandler cl : clientes){
            total += cl.mostrarNota();
        }
        System.out.println(total);
    }

    public void limpiarRespuesta(){
        for (ClienteHandler cl : clientes){
            cl.limpiarRespuesta();
        }
    }


    public void corregirRespuestas(String solucion){
        // actualiza el campo de cliente handler
        // para los que tengn la respuesta correcta
        for (ClienteHandler cl : clientes){
            cl.corregirRespuesta(solucion);
        }
    }

    public String extraerPregunta(Pregunta p){
        return p.getEnunciado()+ " | A" + p.getRespuestaA()
        + " | B:" + p.getRespuestaB()
        + " | C:" + p.getRespuestaC()
        + " | D:" + p.getRespuestaD();
    }

    public void enviarTodos(String msg){
        for (ClienteHandler clH : clientes){
            clH.enviarMensaje(msg);
        }
    }

}

