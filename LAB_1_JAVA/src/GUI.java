import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI {
    private JTextArea BolshayaRazdnica;
    private JFrame mainframe;

    public GUI() {
        mainframe = new JFrame("Test");
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("but");

        BolshayaRazdnica = new JTextArea(15, 10);
        BolshayaRazdnica.setLineWrap(true);
        BolshayaRazdnica.setWrapStyleWord(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(b);
        panel.add(new JScrollPane(BolshayaRazdnica));

        mainframe.add(panel);
        mainframe.pack();
        mainframe.setLayout(null);
        mainframe.setVisible(true);
    }


    public JTextArea getBolshayaRazdnica() {
        return BolshayaRazdnica;
    }

    public void setButtonActionListener(ActionListener listener) {
        JButton b = (JButton) ((JPanel) mainframe.getContentPane().getComponent(0)).getComponent(0);
        b.addActionListener(listener);
    }
}



// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;


// import java.awt.event.ActionListener;
// import javax.swing.*;

// public class GUI {
//     private JTextArea BolshayaRazdnica;
//     private JFrame mainframe;

//     public GUI() {
//         mainframe = new JFrame("Test");
//         mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         // Кнопка
//         JButton b = new JButton("but");

//         // Текстовое поле
//         BolshayaRazdnica = new JTextArea(15, 10);
//         BolshayaRazdnica.setLineWrap(true);
//         BolshayaRazdnica.setWrapStyleWord(true);

//         // Панель для организации компонентов
//         JPanel panel = new JPanel();
//         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//         panel.add(b);
//         panel.add(new JScrollPane(BolshayaRazdnica));

//         // Добавляем панель в главное окно
//         mainframe.add(panel);
//         mainframe.pack();
//         mainframe.setLocationRelativeTo(null);
//         mainframe.setVisible(true);
//     }

//     public JTextArea getBolshayaRazdnica() {
//         return BolshayaRazdnica;
//     }

//     public void setButtonActionListener(ActionListener listener) {
//         // Получаем кнопку из панели
//         JButton b = (JButton) ((JPanel) mainframe.getContentPane().getComponent(0)).getComponent(0);
//         b.addActionListener(listener);
//     }
// }

