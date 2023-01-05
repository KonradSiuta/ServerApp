import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private List<Socket> socketList;

    public Server(int port){
        try{
            serverSocket = new ServerSocket(port);
        } catch (IOException e){
            System.out.print(e.getMessage());
        }
    }

    public void serverConnection(){
        ExecutorService service = Executors.newCachedThreadPool();
        while(true){
            try {
                System.out.println("Oczekuje na klienta ...");
                socket = serverSocket.accept();
                System.out.println("Nawiązałem połączenie z adresem" +
                        socket.getInetAddress().getHostAddress());
                service.submit(new ServerThread(socket));
                socketList.add(socket);
//                sendMessageToClient();
//                disconnectClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            e.printStackTrace();
        }
    }

    private void sendMessageToClientWithOption(){
        // PrintWriter writer = new PrintWriter(socket.getOutputStream(),
        //          true);
        // BufferedReader reader = new BufferedReader(new InputStreamReader(
        //             socket.getInputStream(), StandardCharsets.UTF_8
        //         ));
        String option = "1) dowolny opcja 1\n" +
                "2) dowolna opcja 2\n" +
                "3) dowolna opcja 3\n" +
                "4) zakończ";
    }

    private void disconnectClient(){
        if (!socket.isClosed()){
            try {
                socket.close();
                System.out.println("Zakończyłem połączenie z klientem");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
