package PortalPage.Swing;

import DAO.DoctorDAO;
import DAO.MedicalReportDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Models.Doctor;
import Models.MedicalReport;
import Models.Patient;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClinicalHistoryFrame extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(ClinicalHistoryFrame.class.getName());

    private final Patient patient;
    private final MedicalReportDAO medicalReportDAO;
    private final DoctorDAO doctorDAO;

    private List<MedicalReport> medicalReports;
    private Map<String, String> doctorSpecialties; // Maps doctor names to their specialties

    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    private JComboBox<String> specialtyCombo;
    private JComboBox<String> doctorCombo;
    private JTextField motiveFilterField; // Text field for filtering by consultation reason

    public ClinicalHistoryFrame(Patient patient) {
        this.patient = patient;
        try {
            this.medicalReportDAO = new MedicalReportDAO();
            this.doctorDAO = new DoctorDAO();
        } catch (DatabaseOpeningException e) {
            throw new RuntimeException("Error initializing DAOs", e);
        }

        setTitle("Historial Clínico");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Open in fullscreen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadMedicalReports();
        initComponents();
        setVisible(true);
    }

    // Load medical reports and doctor information
    private void loadMedicalReports() {
        try {
            medicalReports = medicalReportDAO.findByPatientDNI(patient.getDNI());
            doctorSpecialties = new HashMap<>();

            for (MedicalReport report : medicalReports) {
                String doctorDNI = report.getDoctorDNI();
                Doctor doctor = doctorDAO.findById(doctorDNI).orElse(null);
                if (doctor != null) {
                    String doctorName = doctor.getFirstName() + " " + doctor.getSurname();
                    doctorSpecialties.put(doctorName, doctor.getSpeciality());
                    report.setDoctorDNI(doctorName); // Store doctor name instead of DNI. Kinda dirty
                }
            }

            LOGGER.info("Fetched " + medicalReports.size() + " medical reports for patient: " + patient.getDNI());
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error fetching medical reports", e);
            medicalReports = new ArrayList<>();
        }
    }

    // Initialize UI components
    private void initComponents() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Section with Spacing
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        String fullName = patient.getFirstName() + " " + patient.getSurname();
        JLabel titleLabel = new JLabel("Historia Clínica de " + fullName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // Add padding around the title

        // Separator for visual distinction
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2)); // Ensure it spans full width

        // Add padding using vertical spacing
        titlePanel.add(Box.createVerticalStrut(10)); // Space above title
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10)); // Space between title and separator
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15)); // Space between separator and filters

        container.add(titlePanel, BorderLayout.NORTH);
        container.add(createFilterPanel(), BorderLayout.CENTER);
        container.add(createHistoryTablePanel(), BorderLayout.SOUTH);

        add(container);
    }


    // Create filter panel with specialty, doctor, and motive filters
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        // Specialty filter
        specialtyCombo = new JComboBox<>(new String[]{"All"});
        Set<String> uniqueSpecialties = new HashSet<>(doctorSpecialties.values());
        for (String specialty : uniqueSpecialties) {
            specialtyCombo.addItem(specialty);
        }
        specialtyCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        specialtyCombo.addActionListener(e -> updateHistoryTable());

        // Doctor filter
        doctorCombo = new JComboBox<>(new String[]{"All"});
        Set<String> uniqueDoctors = new HashSet<>(doctorSpecialties.keySet());
        for (String doctor : uniqueDoctors) {
            doctorCombo.addItem(doctor);
        }
        doctorCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        doctorCombo.addActionListener(e -> updateHistoryTable());

        // Motive filter (Text Field)
        motiveFilterField = new JTextField(20);
        motiveFilterField.setFont(new Font("Arial", Font.PLAIN, 16));
        motiveFilterField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateHistoryTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateHistoryTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateHistoryTable(); }
        });

        panel.add(new JLabel("Especialidad:"));
        panel.add(specialtyCombo);
        panel.add(new JLabel("Doctor:"));
        panel.add(doctorCombo);
        panel.add(new JLabel("Motivo de consulta:"));
        panel.add(motiveFilterField);

        return panel;
    }

    // Create the history table panel
    private JPanel createHistoryTablePanel() {
        String[] columnNames = {"Fecha", "Especialidad", "Doctor", "Motivo de Consulta"};
        historyTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent table editing
            }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 14));
        historyTable.setRowHeight(25);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateHistoryTable();

        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // Double click to open details
                    int selectedRow = historyTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        MedicalReport selectedReport = medicalReports.get(selectedRow);
                        new ClinicalHistoryDetailsFrame(selectedReport);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(historyTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Update the history table based on selected filters
    private void updateHistoryTable() {
        String selectedSpecialty = (String) specialtyCombo.getSelectedItem();
        String selectedDoctor = (String) doctorCombo.getSelectedItem();
        String motiveFilter = motiveFilterField.getText().trim().toLowerCase();

        historyTableModel.setRowCount(0);

        for (MedicalReport report : medicalReports) {
            String doctorName = report.getDoctorDNI();
            String specialty = doctorSpecialties.getOrDefault(doctorName, "Unknown");
            String motive = report.getAppointmentMotive().toLowerCase();

            if (!"All".equals(selectedSpecialty) && !specialty.equals(selectedSpecialty)) continue;
            if (!"All".equals(selectedDoctor) && !doctorName.equals(selectedDoctor)) continue;
            if (!motive.contains(motiveFilter)) continue;

            historyTableModel.addRow(new Object[]{
                    report.getVisitDate(),
                    specialty,
                    doctorName,
                    report.getAppointmentMotive()
            });
        }
    }
}