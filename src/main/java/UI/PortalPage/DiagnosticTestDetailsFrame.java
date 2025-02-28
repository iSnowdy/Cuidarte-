package UI.PortalPage;

import Database.AaModels.*;
import Utils.Swing.Colors;
import Utils.Utility.CustomLogger;
import Utils.Utility.DiagnosticTestFieldMapper;
import Utils.Utility.PDFReportGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DiagnosticTestDetailsFrame extends JFrame {
    private static final Logger LOGGER = CustomLogger.getLogger(DiagnosticTestDetailsFrame.class);

    private final Object detailedTest;
    private final TestType testType;
    private final List<Method> gettersNames;
    private JButton generatePDFButton;

    public DiagnosticTestDetailsFrame(Object detailedTest) {
        this.detailedTest = detailedTest;
        this.testType = determineTestType(detailedTest);
        this.gettersNames = new ArrayList<>();

        initializeFrame();
        addComponents();
        setVisible(true);
    }

    // Configures the frame settings
    private void initializeFrame() {
        setTitle("Detalles de Prueba Diagnóstica");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Adds all UI components to the frame
    private void addComponents() {
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createDetailsScrollPane(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    // Creates the main panel with a BorderLayout
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }

    // Creates the title panel with a separator
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Detalles de " + testType.getValue(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 10));

        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15));

        return titlePanel;
    }

    // Creates a scrollable panel containing the test details
    private JScrollPane createDetailsScrollPane() {
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createGridBagConstraints();
        generateDetails(detailsPanel, gbc);

        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }

    // Configures GridBagConstraints for layout consistency
    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.weightx = 1.0;
        return gbc;
    }

    // Generates test details dynamically based on reflection
    // It extracts all the fields from the original class and then filters out those that are
    // deemed not valid (ID's)
    // It will then call the getter using that field name and finally, with the mapping util
    // add the translated and unit measure to the UI
    private void generateDetails(JPanel panel, GridBagConstraints gbc) {
        Field[] fields = detailedTest.getClass().getDeclaredFields();
        String[][] fieldMappings = DiagnosticTestFieldMapper.getFieldMappings(testType);

        List<Field> validFields = extractValidFields(fields);

        for (int i = 0; i < validFields.size(); i++) {
            String fieldName = fieldMappings[i][0];
            String unit = fieldMappings[i][1];
            String originalFieldName = formatGetterName(validFields.get(i).getName());

            try {
                Method getterMethod = detailedTest.getClass().getMethod("get" + capitalize(originalFieldName));
                Object value = getterMethod.invoke(detailedTest);
                gettersNames.add(getterMethod);

                addDetailRow(panel, gbc, i, fieldName, value, unit);
            } catch (Exception e) {
                LOGGER.severe("Error retrieving field: " + originalFieldName + " - " + e.getMessage());
            }
        }
    }

    private String formatGetterName(String fieldName) {
        char firstChar = Character.toUpperCase(fieldName.charAt(0));
        String rest = fieldName.substring(1);

        // (eg: gotAst -> GotAst)
        if (rest.matches(".*[A-Z]{2,}.*")) {
            return firstChar + rest.replaceAll("([A-Z]{2,})", "$1");
        }

        return firstChar + rest;
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Extracts valid fields, excluding IDs
    private List<Field> extractValidFields(Field[] fields) {
        List<Field> validFields = new ArrayList<>();
        for (Field field : fields) {
            if (!(field.getName().toLowerCase().startsWith("id") || field.getName().toLowerCase().endsWith("id"))) {
                validFields.add(field);
            }
        }
        return validFields;
    }

    // Adds a row to the details panel with the corresponding value
    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, String fieldName, Object value, String unit) {
        JLabel nameLabel = createDetailLabel(fieldName + ":");

        JComponent valueComponent = (value.toString().length() > 30)
                ? createWrappedLabel(value.toString())
                : createValueLabel(value, unit, fieldName);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(valueComponent, gbc);
    }

    // Wrapping label for long labels such as results
    private JScrollPane createWrappedLabel(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        textArea.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(300, 60));
        return scrollPane;
    }

    // Creates the panel for the PDF generation button
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);

        generatePDFButton = new JButton("Generar informe en PDF");
        generatePDFButton.addActionListener(e -> generatePDFReport());

        buttonPanel.add(Box.createVerticalStrut(30));
        buttonPanel.add(generatePDFButton);

        return buttonPanel;
    }

    // Creates a bold label for the field name
    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Colors.MAIN_APP_COLOUR);
        return label;
    }

    // Creates a value label with color-coded alerts
    private JLabel createValueLabel(Object value, String unit, String fieldName) {
        JLabel label = new JLabel(value.toString() + (unit.isEmpty() ? "" : " " + unit));
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        try {
            double numericValue = Double.parseDouble(value.toString());

            if (isCriticalValue(fieldName, numericValue)) {
                label.setForeground(Color.RED);
            } else if (isSlightlyOutOfRange(fieldName, numericValue)) {
                label.setForeground(Color.ORANGE);
            } else {
                label.setForeground(Color.BLACK);
            }
        } catch (NumberFormatException e) {
            label.setForeground(Color.BLACK); // Default color if value is not numeric
        }

        return label;
    }

    // Determines if a value is critically out of range
    private boolean isCriticalValue(String fieldName, double value) {
        double[] normalRange = DiagnosticTestFieldMapper.getValueRange(fieldName);
        return value < normalRange[0] * 0.7 || value > normalRange[1] * 1.3; // 30% out of range
    }

    // Determines if a value is slightly out of range
    private boolean isSlightlyOutOfRange(String fieldName, double value) {
        double[] normalRange = DiagnosticTestFieldMapper.getValueRange(fieldName);
        return value < normalRange[0] * 0.9 || value > normalRange[1] * 1.1; // 10% out of range
    }


    // Determines the test type based on the detailed test class
    private TestType determineTestType(Object detailedTest) {
        Map<Class<?>, TestType> typeMapping = Map.of(
                BloodCountLab.class, TestType.BLOOD_LAB,
                BiochemistryLab.class, TestType.BIOCHEMISTRY_LAB,
                ImmunologyLab.class, TestType.IMMUNOLOGY_LAB,
                MicrobiologyLab.class, TestType.MICROBIOLOGY_LAB
        );

        return typeMapping.getOrDefault(detailedTest.getClass(), null);
    }

    // Generates a PDF report for the diagnostic test
    private void generatePDFReport() {
        PDFReportGenerator.generateDiagnosticTest(
                detailedTest,
                "C:\\Users\\andyl\\IdeaProjects\\Cuidarte\\src\\main\\resources",
                gettersNames,
                testType
        );
    }
}
