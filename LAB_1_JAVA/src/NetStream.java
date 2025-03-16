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


import java.io.*;
import java.net.*;
import javax.swing.*;
import com.google.gson.Gson;

public class NetStream {
    public Circle MixailKrug;
    private String receivedData;
    private JTextArea BolshayaRazdnica;
    private int serverPort;
    private int clientPort;

    public NetStream(Circle MixailKrug, JTextArea BolshayaRazdnica, int serverPort, int clientPort) throws IOException {
        this.MixailKrug = MixailKrug;
        this.BolshayaRazdnica = BolshayaRazdnica;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
    }

    public void sendData() throws IOException {
        Socket Kuritsa = new Socket("localhost", clientPort);  // Отправляем данные на порт другого экземпляра
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

        Gson gson = new Gson();
        String json = gson.toJson(MixailKrug);

        out.write(json);
        out.flush();
        Kuritsa.close();

        System.out.println("Данные отправлены: " + json);
    }

    public void startServer() {
        try (ServerSocket Petuh = new ServerSocket(serverPort)) {  // Сервер слушает свой порт
            System.out.println("Сервер запущен и ожидает подключения на порту " + serverPort + "...");

            while (true) {
                Socket Korova = Petuh.accept();
                new Thread(() -> {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(Korova.getInputStream()))) {
                        String koza = in.readLine();
                        if (koza != null && koza.contains("{")) {
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
                                if (BolshayaRazdnica != null) {
                                    BolshayaRazdnica.setText("Получены данные:\n" + receivedData);
                                } else {
                                    System.err.println("BolshayaRazdnica is null!");
                                }
                            });
                        } else {
                            System.err.println("Получены некорректные данные: " + koza);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getReceivedData() {
        return receivedData;
    }
}

// public class NetStream {

//     public Circle MixailKrug;
//     private String receivedData;
//     private JTextArea BolshayaRazdnica;

//     public NetStream(Circle MixailKrug, JTextArea BolshayaRazdnica) throws IOException {
//         this.MixailKrug = MixailKrug;
//         this.BolshayaRazdnica = BolshayaRazdnica;
//     }

//     // public void sendData() throws IOException {
//     //     // Логика отправки данных
//     //     Socket Kuritsa = new Socket("localhost", 65432);
//     //     BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));

//     //     Gson gson = new Gson();
//     //     String json = gson.toJson(MixailKrug);

//     //     Class<?> cls = MixailKrug.getClass();
//     //     out.write(cls.getName());
//     //     out.write(json);

//     //     out.flush();
//     //     Kuritsa.close();

//     //     System.out.println("Данные отправлены: " + json);
//     // }

//     public void sendData() throws IOException {
//         Socket Kuritsa = new Socket("localhost", 65432);
//         BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Kuritsa.getOutputStream()));
    
//         Gson gson = new Gson();
//         String json = gson.toJson(MixailKrug);
    
//         Class<?> cls = MixailKrug.getClass();
//         out.write(cls.getName() + "\n");  // Добавляем разделитель
//         out.write(json);
//         out.flush();
//         Kuritsa.close();
    
//         System.out.println("Данные отправлены: " + json);
//     }



//     public void startServer() {
//         try (ServerSocket Petuh = new ServerSocket(65432)) {
//             System.out.println("Сервер запущен и ожидает подключения...");
    
//             while (true) {
//                 Socket Korova = Petuh.accept();
//                 new Thread(() -> {
//                     try (BufferedReader in = new BufferedReader(new InputStreamReader(Korova.getInputStream()))) {
//                         String koza = in.readLine();
//                         String[] parts = koza.split("\\{", 2);
//                         String part1 = parts[0] + "\n";
//                         String part2 = "{" + parts[1];
    
//                         receivedData = part1 + part2;
    
//                         System.out.println(part1);
//                         System.out.println(part2);
    
//                         Gson gson = new Gson();
//                         Circle obj = gson.fromJson(part2, Circle.class);
//                         System.out.println("Deserialized object: " + obj);
    
//                         SwingUtilities.invokeLater(() -> {
//                             if (BolshayaRazdnica != null) {
//                                 BolshayaRazdnica.setText("Получены данные:\n" + receivedData);
//                             } else {
//                                 System.err.println("BolshayaRazdnica is null!");
//                             }
//                         });
    
//                     } catch (Exception e) {
//                         e.printStackTrace();
//                     }
//                 }).start();
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }


//     public String getReceivedData() {
//         return receivedData;
//     }

// }
