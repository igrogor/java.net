import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class GUI extends JFrame implements ComboBoxUpdateListener {
  static JTextArea textArea = new JTextArea();

  public DefaultComboBoxModel<Object> model;
  public static JComboBox<Object> comboBox;

  public DefaultComboBoxModel<Object> modelPerson;
  public static JComboBox<Object> comboBoxPerson;
  public Client clientHandler;

  GUI(Client clientHandler) {
    this.clientHandler = clientHandler;

    model = new DefaultComboBoxModel<>();
    comboBox = new JComboBox<>(model);

    modelPerson = new DefaultComboBoxModel<>();
    comboBoxPerson = new JComboBox<>(modelPerson);

    JFrame frame = new JFrame("Window");

    JPanel buttonsPanel = new JPanel();

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 900);
    frame.setLocationRelativeTo(null);

    JButton create = new JButton("Создать");
    JButton send = new JButton("Отправить");

    ActionListener myButtonsListener = new ButtonsListener(comboBox, comboBoxPerson, clientHandler);

    create.setActionCommand("Создать");
    send.setActionCommand("Отправить");

    create.addActionListener(myButtonsListener);
    send.addActionListener(myButtonsListener);

    // send.addActionListener(myButtonsListenerPerson);

    buttonsPanel.add(create);
    buttonsPanel.add(send);
    buttonsPanel.add(comboBox);

    buttonsPanel.add(comboBoxPerson);
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
      modelPerson.removeAllElements();

      synchronized (Singleton.list) {
        for (int i = 0; i < Singleton.list.size(); i++) {
          model.addElement(i);
        }
      }

      synchronized (Singleton.listPerson) {
        for (int i = 0; i < Singleton.listPerson.size(); i++) {
          modelPerson.addElement(i);
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
