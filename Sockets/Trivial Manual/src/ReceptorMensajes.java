import java.io.BufferedReader;
import java.io.IOException;

public class ReceptorMensajes extends Thread{
    BufferedReader in;

    public ReceptorMensajes(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        while (true){
            try {
                System.out.println(in.readLine());
            } catch (IOException e) {
                throw new RuntimeException("Error" + e.getMessage());
            }
        }
    }
}
