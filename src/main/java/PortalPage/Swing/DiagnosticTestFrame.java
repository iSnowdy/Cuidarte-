package PortalPage.Swing;

import Models.Patient;
import PortalPage.Components.FilterComponent;
import PortalPage.TempModels.DiagnosticTest;
import Models.Enums.TestType;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class DiagnosticTestFrame extends JFrame {
    // Test patient for which diagnostic tests are shown
    private Patient patient;
    // List of all diagnostic tests (dummy data)
    private List<DiagnosticTest> allTests;
    private DefaultListModel<DiagnosticTest> testListModel;
    private JList<DiagnosticTest> testJList;

    // Reusable FilterPanel for managing filter UI
    private FilterComponent filterPanel;

    // Filter constants
    private static final String FILTER_BY_DATE = "Fecha";
    private static final String FILTER_BY_TESTTYPE = "Tipo de prueba";

    public DiagnosticTestFrame(Patient patient) {
        this.patient = patient;
        setTitle("Pruebas Diagnósticas de " + patient.getSurname());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeDummyData();
        initComponents();
        setVisible(true);
    }

    // Create dummy diagnostic tests for testing
    private void initializeDummyData() {
        allTests = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 10);
        allTests.add(new DiagnosticTest(1, patient, "Resultados de hemograma normal.", TestType.BLOOD_LAB, cal.getTime()));
        cal.set(2024, Calendar.JANUARY, 5);
        allTests.add(new DiagnosticTest(2, patient, "Valores de bioquímica alterados.", TestType.BIOCHEMISTRY_LAB, cal.getTime()));
        cal.set(2023, Calendar.OCTOBER, 29);
        allTests.add(new DiagnosticTest(3, patient, "Resultados inmunológicos normales.", TestType.IMMUNOLOGY_LAB, cal.getTime()));
        cal.set(2023, Calendar.DECEMBER, 15);
        allTests.add(new DiagnosticTest(4, patient, "Cultivo de microbiología positivo.", TestType.MICROBIOLOGY_LAB, cal.getTime()));
        cal.set(2023, Calendar.NOVEMBER, 20);
        allTests.add(new DiagnosticTest(5, patient, "Hemograma indica anemia leve.", TestType.BLOOD_LAB, cal.getTime()));
    }

    // Initialize UI components using a vertical BoxLayout
    private void initComponents() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title label
        JLabel titleLabel = new JLabel("Pruebas Diagnósticas de " + patient.getSurname(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        container.add(titleLabel);

        // Create a map of criteria controls for the FilterPanel
        Map<String, JComponent> criteriaMap = new HashMap<>();
        // For "Fecha", a combo box for Ascendente/Descendente order
        JComboBox<String> dateOrderCombo = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        dateOrderCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        dateOrderCombo.setMaximumSize(new Dimension(150, 25));
        dateOrderCombo.addActionListener(e -> updateTestList());
        criteriaMap.put(FILTER_BY_DATE, dateOrderCombo);
        // For "Tipo de prueba", a combo box with options from TestType enum (plus "Todos")
        String[] testTypeOptions = new String[TestType.values().length + 1];
        testTypeOptions[0] = "Todos";
        for (int i = 0; i < TestType.values().length; i++) {
            testTypeOptions[i + 1] = TestType.values()[i].getValue();
        }
        JComboBox<String> testTypeCombo = new JComboBox<>(testTypeOptions);
        testTypeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        testTypeCombo.setMaximumSize(new Dimension(150, 25));
        testTypeCombo.addActionListener(e -> updateTestList());
        criteriaMap.put(FILTER_BY_TESTTYPE, testTypeCombo);

        // Instantiate the reusable FilterPanel with filter types and criteria controls
        String[] filterTypes = {FILTER_BY_DATE, FILTER_BY_TESTTYPE};
        filterPanel = new FilterComponent(filterTypes, criteriaMap);
        container.add(filterPanel);

        container.add(Box.createVerticalStrut(40));

        // Add diagnostic test list panel
        container.add(createTestListPanel());

        add(container);
    }

    // Create the panel containing the diagnostic test list with a scroll pane
    private JPanel createTestListPanel() {
        testListModel = new DefaultListModel<>();
        testJList = new JList<>(testListModel);
        testJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        testJList.setFont(new Font("Arial", Font.PLAIN, 16));
        updateTestList();

        // Double-click to open detail frame
        testJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    DiagnosticTest selected = testJList.getSelectedValue();
                    if (selected != null) {
                        new DiagnosticTestDetailsFrame(selected);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(testJList), BorderLayout.CENTER);
        return panel;
    }

    // Filter and update the diagnostic test list based on selected criteria
    private void updateTestList() {
        String filterType = filterPanel.getSelectedFilterType();
        List<DiagnosticTest> filtered = new ArrayList<>(allTests);

        if (FILTER_BY_DATE.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String order = (String) combo.getSelectedItem();
            filtered.sort(Comparator.comparing(DiagnosticTest::getTestDate));
            if ("Descendente".equals(order)) {
                Collections.reverse(filtered);
            }
        } else if (FILTER_BY_TESTTYPE.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String selectedType = (String) combo.getSelectedItem();
            if (selectedType != null && !selectedType.equalsIgnoreCase("Todos")) {
                filtered.removeIf(dt -> !dt.getTestType().getValue().equalsIgnoreCase(selectedType));
            }
        }

        testListModel.clear();
        for (DiagnosticTest dt : filtered) {
            testListModel.addElement(dt);
        }
    }
}
