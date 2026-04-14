# PROMPT MAESTRO — Trivia Multijugador en Red (Sockets TCP, Java)

Copia y pega todo lo de abajo en cualquier IA para regenerar el proyecto completo.

---

```
Actúa como un experto desarrollador Java especializado en programación de sockets y concurrencia clásica. Genera el código completo, compilable y ejecutable de una aplicación de Trivia Multijugador en Red usando Sockets TCP.

═══════════════════════════════════════════════
  RESTRICCIONES ABSOLUTAS (INCUMPLIR = FALLO)
═══════════════════════════════════════════════

1. NO uses NUNCA expresiones Lambda (->), Streams, ni la API funcional de Java.
2. NO uses clases anidadas ni clases internas. Cada clase va en su propio archivo .java.
3. NO uses paquetes (package). Todas las clases van en el paquete por defecto.
4. Usa ÚNICAMENTE: ServerSocket, Socket, PrintWriter (autoFlush=true), BufferedReader, InputStreamReader, Scanner, ArrayList, HashMap y subclases de Thread.
5. Usa bloques synchronized para TODOS los accesos a la lista compartida de jugadores.
6. Todo el código debe ser Java 8 compatible, con bucles for clásicos e if/else tradicionales.

═══════════════════════════════════════════════
  ARQUITECTURA: 6 ARCHIVOS OBLIGATORIOS
═══════════════════════════════════════════════

Genera exactamente estos 6 archivos, cada uno con su clase pública:

────────────────────────────────
ARCHIVO 1: Pregunta.java
────────────────────────────────
POJO simple con estos atributos privados:
  - String enunciado
  - String opcionA, opcionB, opcionC, opcionD
  - String respuestaCorrecta (una letra: "a", "b", "c" o "d")

Constructor con los 6 parámetros. Getters para enunciado y respuestaCorrecta.
Método getOpciones() que devuelve un String con formato exacto:
  "OPCIONES| a) " + opcionA + " | b) " + opcionB + " | c) " + opcionC + " | d) " + opcionD

────────────────────────────────
ARCHIVO 2: ConsolaAdmin.java
────────────────────────────────
Clase que extiende Thread.
En run(): crea un Scanner(System.in). Imprime "Escribe START para comenzar la partida".
Bucle while(true): lee línea. Si es "START" (ignoreCase):
  - Si Servidor.getNumJugadores() == 0: imprime "No hay jugadores conectados..." y sigue esperando.
  - Si hay jugadores: llama a Servidor.iniciarJuego() y hace break.

────────────────────────────────
ARCHIVO 3: ClientHandler.java
────────────────────────────────
Clase que extiende Thread. Atributos privados:
  - Socket socket
  - BufferedReader entrada
  - PrintWriter salida
  - String nick
  - int puntos (inicializado a 0)
  - String respuestaActual (inicializado a null)
  - long tiempoRespuesta (inicializado a 0)

Constructor: recibe Socket y lo asigna.

Método run():
  1. Crea entrada y salida a partir del socket.
  2. Envía: "Bienvenido al trivia, introduce tu nick"
  3. BUCLE DE VALIDACIÓN DE NICK: while(true) lee intentoNick del cliente.
     - Si Servidor.existeNick(intentoNick) es true → envía "ERROR| Ese nick ya está en uso. Introduce otro:" y continúa el bucle.
     - Si es false → asigna this.nick = intentoNick y hace break.
     IMPORTANTE: Usar una variable local "intentoNick" y solo asignarla a this.nick DESPUÉS de validar, para que existeNick() no se encuentre a sí mismo.
  4. Envía: "Conectado correctamente como " + nick
  5. Imprime en consola del servidor: "Jugador registrado: " + nick
  6. BUCLE DE ESCUCHA: while lee líneas del cliente.
     - Si el mensaje empieza con "RESPUESTA|": extrae la letra (substring(10).trim().toLowerCase()), la guarda en respuestaActual, guarda this.tiempoRespuesta = System.currentTimeMillis(), y envía al cliente: "INFO| Se ha registrado tu respuesta".

Métodos públicos:
  - enviar(String mensaje): escribe con salida.println si salida no es null.
  - getNick(), getPuntos(), getRespuestaActual(), getTiempoRespuesta(): getters.
  - sumarPuntos(int cantidad): puntos += cantidad.
  - restarPunto(): puntos--.
  - resetRespuesta(): respuestaActual = null; tiempoRespuesta = 0.
  - cerrarConexion(): cierra el socket si no está cerrado.

────────────────────────────────
ARCHIVO 4: Servidor.java
────────────────────────────────
Atributos estáticos:
  - private static List<ClientHandler> jugadores = new ArrayList<>()
  - private static boolean juegoIniciado = false

Método main():
  1. Imprime: "Servidor escuchando en el puerto 5000" y "Esperando jugadores..."
  2. Crea e inicia un hilo ConsolaAdmin.
  3. Crea ServerSocket en puerto 5000.
  4. serverSocket.setSoTimeout(1000) para no bloquearse en accept().
  5. Bucle while (!juegoIniciado && jugadores.size() < 10):
     - try: acepta cliente, crea ClientHandler, lo añade a jugadores dentro de synchronized(jugadores), lo inicia con start(), imprime total.
     - catch SocketTimeoutException: no hace nada, vuelve a comprobar.
  6. Cierra serverSocket.

Método public static void iniciarJuego():
  1. juegoIniciado = true.
  2. Thread.sleep(1500) para que el accept() termine su ciclo.
  3. Enviar a todos: "INFO| Comienza la partida" y "INFO| Número de jugadores: X".
  4. Crear array de 5 objetos Pregunta con preguntas sobre Java/redes/hilos. Cada pregunta tiene 4 opciones y una respuesta correcta (a/b/c/d).
  5. Para cada pregunta:
     a. Resetear respuestaActual de todos los handlers (synchronized).
     b. Enviar a todos: "PREGUNTA X: " + enunciado, las opciones con getOpciones(), e "INFO| Tienes 15 segundos para responder (escribe a, b, c o d)".
     c. Thread.sleep(15000).
     d. DENTRO DE synchronized(jugadores):
        - Primer bucle: encontrar al ClientHandler "masRapido" (el que acertó con el menor tiempoRespuesta > 0).
        - Segundo bucle: para cada handler cuya respuesta no sea null:
          · Si acertó Y es masRapido → sumarPuntos(2).
          · Si acertó Y NO es masRapido → sumarPuntos(1).
          · Si falló (respuesta != null pero incorrecta) → restarPunto().
        - Si masRapido != null: enviarATodos "INFO| ¡" + nick + " ha sido el más rápido (+2 puntos)!".
  6. Buscar la puntuación máxima y listar los empatados en un ArrayList<String>.
  7. Enviar a todos: "=== RANKING FINAL ===" seguido del ranking (posición, nick, puntos).
  8. Si hay más de 1 empatado: enviar "EMPATE| Empate entre X y Y con Z PUNTOS".
     Si hay 1 ganador: enviar "GANADOR| nick | Z PUNTOS".
  9. Enviar: "FIN | La partida ha terminado" (con ese formato exacto, espacios incluidos).
  10. Imprimir ranking en consola del servidor.
  11. Cerrar todas las conexiones con cerrarConexion() de cada handler (synchronized).

Métodos auxiliares estáticos:
  - enviarATodos(String): recorre jugadores dentro de synchronized y llama h.enviar().
  - getNumJugadores(): devuelve jugadores.size() dentro de synchronized.
  - mostrarRanking(): recorre jugadores dentro de synchronized, envía y muestra cada posición.
  - existeNick(String nick): recorre jugadores dentro de synchronized, devuelve true si algún handler tiene ese nick (comparación ignoreCase).

────────────────────────────────
ARCHIVO 5: HiloEscucha.java
────────────────────────────────
Clase que extiende Thread. Recibe un BufferedReader en el constructor.
En run(): bucle while lee líneas del servidor y las imprime con System.out.println.
Si el mensaje es exactamente "FIN | La partida ha terminado" → llama System.exit(0).

────────────────────────────────
ARCHIVO 6: Cliente.java
────────────────────────────────
Método main():
  1. Conecta a localhost:5000 con Socket.
  2. Crea BufferedReader para entrada, PrintWriter (autoFlush) para salida, Scanner para teclado.
  3. Lee e imprime el mensaje de bienvenida (readLine).
  4. Muestra prompt ">> ", lee el nick del teclado y lo envía.
  5. Lee e imprime la confirmación del servidor (readLine).
  6. Crea e inicia un HiloEscucha pasándole el BufferedReader.
  7. Bucle while(hiloEscucha.isAlive()):
     - Lee línea del teclado.
     - AUTOCOMPLETADO: si la línea tiene longitud 1 y es una de "abcdABCD" → envía "RESPUESTA|" + letra en minúscula. Si no, envía la línea tal cual.

═══════════════════════════════════════════════
  PROTOCOLO DE MENSAJES (FORMATO EXACTO)
═══════════════════════════════════════════════

Usa estas etiquetas literalmente (incluyendo los espacios después de |):
  - "INFO| " → Mensajes informativos del servidor.
  - "OPCIONES| a) X | b) Y | c) Z | d) W" → Opciones de cada pregunta.
  - "RESPUESTA|letra" → Respuesta del cliente (sin espacio tras |).
  - "ERROR| " → Mensajes de error.
  - "GANADOR| nick | X PUNTOS" → Ganador único.
  - "EMPATE| Empate entre X y Y con Z PUNTOS" → Si hay empate.
  - "FIN | La partida ha terminado" → Señal de fin (con espacio antes y después de |).

═══════════════════════════════════════════════
  INSTRUCCIÓN FINAL
═══════════════════════════════════════════════

Genera los 6 archivos COMPLETOS, listos para compilar con "javac *.java" y ejecutar con "java Servidor" y "java Cliente". No omitas ningún método ni import. No añadas explicaciones fuera del código. Solo código Java.
```
