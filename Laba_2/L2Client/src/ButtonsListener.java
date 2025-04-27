import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ButtonsListener implements ActionListener {
    private JComboBox<Object> comboBox;
    private JComboBox<Object> comboBoxPerson;

    

    public ButtonsListener(JComboBox<Object> comboBox, JComboBox<Object> comboBoxPerson) {
        this.comboBox = comboBox;
        this.comboBoxPerson = comboBoxPerson;

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
            int i = (int) comboBox.getSelectedItem();
            int n = (int) comboBoxPerson.getSelectedItem();

            Object selectedObject = Singleton.list.get(i);
            int person = Singleton.listPerson.get(n);

            if (selectedObject != null) {
                try {
                    Client.client(selectedObject, n);
                    Singleton.removeEl(selectedObject);
                    updateClientTextArea();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Ошибка отправки: " + ex.getMessage());
                }
            }
        }
    }

    private void updateClientTextArea() {
        GUI.textArea.setText("");
        int number = 0;
        // Создаем синхронизированную копию списка
        List<Object> copyList;
        synchronized (Singleton.list) {
            copyList = new ArrayList<>(Singleton.list);
        }

        for (Object item : copyList) {
            GUI.textArea.append(number + "\t" + Singleton.ObjectToJSON(item) + "\n------\n");
            number++;
        }
    }

}
