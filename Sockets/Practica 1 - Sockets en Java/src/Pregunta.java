public class Pregunta {
    private String enunciado;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String respuestaCorrecta;

    public Pregunta(String enunciado, String opcionA, String opcionB, String opcionC, String opcionD, String respuestaCorrecta) {
        this.enunciado = enunciado;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String getOpciones() {
        return "OPCIONES| a) " + opcionA + " | b) " + opcionB + " | c) " + opcionC + " | d) " + opcionD;
    }
}
