import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class App {
    public static void main(String[] args) throws Exception {

        int serverPort = Integer.parseInt(args[0]);
        int clientPort = Integer.parseInt(args[1]);

        Circle MixailKrug = new Circle();
        MixailKrug.setColor("green");
        MixailKrug.setRadius(10);

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            try {
                NetStream netStream = new NetStream(MixailKrug, gui.getBolshayaRazdnica(), serverPort, clientPort);

                // Запуск серверной части в отдельном потоке
                new Thread(() -> netStream.startServer()).start();

                // Обработка кнопки
                gui.setButtonActionListener(event -> {
                    try {
                        netStream.sendData();
                        gui.getBolshayaRazdnica().setText("Данные отправлены:\n" + netStream.getReceivedData());
                    } catch (IOException e) {
                        gui.getBolshayaRazdnica().setText("Ошибка при отправке данных: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                gui.getBolshayaRazdnica().setText("Ошибка при создании NetStream: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.io.OutputStreamWriter;
// import java.net.ServerSocket;
// import java.net.Socket;

// import javax.swing.SwingUtilities;

// import com.google.gson.Gson;

// import javax.swing.SwingUtilities;

// public class App {
// public static void main(String[] args) throws Exception {
// if (args.length < 2) {
// System.err.println("Использование: java App <порт_сервера>
// <порт_для_отправки>");
// return;
// }

// int serverPort = Integer.parseInt(args[0]);
// int clientPort = Integer.parseInt(args[1]);

// Circle MixailKrug = new Circle();
// MixailKrug.setColor("green");
// MixailKrug.setRadius(10);

// SwingUtilities.invokeLater(() -> {
// GUI gui = new GUI();
// try {
// NetStream netStream = new NetStream(MixailKrug, gui.getBolshayaRazdnica(),
// serverPort, clientPort);

// // Запуск серверной части в отдельном потоке
// new Thread(() -> netStream.startServer()).start();

// // Обработка кнопки
// gui.setButtonActionListener(event -> {
// try {
// netStream.sendData();
// gui.getBolshayaRazdnica().setText("Данные отправлены:\n" +
// netStream.getReceivedData());
// } catch (IOException e) {
// gui.getBolshayaRazdnica().setText("Ошибка при отправке данных: " +
// e.getMessage());
// e.printStackTrace();
// }
// });
// } catch (IOException e) {
// gui.getBolshayaRazdnica().setText("Ошибка при создании NetStream: " +
// e.getMessage());
// e.printStackTrace();
// }
// });
// }
// }
