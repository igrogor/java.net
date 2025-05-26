import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

public class MultiClientServer {
  private static final int PORT = 8080;
  private static final Map<String, InetSocketAddress> clients = new ConcurrentHashMap<>();
  private static DatagramSocket socket;
  private static final Map<String, String> userCredentials = new ConcurrentHashMap<>();

  public static void main(String[] args) throws Exception {
    socket = new DatagramSocket(PORT);
    System.out.println("Сервер запущен на порту " + PORT);

    byte[] buffer = new byte[1024];
    while (true) {
      DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
      socket.receive(packet);

      String message = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

      String[] parts = message.split("\n", 2);

    System.out.println("CCCCCCCCCCCCCC\n");
      System.out.println(parts + "\n");
      System.out.println("VVVVVVVVVVVVVV\n");


      String clientId = parts[0];
      String data = parts.length > 1 ? parts[1] : "";

      System.out.println(clientId + "\n");
      System.out.println("ZZZZZZZZZZZZZZZc\n");
      System.out.println(message + "\n");
      System.out.println("ZZZZZZZZZZZZZZZc\n");

      String[] lines = data.split("\n");
      
      if (lines.length > 0 && lines[0].equals("exit")) {
        if (clients.containsKey(clientId)) {
          clients.remove(clientId);
          System.out.println("Клиент " + clientId + " отключился");
          updateClients();
        }
        continue;
      }

      if (lines.length >= 1) {
        String command = lines[0];

        if ("REGISTER".equals(command) && lines.length >= 3) {
          String login = lines[1];
          String password = lines[2];
          if (userCredentials.containsKey(login)) {
            sendMessage("REGISTER_FAIL\nПользователь уже существует", packet.getAddress(),
                packet.getPort());
          } else {
            userCredentials.put(login, password);
            sendMessage("REGISTER_SUCCESS\nРегистрация прошла успешно", packet.getAddress(),
                packet.getPort());
            System.out.println("Регистрация: " + login);
          }
          continue;
        }

        // === Вход ===
        if ("LOGIN".equals(command) && lines.length >= 3) {
          String login = lines[1];
          String password = lines[2];
          if (userCredentials.containsKey(login) && userCredentials.get(login).equals(password)) {
            clients.put(login, new InetSocketAddress(packet.getAddress(), packet.getPort()));
            sendMessage("LOGIN_SUCCESS\n" + login, packet.getAddress(), packet.getPort());
            System.out.println("Успешный вход: " + login);
            updateClients();
          } else {
            sendMessage(
                "LOGIN_FAIL\nНеверный логин или пароль", packet.getAddress(), packet.getPort());
          }
          continue;
        }

        InetSocketAddress clientAddress =
            new InetSocketAddress(packet.getAddress(), packet.getPort());
        if (!clients.containsKey(clientId)) {
          clients.put(clientId, clientAddress);
          System.out.println(
              "Зарегистрирован клиент: " + clientId + " с адресом: " + clientAddress);
          updateClients();
        } else {
          clients.put(clientId, clientAddress);
        }

        // Обработка сообщения
        if (lines.length >= 3) {
          String personId = lines[0];

          if ("exit".equals(lines[1])) {
            clients.remove(personId);
            updateClients();
          } else {
            String className = lines[1];
            String json = lines[2];
            System.out.println(
                "Получено от " + clientId + ": " + personId + ", " + className + ", " + json);
            broadcast(className + "\n" + json, personId, clientId);
          }

          // String className = lines[1];
          // String json = lines[2];
          // System.out.println("Получено от " + clientId + ": " + personId + ", " + className + ",
          // " + json); broadcast(className + "\n" + json, personId, clientId);
        }
      }
    }
  }

  private static void broadcast(String message, String personId, String senderId) {
    InetSocketAddress recipient = clients.get(personId);
    if (recipient != null) {
      String fullMessage = message + "\n" + senderId;
      byte[] data = fullMessage.getBytes();
      DatagramPacket packet =
          new DatagramPacket(data, data.length, recipient.getAddress(), recipient.getPort());
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
      DatagramPacket packet =
          new DatagramPacket(data, data.length, addr.getAddress(), addr.getPort());
      try {
        socket.send(packet);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void sendMessage(String msg, InetAddress address, int port) {
    try {
      byte[] data = msg.getBytes();
      DatagramPacket response = new DatagramPacket(data, data.length, address, port);
      socket.send(response);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}