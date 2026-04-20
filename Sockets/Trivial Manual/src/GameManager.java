import java.util.ArrayList;

public class GameManager {
    private ArrayList<ClienteHandler> clientes = new ArrayList<>();
    private static boolean rondaAbierta = false;
    private ArrayList<Pregunta> preguntas = new ArrayList<>();


    public GameManager(ArrayList<ClienteHandler> clientes) {
        this.clientes = clientes;
        this.preguntas.add(new Pregunta("¿Qué palabra clave se usa en Java para heredar de una clase?", "implements", "extends", "inherits", "super", "b"));
        this.preguntas.add(new Pregunta("¿Qué comando SQL se usa para obtener datos de una tabla?", "INSERT", "UPDATE", "SELECT", "DELETE", "c"));
        this.preguntas.add(new Pregunta("¿Qué protocolo de la capa de transporte es orientado a conexión?", "UDP", "ICMP", "TCP", "ARP", "c"));
        this.preguntas.add(new Pregunta("¿Qué patrón de diseño asegura que solo exista una instancia de una clase?", "Factory", "Observer", "Adapter", "Singleton", "d"));
        this.preguntas.add(new Pregunta("¿Qué tipo de dato en Java almacena texto?", "int", "boolean", "String", "double", "c"));
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
        return p.getEnunciado() + " | A: " + p.getRespuestaA()
        + " | B: " + p.getRespuestaB()
        + " | C: " + p.getRespuestaC()
        + " | D: " + p.getRespuestaD();
    }

    public void enviarTodos(String msg){
        for (ClienteHandler clH : clientes){
            clH.enviarMensaje(msg);
        }
    }

}

