import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerThread implements Runnable{

    private Socket socket;

    public ServerThread(Socket s){
        socket = s;
    }

    @Override
    public void run() {
        sendMessageToClient();
    }

    private void sendMessageToClient() {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(),
                    true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8
            ));
            String text;
            while(true){
                writer.println("Witaj podaj imię:");
                text = reader.readLine();
                if (text.equals("e")){
                    System.out.println("klient zakończył połączenie");
                    writer.close();
                    reader.close();
                    break;
                }
                System.out.println(text);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
