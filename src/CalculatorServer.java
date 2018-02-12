import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

    public static void main(String... args) {
        ServerSocket ecout = null;
        try {
            ecout = new ServerSocket(1111);
            while (true) {
                System.out.println("Server Started");
                Socket socket = ecout.accept();
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Calculator calcule = (Calculator) in.readObject();
                calcule.calculate();
                System.out.println(calcule.getResult());
                os.writeObject(calcule);
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
