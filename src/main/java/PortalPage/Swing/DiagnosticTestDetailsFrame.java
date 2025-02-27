package PortalPage.Swing;

import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class DiagnosticTestDetailsFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(DiagnosticTestDetailsFrame.class.getName());

    private final Object detailedTest;
    private JButton generatePDFButton;

    public DiagnosticTestDetailsFrame(Object detailedTest) {
        this.detailedTest = detailedTest;

        initializeFrame();
        addComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Detalle de Prueba Diagnóstica");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Detalle de Prueba Diagnóstica", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(new JSeparator(), BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        detailsPanel.setBackground(Color.WHITE);

        // Auto-generate details dynamically
        generateDetails(detailsPanel);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        generatePDFButton = new JButton("Generar informe en PDF");
        //generatePDFButton.addActionListener(e -> generateMedicalReport());
        mainPanel.add(generatePDFButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void generateDetails(JPanel panel) {
        try {
            Field[] fields = detailedTest.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);

                Object value = field.get(detailedTest); // Retrieves the attribute

                // If the field is an ID, ignore it and go to the next one
                if (field.getName().toLowerCase().contains("id") || value == null) continue;

                // Properly formats the attribute and creates the label with it
                panel.add(new JLabel(formatFieldName(field.getName()) + ":"));

                // If the field is a String, and a long one, then creates a JTextArea
                // Otherwise, a simple JLabel
                if (value instanceof String && ((String) value).length() > 30) {
                    JTextArea textArea = new JTextArea(value.toString());
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setEditable(false);

                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 100));
                    panel.add(scrollPane);
                } else {
                    panel.add(new JLabel(value.toString()));
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.severe("Error accessing fields in " + detailedTest.getClass().getSimpleName());
        }
    }

    // Correct formatting for the field since we extracted it from the metadata of the Object
    private String formatFieldName(String fieldName) {
        String formatted = fieldName.replaceAll("([a-z])([A-Z])", "$1 $2"); // CamelCase -> Words
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1); // First letter capitalized
    }
}
