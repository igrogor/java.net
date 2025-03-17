import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class App {
    public static void main(String[] args) throws Exception {

        int serverPort = Integer.parseInt(args[0]);
        int clientPort = Integer.parseInt(args[1]);

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();

            try {
                NetStream netStream = new NetStream(/* MixailKrug, */ gui.getBolshayaRazdnica(), serverPort,
                        clientPort);

                Circle MixailKrug = new Circle(10, "green");
                Circle circle = new Circle(10, "green");
                Square square = new Square(20, "blue");
                Triangle triangle = new Triangle(15, 10, "red");

                JComboBox<Object> shapeComboBox = gui.getShapeComboBox();
                shapeComboBox.addItem(circle);
                shapeComboBox.addItem(square);
                shapeComboBox.addItem(triangle);

                // Запуск серверной части в отдельном потоке
                new Thread(() -> netStream.startServer()).start();

                // Обработка кнопки
                gui.setButtonActionListener(event -> {
                    try {
                        Object selectedShape = shapeComboBox.getSelectedItem();
                        netStream.sendData(selectedShape);
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
