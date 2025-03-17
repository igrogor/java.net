import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI {
    private JTextArea BolshayaRazdnica;
    private JFrame mainframe;
    private JComboBox<Object> shapeComboBox;


    public GUI() {
        mainframe = new JFrame("Test");
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("");
        b.setBackground(Color.RED);
        BolshayaRazdnica = new JTextArea(15, 10);
        BolshayaRazdnica.setLineWrap(true);
        BolshayaRazdnica.setWrapStyleWord(true);

        shapeComboBox = new JComboBox<>();


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(b);
        panel.add(shapeComboBox);
        panel.add(new JScrollPane(BolshayaRazdnica));

        mainframe.add(panel);
        mainframe.pack();
        mainframe.setLayout(null);
        mainframe.setVisible(true);
    }

    public JComboBox<Object> getShapeComboBox() {
        return shapeComboBox;
    }

    public JTextArea getBolshayaRazdnica() {
        return BolshayaRazdnica;
    }

    public void setButtonActionListener(ActionListener listener) {
        JButton b = (JButton) ((JPanel) mainframe.getContentPane().getComponent(0)).getComponent(0);
        b.addActionListener(listener);
    }
}
