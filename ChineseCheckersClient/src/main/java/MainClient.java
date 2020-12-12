
import java.net.InetAddress;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

public class MainClient {

    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;


    public MainClient() throws Exception {

        socket = new Socket(InetAddress.getLocalHost(), 58901);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream(), true);

    }



    public void play() throws Exception {
        try {
            while (in.hasNextLine()) {
                var response = in.nextLine();
                if (response.startsWith("START")) {
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws Exception {
        MainClient client = new MainClient();
        client.play();
    }
}