import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.google.gson.Gson;

public class NetStream {

    // public Circle MixailKrug;
    private String receivedData;
    private JTextArea BolshayaRazdnica;
    private int serverPort;
    private int clientPort;

    public NetStream(/*Circle MixailKrug, */JTextArea BolshayaRazdnica, int serverPort, int clientPort) throws IOException {
        // this.MixailKrug = MixailKrug;
        this.BolshayaRazdnica = BolshayaRazdnica;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void sendData(Object MixailKrug) throws IOException {
       
        Socket Kuritsa = new Socket("localhost", clientPort);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

        Gson gson = new Gson();
        String json = gson.toJson(MixailKrug);

        Class<?> cls = MixailKrug.getClass();
        out.write(cls.getName());
        out.write(json);

        out.flush();
        Kuritsa.close();

        System.out.println("Данные отправлены: ");
    }

    public void startServer() {
        try (ServerSocket Petuh = new ServerSocket(serverPort)) {
            while (true) {
                Socket Korova = Petuh.accept();
                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(Korova.getInputStream()))) {
                        String koza = in.readLine();
                        String[] parts = koza.split("\\{", 2);
                        String part1 = parts[0] + "\n";
                        String part2 = "{" + parts[1];

                        receivedData = part1 + part2;

                        System.out.println(part1);
                        System.out.println(part2);

                        Gson gson = new Gson();
                        Circle obj = gson.fromJson(part2, Circle.class);
                        System.out.println("Deserialized object: " + obj);

                        SwingUtilities.invokeLater(() -> {
                            BolshayaRazdnica.setText("Получены данные:\n" + receivedData);
                        });

                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }).start();
            }
        } catch (IOException e) {

        }
    }

    public String getReceivedData() {
        return receivedData;
    }

}
