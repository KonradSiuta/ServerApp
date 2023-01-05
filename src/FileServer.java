import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class FileServer {

    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String defaultLocation;

    public FileServer(int port, String location){
        this.defaultLocation = location;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clientConnection(){
        try{
            while (true){
                System.out.println("Oczekuje na klienta ...");
                socket = serverSocket.accept();
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                System.out.println("Nawiązano połączenie z klientem z adresu " );
                getFileFromClient();
                cloesSocket();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFileFromClient(){
        try {
            int fileNameLength = dataInputStream.readInt();
            if (fileNameLength > 0){
                byte[] fileNameBytes = new byte[fileNameLength];
                dataInputStream.readFully(fileNameBytes);
                String fileName = new String(fileNameBytes);
                long fileContentLength = dataInputStream.readLong();
                if(fileContentLength > 0){
                    byte[] fileContentBytes = new byte[(int) fileContentLength];
                    dataInputStream.readFully(fileContentBytes);
                    FileOutputStream fileOut =
                            new FileOutputStream(defaultLocation + fileName);
                    fileOut.write(fileContentBytes);
                    fileOut.flush();
                    fileOut.close();
                    System.out.println("Zapisano przekazany plik " + fileName);
                }else{
                    System.out.println("Plik pusty nie tworzę go");
                }
            }else{
                System.out.println("nie przekazano plku");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFileToClient(){
        File file = new File(defaultLocation + "test.txt");
        // pobranie nazwy i konwersja na tablice najtów
        System.out.println("przygotowywanie pliku do wysłania");
        int fileNameLength = file.getName().length();
        byte[] fileNameBytes = file.getName().getBytes(StandardCharsets.UTF_8);
        byte[] fileContentBytes = new byte[(int) file.length()];
        try {
            FileInputStream fileIn = new FileInputStream(file);
            fileIn.read(fileContentBytes);
            fileIn.close();
            // wysłanie nazwy pliku
            dataOutputStream.writeInt(fileNameLength);
            dataOutputStream.write(fileNameBytes);
            // wysyłanie zawartości pliku
            dataOutputStream.writeLong(file.length());
            dataOutputStream.write(fileContentBytes);
            dataOutputStream.flush();
            System.out.println("Wysłano plik");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cloesSocket(){
        if (!socket.isClosed()){
            try {
                dataInputStream.close();
                dataOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
