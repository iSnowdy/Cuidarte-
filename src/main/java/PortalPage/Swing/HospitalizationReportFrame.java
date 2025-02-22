package PortalPage.Swing;

import PortalPage.TempModels.HospitalizationReport;
import PortalPage.TempModels.TestPatient;
import Utils.FilterComponent;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class HospitalizationReportFrame extends JFrame {
    private TestPatient patient;
    private List<HospitalizationReport> allReports;
    private DefaultListModel<HospitalizationReport> reportListModel;
    private JList<HospitalizationReport> reportJList;

    // Reusable FilterPanel for hospitalization reports
    private FilterComponent filterPanel;

    // Filter options
    private static final String FILTER_BY_DATE = "Fecha";
    private static final String FILTER_BY_DIAGNOSIS = "Diagnóstico";

    public HospitalizationReportFrame(TestPatient patient) {
        this.patient = patient;
        setTitle("Informes de Hospitalización de " + patient.getName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        initializeDummyData();
        initComponents();
        setVisible(true);
    }

    // Create dummy hospitalization reports for testing
    private void initializeDummyData() {
        allReports = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        cal.set(2024, Calendar.JANUARY, 15);
        allReports.add(new HospitalizationReport(1, patient, cal.getTime(), addDays(cal.getTime(), 7),
                "Neumonía", "Antibióticos, Oxigenoterapia", "Mejoría progresiva."));
        cal.set(2023, Calendar.DECEMBER, 1);
        allReports.add(new HospitalizationReport(2, patient, cal.getTime(), addDays(cal.getTime(), 5),
                "Apendicitis", "Apendicectomía", "Alta sin complicaciones."));
        cal.set(2023, Calendar.OCTOBER, 20);
        allReports.add(new HospitalizationReport(3, patient, cal.getTime(), addDays(cal.getTime(), 10),
                "Fractura de fémur", "Cirugía ortopédica", "Rehabilitación necesaria."));
    }

    // Helper to add days to a Date
    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    // Initialize UI components using a vertical BoxLayout
    private void initComponents() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20,20,20,20));

        // Title label
        JLabel titleLabel = new JLabel("Informes de Hospitalización de " + patient.getName(), JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10,0,20,0));
        container.add(titleLabel);

        // Create map for filter criteria for hospitalization reports
        Map<String, JComponent> criteriaMap = new HashMap<>();
        // For "Fecha", combo for Ascendente/Descendente
        JComboBox<String> dateOrderCombo = new JComboBox<>(new String[]{"Ascendente", "Descendente"});
        dateOrderCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        dateOrderCombo.setMaximumSize(new Dimension(150, 25));
        dateOrderCombo.addActionListener(e -> updateReportList());
        criteriaMap.put(FILTER_BY_DATE, dateOrderCombo);
        // For "Diagnóstico", combo with dummy options
        JComboBox<String> diagnosisCombo = new JComboBox<>(new String[]{"Todos", "Neumonía", "Apendicitis", "Fractura"});
        diagnosisCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        diagnosisCombo.setMaximumSize(new Dimension(150, 25));
        diagnosisCombo.addActionListener(e -> updateReportList());
        criteriaMap.put(FILTER_BY_DIAGNOSIS, diagnosisCombo);

        String[] filterTypes = {FILTER_BY_DATE, FILTER_BY_DIAGNOSIS};
        filterPanel = new FilterComponent(filterTypes, criteriaMap);
        container.add(filterPanel);

        container.add(Box.createVerticalStrut(40));

        // Add report list panel
        container.add(createReportListPanel());

        add(container);
    }

    // Create the panel containing the hospitalization reports list with a scroll pane
    private JPanel createReportListPanel() {
        reportListModel = new DefaultListModel<>();
        reportJList = new JList<>(reportListModel);
        reportJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportJList.setFont(new Font("Arial", Font.PLAIN, 16));
        updateReportList();

        reportJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    HospitalizationReport selected = reportJList.getSelectedValue();
                    if (selected != null) {
                        new HospitalizationReportDetailsFrame(selected);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(reportJList), BorderLayout.CENTER);
        return panel;
    }

    // Filter and update the hospitalization report list based on the selected criteria
    private void updateReportList() {
        String filterType = filterPanel.getSelectedFilterType();
        List<HospitalizationReport> filtered = new ArrayList<>(allReports);

        if (FILTER_BY_DATE.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String order = (String) combo.getSelectedItem();
            filtered.sort(Comparator.comparing(HospitalizationReport::getAdmissionDate));
            if ("Descendente".equals(order)) {
                Collections.reverse(filtered);
            }
        } else if (FILTER_BY_DIAGNOSIS.equals(filterType)) {
            JComboBox<String> combo = (JComboBox<String>) filterPanel.getCurrentCriteriaComponent();
            String diagnosis = (String) combo.getSelectedItem();
            if (diagnosis != null && !diagnosis.equalsIgnoreCase("Todos")) {
                filtered.removeIf(report -> !report.getDiagnosis().equalsIgnoreCase(diagnosis));
            }
        }

        reportListModel.clear();
        for (HospitalizationReport hr : filtered) {
            reportListModel.addElement(hr);
        }
    }
}
