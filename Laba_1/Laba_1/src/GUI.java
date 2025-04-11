import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame implements ComboBoxUpdateListener {
    static JTextArea textArea = new JTextArea();

    public DefaultComboBoxModel<Object> model;
    public static JComboBox<Object> comboBox;

    GUI() {

        model = new DefaultComboBoxModel<>();
        comboBox = new JComboBox<>(model);

        JFrame frame = new JFrame("Window");

        JPanel buttonsPanel = new JPanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 900);
        frame.setLocationRelativeTo(null);

        JButton create = new JButton("Создать");
        JButton send = new JButton("Отправить");

        ActionListener myButtonsListener = new ButtonsListener(comboBox, model);

        create.setActionCommand("Создать");
        send.setActionCommand("Отправить");

        create.addActionListener(myButtonsListener);
        send.addActionListener(myButtonsListener);

        buttonsPanel.add(create);
        buttonsPanel.add(send);
        buttonsPanel.add(comboBox);

        ButtonGroup group = new ButtonGroup();

        JRadioButton[] radioButtons = new JRadioButton[2];
        radioButtons[1] = new JRadioButton("Клиент");
        radioButtons[0] = new JRadioButton("Сервер");
        group.add(radioButtons[0]);
        group.add(radioButtons[1]);
        buttonsPanel.add(radioButtons[0]);
        buttonsPanel.add(radioButtons[1]);

        radioButtons[0].addActionListener(e -> {

            if (radioButtons[0].isSelected()) {
                System.out.println("Выбран Сервер: 0");
                App.flag = 0;
                create.setVisible(false);
                send.setVisible(false);
                comboBox.setVisible(false);
                ClientAndServer.stopServer(); // Остановить предыдущий сервер
                ClientAndServer.server(); // Запустить новый
            }
        });

        radioButtons[1].addActionListener(e -> {
            if (radioButtons[1].isSelected()) {
                System.out.println("Выбран Клиент: " + 1);
                ClientAndServer.stopServer(); // Остановить предыдущий сервер
                App.flag = 1;
                create.setVisible(true);
                send.setVisible(true);
                comboBox.setVisible(true);

            }
        });

        frame.getContentPane().add(BorderLayout.NORTH, buttonsPanel);

        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.getContentPane().add(BorderLayout.CENTER, scrollPane);

        frame.setVisible(true);

        Singleton.setUpdateListener(this);
    }

    @Override
    public void onComboBoxUpdateNeeded() {

        SwingUtilities.invokeLater(() -> {
            model.removeAllElements();

            // Синхронизируем доступ к списку
            synchronized (Singleton.list) {
                for (int i = 0; i < Singleton.list.size(); i++) {
                    model.addElement(i);
                }
            }

            // Обновляем текстовую область с синхронизацией
            textArea.setText("");
            synchronized (Singleton.list) {
                for (Object item : Singleton.list) {
                    textArea.append(Singleton.ObjectToJSON(item) + "\n------\n");
                }
            }
        });
    }

}
