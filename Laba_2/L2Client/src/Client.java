import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {

        GUI gui = new GUI(); // GUI создается в EDT

        // try (Socket socket = new Socket(HOST, PORT);
        //         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //         Scanner scanner = new Scanner(System.in)) {

        //     // new Thread(() -> {
        //     // try {
        //     // String serverMessage;
        //     // while ((serverMessage = in.readLine()) != null) {
        //     // System.out.println("Сервер: " + serverMessage);
        //     // }
        //     // } catch (IOException e) {
        //     // e.printStackTrace();
        //     // }
        //     // }).start();

        //     String userInput;
        //     while ((userInput = scanner.nextLine()) != null) {

        //         out.println(userInput);

        //     }
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }


    public static void client(Object obj) throws IOException {
        System.out.println("Client");
        Socket Kuritsa = new Socket("localhost", PORT);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

        Gson gson = new Gson();
        String className = obj.getClass().getName();
        String json = gson.toJson(obj);

        out.write(className + "\n");
        out.write(json + "\n");
        out.flush();
        Kuritsa.close();
    }
}