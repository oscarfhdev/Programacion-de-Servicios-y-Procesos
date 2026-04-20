import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static List<ClientHandler> jugadores = new ArrayList<>();
    private static boolean juegoIniciado = false;

    public static void main(String[] args) {
        System.out.println("Servidor escuchando en el puerto 5000");
        System.out.println("Esperando jugadores...");

        // Hilo 2: ConsolaAdmin escucha la consola del servidor
        ConsolaAdmin consola = new ConsolaAdmin();
        consola.start();

        // Hilo 1 (main): acepta conexiones hasta 10 jugadores o hasta START
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            serverSocket.setSoTimeout(1000); // Comprobamos cada segundo si el juego ha empezado

            while (!juegoIniciado && jugadores.size() < 10) {
                try {
                    Socket cliente = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(cliente);
                    synchronized (jugadores) {
                        jugadores.add(handler);
                    }
                    handler.start();
                    System.out.println("Jugador conectado. Total: " + jugadores.size());
                } catch (SocketTimeoutException e) {
                    // No pasa nada, volvemos a comprobar el bucle
                }
            }

            serverSocket.close();

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    public static void iniciarJuego() {
        juegoIniciado = true;

        // Esperar un momento para que el accept() termine su ciclo
        try { Thread.sleep(1500); } catch (InterruptedException e) { e.printStackTrace(); }

        int numJugadores;
        synchronized (jugadores) {
            numJugadores = jugadores.size();
        }

        enviarATodos("INFO| Comienza la partida");
        enviarATodos("INFO| Número de jugadores: " + numJugadores);
        System.out.println("Partida iniciada con " + numJugadores + " jugadores");

        // Creamos las 5 preguntas
        Pregunta[] preguntas = new Pregunta[5];
        preguntas[0] = new Pregunta(
            "¿Qué clase se usa para crear un servidor TCP en Java?",
            "Socket", "ServerSocket", "DatagramSocket", "URLConnection", "b");
        preguntas[1] = new Pregunta(
            "¿Qué método de Thread se usa para arrancar un hilo?",
            "run()", "execute()", "start()", "begin()", "c");
        preguntas[2] = new Pregunta(
            "¿Qué significa TCP?",
            "Transfer Control Protocol", "Transmission Control Protocol",
            "Total Connection Protocol", "Transport Control Protocol", "b");
        preguntas[3] = new Pregunta(
            "¿Qué puerto está reservado para HTTP?",
            "21", "443", "80", "25", "c");
        preguntas[4] = new Pregunta(
            "¿Qué palabra clave evita el acceso simultáneo de dos hilos?",
            "volatile", "static", "final", "synchronized", "d");

        // Iteramos las 5 preguntas
        for (int i = 0; i < preguntas.length; i++) {
            Pregunta p = preguntas[i];

            // Reseteamos la respuesta de cada jugador
            synchronized (jugadores) {
                for (ClientHandler h : jugadores) {
                    h.resetRespuesta();
                }
            }

            // Enviamos la pregunta a todos
            enviarATodos("PREGUNTA " + (i + 1) + ": " + p.getEnunciado());
            enviarATodos(p.getOpciones());
            enviarATodos("INFO| Tienes 15 segundos para responder (escribe a, b, c o d)");

            System.out.println("Pregunta " + (i + 1) + " enviada. Los jugadores responden con: a, b, c o d. Esperando 15 segundos...");

            // Damos 15 segundos para responder
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Comprobamos las respuestas calculando al más rápido y sumando puntos
            synchronized (jugadores) {
                long mejorTiempo = Long.MAX_VALUE;
                ClientHandler masRapido = null;

                // Primero, localizamos al jugador más rápido que haya acertado
                for (ClientHandler h : jugadores) {
                    String resp = h.getRespuestaActual();
                    if (resp != null && resp.equals(p.getRespuestaCorrecta())) {
                        if (h.getTiempoRespuesta() > 0 && h.getTiempoRespuesta() < mejorTiempo) {
                            mejorTiempo = h.getTiempoRespuesta();
                            masRapido = h;
                        }
                    }
                }

                // Luego, repartimos los puntos y aplicamos el Modo Hardcore (penalizaciones)
                for (ClientHandler h : jugadores) {
                    String resp = h.getRespuestaActual();
                    if (resp != null) {
                        if (resp.equals(p.getRespuestaCorrecta())) {
                            if (h == masRapido) {
                                h.sumarPuntos(2);
                            } else {
                                h.sumarPuntos(1);
                            }
                        } else {
                            h.restarPunto();
                        }
                    }
                }

                if (masRapido != null) {
                    enviarATodos("INFO| ¡" + masRapido.getNick() + " ha sido el más rápido (+2 puntos)!");
                }
            }

        }

        // Buscar ganador (o empate)
        int maxPuntos = 0;
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                if (h.getPuntos() > maxPuntos) {
                    maxPuntos = h.getPuntos();
                }
            }
        }

        // Buscar cuántos tienen la puntuación máxima
        ArrayList<String> empatados = new ArrayList<>();
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                if (h.getPuntos() == maxPuntos) {
                    empatados.add(h.getNick());
                }
            }
        }

        // Enviamos ranking final a todos los jugadores
        enviarATodos("=== RANKING FINAL ===");
        mostrarRanking();

        if (empatados.size() > 1) {
            // Hay empate
            String nombres = "";
            for (int i = 0; i < empatados.size(); i++) {
                nombres += empatados.get(i);
                if (i < empatados.size() - 1) {
                    nombres += " y ";
                }
            }
            enviarATodos("EMPATE| Empate entre " + nombres + " con " + maxPuntos + " PUNTOS");
            System.out.println("EMPATE entre " + nombres + " con " + maxPuntos + " puntos");
        } else {
            enviarATodos("GANADOR| " + empatados.get(0) + " | " + maxPuntos + " PUNTOS");
            System.out.println("GANADOR: " + empatados.get(0) + " con " + maxPuntos + " puntos");
        }
        enviarATodos("FIN | La partida ha terminado");

        System.out.println("=== RANKING FINAL EN SERVIDOR ===");
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                System.out.println(h.getNick() + " - " + h.getPuntos() + " puntos");
            }
        }

        // Cerramos todas las conexiones para que los hilos ClientHandler terminen
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                h.cerrarConexion();
            }
        }
        System.out.println("Servidor finalizado.");
    }

    public static void enviarATodos(String mensaje) {
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                h.enviar(mensaje);
            }
        }
    }

    public static int getNumJugadores() {
        synchronized (jugadores) {
            return jugadores.size();
        }
    }

    private static void mostrarRanking() {
        synchronized (jugadores) {
            int pos = 1;
            for (ClientHandler h : jugadores) {
                String linea = pos + ". " + h.getNick() + " - " + h.getPuntos() + " puntos";
                enviarATodos(linea);
                System.out.println(linea);
                pos++;
            }
        }
    }

    public static boolean existeNick(String nick) {
        synchronized (jugadores) {
            for (ClientHandler h : jugadores) {
                if (h.getNick() != null && h.getNick().equalsIgnoreCase(nick)) {
                    return true;
                }
            }
        }
        return false;
    }
}
