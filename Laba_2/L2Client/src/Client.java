import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static Thread serverThread;

    public static void main(String[] args) throws Exception {

        GUI gui = new GUI(); // GUI создается в EDT

        serverThread = new Thread(() -> {
            try (Socket NeKuritsa = new Socket("localhost", PORT)) {
                // while (true) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(NeKuritsa.getInputStream()))) {

                    while (true) {

                        try {
                            String mess = in.readLine();
                            if ("Start".equals(mess)) {
                                System.out.println("Start: " + "\n");

                                Singleton.removeAllPerson();
                                while (!"Stop".equals(mess = in.readLine())) {
                                    Singleton.addPerson(Integer.parseInt(mess));
                                    System.out.println(Integer.parseInt(mess) + "\n");

                                }
                                System.out.println("Stop: " + "\n");

                            } else {
                                String className = mess;
                                System.out.println("Получено: " + className);

                                String json = in.readLine();
                                System.out.println("Получено: " + json);

                                if (className == null || json == null)
                                    break;

                                Class<?> clazz = Class.forName(className);
                                Object obj = new Gson().fromJson(json, clazz);
                                Singleton.addEl(obj);
                            }

                        } catch (NumberFormatException e) {

                        }
                    }

                } catch (Exception e) {
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        serverThread.start();

    }

    public static void client(Object obj, int n) throws IOException {
        System.out.println("Client");
        Socket Kuritsa = new Socket("localhost", PORT);

        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

        Gson gson = new Gson();
        String className = obj.getClass().getName();
        String json = gson.toJson(obj);

        out.write(n + "\n");
        out.write(className + "\n");
        out.write(json + "\n");
        out.flush();
        // Kuritsa.close();
    }

}
