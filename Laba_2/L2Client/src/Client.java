import com.google.gson.Gson;
import java.io.*;
import java.net.*;

public class Client {
  public final String HOST = "localhost";
  public final int PORT = 8080;
  public Thread serverThread;
  public BufferedReader in;
  public BufferedWriter out;
  public Socket NeKuritsa;

  public static void main(String[] args) throws Exception {
    Client clientHandler = new Client();
    GUI gui = new GUI(clientHandler); // GUI создается в EDT
    try {
      clientHandler.NeKuritsa = new Socket(clientHandler.HOST, clientHandler.PORT);
      clientHandler.in =
          new BufferedReader(new InputStreamReader(clientHandler.NeKuritsa.getInputStream()));
      clientHandler.out =
          new BufferedWriter(new OutputStreamWriter(clientHandler.NeKuritsa.getOutputStream()));
      clientHandler.serverThread = new Thread(() -> {
        while (true) {
          try {
            String mess = clientHandler.in.readLine();
            System.out.println("rec: " + mess);
            if ("Start2".equals(mess)) {
              System.out.println("Start1:\n");
              Singleton.removeAllPerson();
              while (!"Stop2".equals(mess = clientHandler.in.readLine())) {
                Singleton.addPerson(Integer.parseInt(mess));
                System.out.println(Integer.parseInt(mess) + "\n");
              }
              System.out.println("Stop1:\n");
            } else {
              String className = mess;
              System.out.println("Получено: " + className);
              String json = clientHandler.in.readLine();
              System.out.println("Получено: " + json);
              if (className == null || json == null)
                break;
              Class<?> clazz = Class.forName(className);
              Object obj = new Gson().fromJson(json, clazz);
              Singleton.addEl(obj);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    clientHandler.serverThread.start();
  }

  public void client(Object obj, int n) throws IOException {
    System.out.println("Client");
    Gson gson = new Gson();
    String className = obj.getClass().getName();
    String json = gson.toJson(obj);

    out.write(n + "\n");
    out.write(className + "\n");
    out.write(json + "\n");
    out.flush();
  }
}
