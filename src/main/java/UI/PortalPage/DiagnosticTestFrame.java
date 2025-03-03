package UI.PortalPage;

import Database.DAO.DiagnosticTestDAO;
import Exceptions.DatabaseQueryException;
import Components.NotificationPopUp;
import Database.Models.DiagnosticTest;
import Database.Models.Patient;
import Utils.Swing.Colors;
import Utils.Utility.CustomLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Fonts.MAIN_FONT;
import static Utils.Swing.Fonts.PATIENT_PORTAL_SUB_PANEL_FONT;

public class DiagnosticTestFrame extends JFrame {
    private static final Logger LOGGER = CustomLogger.getLogger(DiagnosticTestFrame.class);

    private final Patient patient;
    private final DiagnosticTestDAO diagnosticTestDAO;

    private List<DiagnosticTest> allTests;
    private List<DiagnosticTest> filteredTests;

    private JTable diagnosticTestData;
    private DefaultTableModel tableModel;
    private JComboBox<String> testTypeCombo, dateOrderCombo;

    public DiagnosticTestFrame(Patient patient) {
        this.patient = patient;
        this.filteredTests = new ArrayList<>();

        try {
            this.diagnosticTestDAO = new DiagnosticTestDAO();
            this.allTests = diagnosticTestDAO.findByPatientDNI(patient.getDNI());
        } catch (DatabaseQueryException e) {
            throw new RuntimeException("Error fetching diagnostic tests", e);
        }

        initializeFrame();
        addComponents();
        updateDiagnosticTestsTable();
        setVisible(true);
    }

    // Configure window properties
    private void initializeFrame() {
        setTitle("Pruebas Diagnósticas");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Adds all UI components
    private void addComponents() {
        JPanel mainPanel = createMainPanel();
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createFilterPanel(), BorderLayout.CENTER);
        mainPanel.add(createDiagnosticTestsTablePanel(), BorderLayout.SOUTH);
        add(mainPanel);
    }

    // Creates the main panel with a BorderLayout
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return mainPanel;
    }

    // Creates the title panel with a separator
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Pruebas Diagnósticas de " + patient.getSurname(), JLabel.CENTER);
        titleLabel.setFont(PATIENT_PORTAL_SUB_PANEL_FONT);
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2));

        // Spacing between each element
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15));

        return titlePanel;
    }

    // Creates the filter panel for sorting and filtering test data
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(new JLabel("Ordenar por fecha:"));
        dateOrderCombo = createDateOrderComboBox();
        panel.add(dateOrderCombo);

        panel.add(Box.createHorizontalStrut(20));

        panel.add(new JLabel("Tipo de prueba:"));
        testTypeCombo = createTestTypeComboBox();
        panel.add(testTypeCombo);

        return panel;
    }

    // Creates the date order combo box
    private JComboBox<String> createDateOrderComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        comboBox.addActionListener(e -> updateDiagnosticTestsTable());
        return comboBox;
    }

    // Creates the test type combo box
    private JComboBox<String> createTestTypeComboBox() {
        JComboBox<String> comboBox =
                new JComboBox<>(new String[]{"Todos", "Hemograma", "Bioquímica", "Inmunología", "Microbiología", "Radiografía"});
        comboBox.addActionListener(e -> updateDiagnosticTestsTable());
        return comboBox;
    }

    // Creates the table panel with test results
    private JPanel createDiagnosticTestsTablePanel() {
        tableModel = new DefaultTableModel(new String[]{"Fecha", "Tipo de Prueba", "Resultados"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        diagnosticTestData = new JTable(tableModel);
        diagnosticTestData.setFont(new Font("Arial", Font.PLAIN, 14));
        diagnosticTestData.setRowHeight(25);
        diagnosticTestData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        customizeTableAppearance();

        updateDiagnosticTestsTable();
        addTableClickListener();

        JScrollPane scrollPane = new JScrollPane(diagnosticTestData);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Customizes the appearance of the table
    private void customizeTableAppearance() {
        JTableHeader header = diagnosticTestData.getTableHeader();
        header.setFont(MAIN_FONT.deriveFont(Font.BOLD));
        header.setBackground(new Color(230, 230, 230));
        header.setOpaque(true);

        // Makes it so one is greyed, the other not
        diagnosticTestData.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240));
                }
                ((JLabel) cell).setHorizontalAlignment(SwingConstants.CENTER);
                return cell;
            }
        });
    }

    // Adds a listener to detect double-clicks on a row
    private void addTableClickListener() {
        diagnosticTestData.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = diagnosticTestData.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Use the filtered list to get the correct test
                        DiagnosticTest selectedTest = filteredTests.get(selectedRow);
                        openTestDetails(selectedTest);
                    }
                }
            }
        });
    }

    // Updates the table based on selected filters
    private void updateDiagnosticTestsTable() {
        tableModel.setRowCount(0);
        filteredTests.clear(); // Resets the list containing the filtered objects

        try {
            allTests = diagnosticTestDAO.findByPatientDNI(patient.getDNI());
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading diagnostic tests from DB", e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudieron cargar las pruebas diagnósticas.");
            return;
        }

        boolean ascending = dateOrderCombo.getSelectedItem().equals("Ascendente");
        allTests.sort((t1, t2) -> ascending ? t1.getTestDate().compareTo(t2.getTestDate())
                : t2.getTestDate().compareTo(t1.getTestDate()));

        for (DiagnosticTest test : allTests) {
            if (shouldIncludeTest(test)) {
                filteredTests.add(test);
                tableModel.addRow(new Object[]{
                        test.getTestDate(), test.getTestType().getValue(), test.getResults()
                });
            }
        }
    }

    // Determines if a test should be included in the table based on filters
    private boolean shouldIncludeTest(DiagnosticTest test) {
        String selectedType = (String) testTypeCombo.getSelectedItem();
        return selectedType.equals("Todos") || test.getTestType().getValue().equalsIgnoreCase(selectedType);
    }

    // Opens the detailed test view when a row is double-clicked
    private void openTestDetails(DiagnosticTest selectedTest) {
        try {
            Optional<Object> detailedTest = diagnosticTestDAO.findDetailedTestByID(
                    selectedTest.getDiagnosticTestID(),
                    selectedTest.getTestType()
            );

            if (detailedTest.isPresent()) {
                new DiagnosticTestDetailsFrame(detailedTest.get());
            } else {
                NotificationPopUp.showErrorMessage(
                        this,
                        "Error",
                        "No se encontró información detallada.");
            }
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error fetching detailed test", e);
        }
    }
}
