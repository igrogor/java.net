import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class ButtonsListener implements ActionListener {
    private JComboBox<String> comboBox;
    private JComboBox<String> comboBoxPerson;
    private Client clientHandler;

    public ButtonsListener(JComboBox<String> comboBox, JComboBox<String> comboBoxPerson, Client clientHandler) {
        this.comboBox = comboBox;
        this.comboBoxPerson = comboBoxPerson;
        this.clientHandler = clientHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("Создать")) {
            Singleton.Generation();
            GUI.textArea.setText("");
            updateClientTextArea();
        }
        if (command.equals("Отправить")) {
            String selectedIndex = (String) comboBox.getSelectedItem();
            String personId = (String) comboBoxPerson.getSelectedItem();

            // Проверка на выбор объекта и получателя
            if (selectedIndex == null || personId == null) {
                JOptionPane.showMessageDialog(null, "Выберите объект и получателя");
                return;
            }

            // Запрет отправки самому себе
            if (personId.equals(clientHandler.getClientId())) {
                JOptionPane.showMessageDialog(null, "Нельзя отправить объект самому себе");
                return;
            }

            int i = Integer.parseInt(selectedIndex);
            Object selectedObject = Singleton.list.get(i);
            if (selectedObject != null) {
                try {
                    System.out.println("Отправка объекта: " + selectedObject + " клиенту: " + personId);
                    clientHandler.client(selectedObject, personId);
                    System.out.println("Удаление объекта: " + selectedObject);
                    Singleton.removeEl(selectedObject);
                    updateClientTextArea();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Ошибка отправки: " + ex.getMessage());
                }
            }
        }
    }

    private void updateClientTextArea() {
        GUI.textArea.setText("");
        int number = 0;
        synchronized (Singleton.list) {
            for (Object item : Singleton.list) {
                GUI.textArea.append(number + "\t" + Singleton.ObjectToJSON(item) + "\n------\n");
                number++;
            }
        }
    }
}