import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ClientAndServer {
    private static ServerSocket serverSocket;
    private static Thread serverThread;
    private static volatile boolean isRunning = false;

    public static void client(Object obj) throws IOException {
        System.out.println("Client");
        Socket Kuritsa = new Socket("localhost", 65432);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

        Gson gson = new Gson();
        String className = obj.getClass().getName();
        String json = gson.toJson(obj);

        out.write(className + "\n");
        out.write(json + "\n");
        out.flush();
        Kuritsa.close();
    }

    public static void server() {
        if (isRunning)
            return;
        isRunning = true;

        serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(65432)) {
                while (isRunning) {
                    try (Socket clientSocket = serverSocket.accept();
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(clientSocket.getInputStream()))) {

                        String className = in.readLine();
                        String json = in.readLine();

                        Class<?> clazz = Class.forName(className);
                        Object obj = new Gson().fromJson(json, clazz);

                        Singleton.addEl(obj);

                    } catch (Exception e) {
                        if (isRunning)
                            e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                isRunning = false;
            }
        });
        serverThread.start();
    }

    public static void stopServer() {
        isRunning = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
