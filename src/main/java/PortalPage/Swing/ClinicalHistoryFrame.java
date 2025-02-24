package PortalPage.Swing;

import Models.Patient;
import PortalPage.TempModels.ClinicalHistory;
import Utils.FilterComponent;
import Utils.Swing.Colors;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ClinicalHistoryFrame extends JFrame {
    private Patient patient;
    private List<ClinicalHistory> allHistories;
    private DefaultListModel<ClinicalHistory> historyListModel;
    private JList<ClinicalHistory> historyJList;

    // Reusable FilterPanel for managing filter UI
    private FilterComponent filterPanel;

    // Filter constants
    private static final String FILTER_BY_DATE = "Fecha";
    private static final String FILTER_BY_SPECIALTY = "Especialidad";
    private static final String FILTER_BY_DOCTOR = "Doctor";

    public ClinicalHistoryFrame(Patient patient) {
        this.patient = patient;
        setTitle("Historial Clínico");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeDummyData();
        initComponents();
        setVisible(true);
    }

    // Create dummy clinical histories for testing
    private void initializeDummyData() {
        allHistories = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 10);
        allHistories.add(new ClinicalHistory(1, patient, "Detalles de la historia 1", "Dr. García", "Cardiología", cal.getTime()));
        cal.set(2024, Calendar.JANUARY, 5);
        allHistories.add(new ClinicalHistory(2, patient, "Detalles de la historia 2", "Dra. López", "Neurología", cal.getTime()));
        cal.set(2023, Calendar.OCTOBER, 29);
        allHistories.add(new ClinicalHistory(3, patient, "Detalles de la historia 3", "Dr. Pérez", "Oncología", cal.getTime()));
        cal.set(2023, Calendar.DECEMBER, 15);
        allHistories.add(new ClinicalHistory(4, patient, "Detalles de la historia 4", "Dr. Sánchez", "Pediatría", cal.getTime()));
        cal.set(2023, Calendar.NOVEMBER, 20);
        allHistories.add(new ClinicalHistory(5, patient, "Detalles de la historia 5", "Dra. Martínez", "Ginecología", cal.getTime()));
    }

    // Initialize UI components and layout using a vertical BoxLayout
    private void initComponents() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title label: "Historia Clínica de [patient name]"
        JLabel titleLabel = new JLabel("Historia Clínica de " + patient.getSurname(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        container.add(titleLabel);

        // Create a map of criteria controls for the filter panel
        Map<String, JComponent> criteriaMap = new HashMap<>();
        // For "Fecha" filter: a combo box for Ascendente/Descendente order
        JComboBox<String> dateOrderCombo = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        dateOrderCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        dateOrderCombo.setMaximumSize(new Dimension(150, 25));
        dateOrderCombo.addActionListener(e -> updateHistoryList());
        criteriaMap.put(FILTER_BY_DATE, dateOrderCombo);

        // For "Especialidad" filter: a combo box with dummy specialties
        JComboBox<String> specialtyCombo = new JComboBox<>(new String[]{"Todos", "Cardiología", "Neurología", "Oncología", "Pediatría", "Ginecología"});
        specialtyCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        specialtyCombo.setMaximumSize(new Dimension(150, 25));
        specialtyCombo.addActionListener(e -> updateHistoryList());
        criteriaMap.put(FILTER_BY_SPECIALTY, specialtyCombo);

        // For "Doctor" filter: a combo box for alphabetical order
        JComboBox<String> doctorCombo = new JComboBox<>(new String[]{"Alfabético"});
        doctorCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        doctorCombo.setMaximumSize(new Dimension(150, 25));
        doctorCombo.addActionListener(e -> updateHistoryList());
        criteriaMap.put(FILTER_BY_DOCTOR, doctorCombo);

        // Instantiate FilterPanel with filter types and criteria map
        String[] filterTypes = {FILTER_BY_DATE, FILTER_BY_SPECIALTY, FILTER_BY_DOCTOR};
        filterPanel = new FilterComponent(filterTypes, criteriaMap);
        container.add(filterPanel);

        container.add(Box.createVerticalStrut(40));

        // Add the clinical history list panel
        container.add(createHistoryListPanel());

        add(container);
    }

    // Create the panel containing the clinical histories list with a scroll pane
    private JPanel createHistoryListPanel() {
        historyListModel = new DefaultListModel<>();
        historyJList = new JList<>(historyListModel);
        historyJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyJList.setFont(new Font("Arial", Font.PLAIN, 16));
        updateHistoryList();

        historyJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    ClinicalHistory selected = historyJList.getSelectedValue();
                    if (selected != null) {
                        new ClinicalHistoryDetailsFrame(selected);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(historyJList), BorderLayout.CENTER);
        return panel;
    }

    // Filter and update the clinical history list based on the selected criteria
    private void updateHistoryList() {
        String filterType = filterPanel.getSelectedFilterType();
        List<ClinicalHistory> filtered = new ArrayList<>(allHistories);

        if (FILTER_BY_DATE.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String order = (String) combo.getSelectedItem();
            filtered.sort(Comparator.comparing(ClinicalHistory::getDate));
            if ("Descendente".equals(order)) {
                Collections.reverse(filtered);
            }
        } else if (FILTER_BY_SPECIALTY.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String specialty = (String) combo.getSelectedItem();
            if (specialty != null && !specialty.equalsIgnoreCase("Todos")) {
                filtered.removeIf(ch -> !ch.getSpecialty().toLowerCase().contains(specialty.toLowerCase()));
            }
        } else if (FILTER_BY_DOCTOR.equals(filterType)) {
            filtered.sort(Comparator.comparing(ClinicalHistory::getDoctorName));
        }

        historyListModel.clear();
        for (ClinicalHistory ch : filtered) {
            historyListModel.addElement(ch);
        }
    }
}
