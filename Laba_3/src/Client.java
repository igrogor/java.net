import com.google.gson.Gson;
import java.io.*;
import java.net.*;
import java.util.UUID;

public class Client {
  private static final String HOST = "localhost";
  private static final int PORT = 8080;
  private DatagramSocket socket;
  private InetAddress serverAddress;
  private String clientId = UUID.randomUUID().toString();

  public static void main(String[] args) throws Exception {
    Client clientHandler = new Client();
    GUI gui = new GUI(clientHandler);
    clientHandler.start();
  }

  public Client() throws Exception {
    socket = new DatagramSocket();
    serverAddress = InetAddress.getByName(HOST);
  }

  public void start() throws Exception {
    send(clientId + "\n");
    System.out.println("Клиент " + clientId + " отправил регистрацию серверу");

    new Thread(() -> {
      byte[] buffer = new byte[1024];
      while (true) {
        try {
          DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
          socket.receive(packet);
          String message = new String(packet.getData(), 0, packet.getLength());
          System.out.println("Клиент " + clientId + " получил: " + message);
          String[] lines = message.split("\n");

          if (lines.length > 0 && lines[0].equals("Start2")) {
            Singleton.removeAllPerson();
            for (int i = 1; i < lines.length - 1; i++) {
              String clientId = lines[i];
              if (!clientId.equals(this.clientId)) { // Исключаем текущий клиент из списка
                Singleton.addPerson(clientId);
              }
            }
          } else if (lines.length >= 3) {
            String className = lines[0];
            String json = lines[1];
            String senderId = lines[2]; // Получаем ID отправителя
            if (!senderId.equals(clientId)) { // Проверяем, не отправитель ли это
              try {
                Class<?> clazz = Class.forName(className);
                Object obj = new Gson().fromJson(json, clazz);
                Singleton.addEl(obj);
                System.out.println("Клиент " + clientId + " добавил объект: " + obj);
              } catch (ClassNotFoundException e) {
                e.printStackTrace();
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public void client(Object obj, String personId) throws IOException {
    Gson gson = new Gson();
    String className = obj.getClass().getName();
    String json = gson.toJson(obj);
    String message = clientId + "\n" + personId + "\n" + className + "\n" + json;
    System.out.println("Клиент отправляет: " + message);
    send(message);
  }

  private void send(String message) throws IOException {
    byte[] data = message.getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT);
    socket.send(packet);
  }

  public String getClientId() {
    return clientId;
  }
}