package PortalPage.Swing;

import DAO.DiagnosticTestDAO;
import Exceptions.DatabaseQueryException;
import LandingPage.Components.NotificationPopUp;
import Models.DiagnosticTest;
import Models.Patient;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DiagnosticTestFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(DiagnosticTestFrame.class.getName());

    private final Patient patient;
    private final DiagnosticTestDAO diagnosticTestDAO;
    private List<DiagnosticTest> allTests;
    private JTable diagnosticTestData;
    private DefaultTableModel tableModel;
    private JComboBox<String> testTypeCombo, dateOrderCombo;

    public DiagnosticTestFrame(Patient patient) {
        this.patient = patient;

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

    private void initializeFrame() {
        setTitle("Pruebas Diagnósticas");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Label
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Pruebas Diagnósticas de " + patient.getSurname(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Separator
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2));

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15));

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.CENTER);

        // Test Table
        JPanel tablePanel = createDiagnosticTestsTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(new JLabel("Ordenar por fecha:"));
        dateOrderCombo = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        dateOrderCombo.addActionListener(e -> updateDiagnosticTestsTable());
        panel.add(dateOrderCombo);

        panel.add(Box.createHorizontalStrut(20));

        panel.add(new JLabel("Tipo de prueba:"));
        testTypeCombo = new JComboBox<>(new String[]{"Todos", "Hemograma", "Bioquímica", "Inmunología", "Microbiología"});
        testTypeCombo.addActionListener(e -> updateDiagnosticTestsTable());
        panel.add(testTypeCombo);

        return panel;
    }

    private JPanel createDiagnosticTestsTablePanel() {
        String[] columnNames = {"Fecha", "Tipo de Prueba", "Resultados"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevents editing
            }
        };

        diagnosticTestData = new JTable(tableModel);
        diagnosticTestData.setFont(new Font("Arial", Font.PLAIN, 14)); // Consistent font style
        diagnosticTestData.setRowHeight(25);
        diagnosticTestData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set table header style
        JTableHeader header = diagnosticTestData.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14)); // Bold header text
        header.setBackground(new Color(230, 230, 230)); // Light gray background
        header.setOpaque(true);

        // Apply row striping for better readability
        diagnosticTestData.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating row colors
                }
                ((JLabel) cell).setHorizontalAlignment(SwingConstants.CENTER); // Center align text
                return cell;
            }
        });

        diagnosticTestData.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double-click to open details
                    int row = diagnosticTestData.getSelectedRow();
                    if (row != -1) {
                        openTestDetails(row);
                    }
                }
            }
        });

        updateDiagnosticTestsTable(); // Load table data

        JScrollPane scrollPane = new JScrollPane(diagnosticTestData);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void updateDiagnosticTestsTable() {
        tableModel.setRowCount(0); // Clears table

        try {
            allTests = diagnosticTestDAO.findByPatientDNI(patient.getDNI()); // Fetches from DB
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
                tableModel.addRow(new Object[]{
                        test.getTestDate(), test.getTestType().getValue(), test.getResults()
                });
            }
        }
    }

    private boolean shouldIncludeTest(DiagnosticTest test) {
        String selectedType = (String) testTypeCombo.getSelectedItem();
        return selectedType.equals("Todos") || test.getTestType().getValue().equalsIgnoreCase(selectedType);
    }

    // Opens the detailed test view when a row is double-clicked
    private void openTestDetails(int row) {
        try {
            DiagnosticTest selectedTest = allTests.get(row);

            Optional<Object> detailedTest = diagnosticTestDAO.findDetailedTestByID(
                    selectedTest.getDiagnosticTestID(),
                    selectedTest.getTestType()
            );

            if (detailedTest.isPresent()) {
                new DiagnosticTestDetailsFrame(detailedTest.get());
            } else {
                NotificationPopUp.showErrorMessage(this, "Error", "No se encontró información detallada.");
            }
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error fetching detailed test", e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudo cargar la información de la prueba.");
        }
    }
}
