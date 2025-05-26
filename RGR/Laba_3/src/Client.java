// Client.java
import com.google.gson.Gson;
import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.UUID;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    private DatagramSocket socket;
    private InetAddress serverAddress;
    private String clientId; // будет установлен после регистрации/входа

    public static void main(String[] args) throws Exception {
        Client clientHandler = new Client();

        // Шаг 1: регистрация или вход
        while (true) {
            String login = JOptionPane.showInputDialog("Введите логин:");
            String password = JOptionPane.showInputDialog("Введите пароль:");

            if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Логин и пароль не должны быть пустыми");
                continue;
            }

            int choice = JOptionPane.showConfirmDialog(
                null,
                "Зарегистрироваться? (Да — регистрация, Нет — вход)",
                "Авторизация",
                JOptionPane.YES_NO_OPTION
            );

            // сохраняем временный clientId до входа (например, UUID)
            clientHandler.clientId = UUID.randomUUID().toString();

            boolean success = (choice == JOptionPane.YES_OPTION)
                ? clientHandler.register(login, password)
                : clientHandler.login(login, password);

            if (success) {
                // после успешного входа/регистрации идентификаторю клиента — это логин
                clientHandler.clientId = login;
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Ошибка: неверный логин/пароль или логин занят");
            }
        }

        // Шаг 2: сообщаем серверу о своём присутствии (IP/порт)
        clientHandler.sendPresence();

        // Шаг 3: запускаем GUI и поток приёма сообщений
        SwingUtilities.invokeLater(() -> new GUI(clientHandler));
        clientHandler.start();
    }

    public Client() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        serverAddress = InetAddress.getByName(HOST);
    }

    /** Уведомляет сервер о том, что этот clientId активен */
    public void sendPresence() throws IOException {
        // Сервер воспринимает это как новый адрес для clientId
        send(clientId + "\n");
    }

    /** Запускает поток, принимающий входящие пакеты от сервера */
    public void start() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String message = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("Клиент " + clientId + " получил: " + message);

                    String[] lines = message.split("\n");
                    if (lines.length > 0 && "Start2".equals(lines[0])) {
                        // Обновление списка клиентов
                        Singleton.removeAllPerson();
                        for (int i = 1; i < lines.length - 1; i++) {
                            String otherId = lines[i];
                            if (!otherId.equals(this.clientId)) {
                                Singleton.addPerson(otherId);
                            }
                        }
                    } else if (lines.length >= 3) {
                        // Получили объект
                        String className = lines[0];
                        String json = lines[1];
                        String senderId = lines[2];

                        if (!senderId.equals(clientId)) {
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

    /** Отправляет произвольное сообщение-сообщение серверу по UDP */
    private void send(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT);
        socket.send(packet);
    }

    /** Регистрация нового пользователя */
    public boolean register(String login, String password) throws IOException {
        String msg = clientId + "\n"
                   + "REGISTER\n"
                   + login + "\n"
                   + password;
        return sendAndReceive(msg).startsWith("REGISTER_SUCCESS");
    }

    /** Вход существующего пользователя */
    public boolean login(String login, String password) throws IOException {
        String msg = clientId + "\n"
                   + "LOGIN\n"
                   + login + "\n"
                   + password;
        String response = sendAndReceive(msg);
        return response.startsWith("LOGIN_SUCCESS");
    }

    /** Отправляет команду и ждёт одно UDP-ответа (до таймаута) */
    private String sendAndReceive(String message) throws IOException {
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, PORT);
        socket.send(packet);

        byte[] buffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.setSoTimeout(2000); // 2 секунды ожидания
        try {
            socket.receive(response);
            return new String(response.getData(), 0, response.getLength());
        } catch (SocketTimeoutException e) {
            return "TIMEOUT";
        }
    }

    /** Отправка объекта другому клиенту */
    public void client(Object obj, String personId) throws IOException {
        Gson gson = new Gson();
        String className = obj.getClass().getName();
        String json = gson.toJson(obj);

        String message = clientId + "\n"
                       + personId + "\n"
                       + className + "\n"
                       + json;
        System.out.println("Клиент отправляет: " + message);
        send(message);
    }

    public String getClientId() {
        return clientId;
    }

    public void disconnect() {
        try {
            send(clientId + "\n" + "exit\n");
            socket.close();
        } catch (Exception e) {
        }
    }
}
