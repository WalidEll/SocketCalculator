import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CalculatorServer {

    public static void main(String... args) {
        ServerSocket ecout = null;
        try {

            //   starting a java socket server
            ecout = new ServerSocket(1111);
            System.out.println("Server Started");

            while (true) {

                Socket socket = ecout.accept();
                ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                //getting serialized object from client
                Calculator calcule = (Calculator) in.readObject();
                //calculate result
                calcule.calculate();
                System.out.println(calcule.getResult());
                //returning result to client
                os.writeObject(calcule);
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
