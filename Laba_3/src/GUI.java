import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ComboBoxUpdateListener {
  static JTextArea textArea = new JTextArea();
  public DefaultComboBoxModel<String> model;
  public JComboBox<String> comboBox;
  public DefaultComboBoxModel<String> modelPerson;
  public JComboBox<String> comboBoxPerson;
  public Client clientHandler;

  GUI(Client clientHandler) {
    this.clientHandler = clientHandler;

    model = new DefaultComboBoxModel<>();
    comboBox = new JComboBox<>(model);

    modelPerson = new DefaultComboBoxModel<>();
    comboBoxPerson = new JComboBox<>(modelPerson);

    JFrame frame = new JFrame("Client: " + clientHandler.getClientId());

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

    // buttonsPanel.add(create);
    // buttonsPanel.add(send);
    // buttonsPanel.add(comboBox);
    // buttonsPanel.add(comboBoxPerson);
    buttonsPanel.setVisible(true);

    buttonsPanel.setLayout(new FlowLayout());
    buttonsPanel.add(create);
    buttonsPanel.add(send);
    buttonsPanel.add(comboBox);
    buttonsPanel.add(comboBoxPerson);

    frame.getContentPane().add(BorderLayout.NORTH, buttonsPanel);

    JScrollPane scrollPane = new JScrollPane(textArea);
    frame.getContentPane().add(BorderLayout.CENTER, scrollPane);

    frame.setVisible(true);

    Singleton.setUpdateListener(this);
    onComboBoxUpdateNeeded();
    System.out.println("GUI инициализирована для клиента: " + clientHandler.getClientId());
  }

  @Override
  public void onComboBoxUpdateNeeded() {
    SwingUtilities.invokeLater(() -> {
      model.removeAllElements();
      modelPerson.removeAllElements();

      synchronized (Singleton.list) {
        for (int i = 0; i < Singleton.list.size(); i++) {
          model.addElement(String.valueOf(i));
        }
      }

      synchronized (Singleton.listPerson) {
        System.out.println("Обновление comboBoxPerson: " + Singleton.listPerson);
        for (String id : Singleton.listPerson) {
          modelPerson.addElement(id);
        }
      }

      // Принудительное обновление комбо-боксов
      comboBox.revalidate();
      comboBox.repaint();
      comboBoxPerson.revalidate();
      comboBoxPerson.repaint();

      // Обновление текстовой области
      textArea.setText("");
      synchronized (Singleton.list) {
        for (Object item : Singleton.list) {
          textArea.append(Singleton.ObjectToJSON(item) + "\n------\n");
        }
      }
    });
  }
}