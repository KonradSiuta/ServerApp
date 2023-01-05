package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

public class UDPServer {
    private DatagramSocket socket;
    private DatagramPacket packet;

    public UDPServer() {
        try {
            socket = new DatagramSocket(5501);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void getConnection() {
        byte[] buff = new byte[32];
        while (true) {
            try {
                packet = new DatagramPacket(buff, buff.length);
                socket.receive(packet);

                String m = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                System.out.println("Wiadmomość z " + packet.getAddress() + ": " + m);

                packet.setData("Odpowiedz zwrotna: ".getBytes(StandardCharsets.UTF_8));
                socket.send(packet);

                if (m.equals("end")) {
                    System.out.println("Koniec pracy serwera");
                    socket.send(packet);
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();

    }

    public void sendMessageToClients() {
        LocalDateTime now = LocalDateTime.now();
        Random random = new Random();
        String msg;
        while (true) {
            int number = random.nextInt();
            msg = "Wylosowano liczbe " + number;
            if (LocalDateTime.now().equals(now.plusSeconds(5))) {
                packet.setData(msg.getBytes());
                try {
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
