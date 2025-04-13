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
        buttonsPanel.setVisible(true);

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

            synchronized (Singleton.list) {
                for (int i = 0; i < Singleton.list.size(); i++) {
                    model.addElement(i);
                }
            }
            textArea.setText("");
            synchronized (Singleton.list) {
                for (Object item : Singleton.list) {
                    textArea.append(Singleton.ObjectToJSON(item) + "\n------\n");
                }
            }
        });
    }

}
