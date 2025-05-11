import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class MultiClientServer {
    private static final int PORT = 8080;
    private static final Map<String, InetSocketAddress> clients = new ConcurrentHashMap<>();
    private static DatagramSocket socket;

    public static void main(String[] args) throws Exception {
        socket = new DatagramSocket(PORT);
        System.out.println("Сервер запущен на порту " + PORT);

        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());
            String[] parts = message.split("\n", 2);
            String clientId = parts[0];
            String data = parts.length > 1 ? parts[1] : "";

            // Регистрация нового клиента
            InetSocketAddress clientAddress = new InetSocketAddress(packet.getAddress(), packet.getPort());
            if (!clients.containsKey(clientId)) {
                clients.put(clientId, clientAddress);
                System.out.println("Зарегистрирован клиент: " + clientId + " с адресом: " + clientAddress);
                updateClients();
            } else {
                // Обновляем адрес клиента, если он уже зарегистрирован
                clients.put(clientId, clientAddress);
            }

            // Обработка данных от клиента
            if (!data.isEmpty()) {
                String[] lines = data.split("\n");
                if (lines.length >= 3) {
                    String personId = lines[0];
                    String className = lines[1];
                    String json = lines[2];
                    System.out.println("Получено от " + clientId + ": " + personId + ", " + className + ", " + json);
                    broadcast(className + "\n" + json, personId, clientId);
                }
            }
        }
    }

    private static void broadcast(String message, String personId, String senderId) {
        InetSocketAddress recipient = clients.get(personId);
        if (recipient != null) {
            String fullMessage = message + "\n" + senderId; // Добавляем ID отправителя
            byte[] data = fullMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, recipient.getAddress(), recipient.getPort());
            try {
                System.out.println("Отправка данных клиенту " + personId + ": " + fullMessage);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Клиент с ID " + personId + " не найден");
        }
    }

    private static void updateClients() {
        String clientList = "Start2\n" + String.join("\n", clients.keySet()) + "\nStop2";
        System.out.println("Отправка списка клиентов: " + clientList);
        for (InetSocketAddress addr : clients.values()) {
            byte[] data = clientList.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, addr.getAddress(), addr.getPort());
            try {
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}