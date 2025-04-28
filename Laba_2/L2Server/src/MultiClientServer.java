import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiClientServer {
  private static final int PORT = 8080;
  //   private static final ExecutorService executor = Executors.newCachedThreadPool();
  private static final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

  public static void main(String[] args) {
    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
      System.out.println("Сервер запущен на порту " + PORT);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        ClientHandler client = new ClientHandler(clientSocket);
        clients.add(client);
        // executor.execute(client);
        new Thread(client).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        ClientHandler.updatePerson();

        while (true) {
          String personLine = in.readLine();
          System.out.println("Получено: " + personLine);
          if (personLine == null)
            break; // Проверка конца потока

          try {
            int person = Integer.parseInt(personLine);
            String className = in.readLine();
            System.out.println("Получено: " + className);

            String json = in.readLine();
            System.out.println("Получено: " + json);

            if (className == null || json == null)
              break; // Проверка целостности данных

            // Отправляем данные конкретному клиенту
            broadcast(className, person);
            broadcast(json, person);
          } catch (NumberFormatException e) {
            System.err.println("Некорректный формат номера клиента: " + personLine);
            break;
          }
        }

      } catch (IOException e) {
        System.out.println("Клиент отключен");
      } finally {
        try {
          socket.close();
          clients.remove(this);
          ClientHandler.updatePerson();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    // private void broadcast(String message, int i) {
    //     clients.get(i).sendMessage(message);
    // }

    private void broadcast(String message, int i) {
      if (i >= 0 && i < clients.size()) {
        ClientHandler client = clients.get(i);
        if (client != null) {
          client.sendMessage(message);
        }
      }
    }

    private void sendMessage(String message) {
      out.println(message);
    }

    public static void updatePerson() {
      synchronized (clients) {
        for (int i = 0; i < clients.size(); i++) {
          ClientHandler client = clients.get(i);
          if (client.out != null) {
            client.out.println("Start2");
            System.out.println("Start3: "
                + "\n");

            for (int j = 0; j < clients.size(); j++) {
              client.out.println(j);
              System.out.println(j + "\n");
            }
            client.out.println("Stop2");
            System.out.println("Stop3: "
                + "\n");
          }
        }
      }
    }

    // public static void updatePerson() {
    //     synchronized (clients) {
    //         for (ClientHandler client : clients) {
    //             if (client.out != null) {
    //                 client.out.println("Start");
    //                 clients.forEach(c -> client.out.println(clients.indexOf(c)));
    //                 client.out.println("Stop");
    //             }
    //         }
    //     }
    // }
  }
}