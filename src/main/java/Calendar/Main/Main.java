package Calendar.Main;

import Calendar.Component.CalendarCustom;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private CalendarCustom calendarPanel;

    public Main() {
        setTitle("Calendario - Citas Médicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Centers the window in the middle of the screen

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        calendarPanel = new CalendarCustom();
        calendarPanel.setBorder(BorderFactory.createLineBorder(new Color(205, 205, 205)));

        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
