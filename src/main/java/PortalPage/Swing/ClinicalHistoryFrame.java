package PortalPage.Swing;

import DAO.DoctorDAO;
import DAO.MedicalReportDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Models.Doctor;
import Models.MedicalReport;
import Models.Patient;
import Utils.Swing.Colors;
import Utils.Utility.CustomLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Fonts.COMBOBOX_FONT;
import static Utils.Swing.Fonts.MAIN_FONT;

public class ClinicalHistoryFrame extends JFrame {
    private static final Logger LOGGER = CustomLogger.getLogger(ClinicalHistoryFrame.class);

    private final Patient patient;
    private final MedicalReportDAO medicalReportDAO;
    private final DoctorDAO doctorDAO;

    private List<MedicalReport> medicalReports;
    private Map<String, String> doctorSpecialties; // Maps doctor names to their specialties

    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    private JComboBox<String> specialityCombo;
    private JComboBox<String> doctorCombo;
    private JTextField motiveFilterField;

    public ClinicalHistoryFrame(Patient patient) {
        this.patient = patient;
        try {
            this.medicalReportDAO = new MedicalReportDAO();
            this.doctorDAO = new DoctorDAO();
        } catch (DatabaseOpeningException e) {
            throw new RuntimeException("Error initializing DAOs", e);
        }

        setTitle("Historial Clínico");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

            // Iterates all the medical reports of that patient to find the doctor's name and speciality
            for (MedicalReport report : medicalReports) {
                String doctorDNI = report.getDoctorDNI();
                Doctor doctor = doctorDAO.findById(doctorDNI).orElse(null);
                if (doctor != null) {
                    String doctorName = doctor.getFirstName() + " " + doctor.getSurname();
                    doctorSpecialties.put(doctorName, doctor.getSpeciality());
                    report.setDoctorDNI(doctorName); // Store doctor name instead of DNI
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

        // Title | Filters | List of Medical Reports
        container.add(createTitlePanel(), BorderLayout.NORTH);
        container.add(createFilterPanel(), BorderLayout.CENTER);
        container.add(createHistoryTablePanel(), BorderLayout.SOUTH);

        add(container);
    }

    // Create title panel with separator
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.WHITE);

        String fullName = patient.getFirstName() + " " + patient.getSurname();
        JLabel titleLabel = new JLabel("Historia Clínica de " + fullName, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Separator between the title and the filters
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setMaximumSize(new Dimension(Short.MAX_VALUE, 2));

        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(10));
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(15));

        return titlePanel;
    }

    // Create filter panel with speciality, doctor, and motive filters
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(Color.WHITE);

        specialityCombo = createSpecialityCombo();
        doctorCombo = createDoctorComboBox();
        motiveFilterField = createMotiveFilterField();

        panel.add(new JLabel("Especialidad:"));
        panel.add(specialityCombo);
        panel.add(new JLabel("Doctor:"));
        panel.add(doctorCombo);
        panel.add(new JLabel("Motivo de consulta:"));
        panel.add(motiveFilterField);

        return panel;
    }

    // Create speciality combo box
    private JComboBox<String> createSpecialityCombo() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"All"});
        // A HashSet ensures that even if there are duplicates of a speciality,
        // we only show it once
        Set<String> uniqueSpecialties = new HashSet<>(doctorSpecialties.values());
        for (String speciality : uniqueSpecialties) {
            comboBox.addItem(speciality);
        }
        comboBox.setFont(COMBOBOX_FONT);
        comboBox.addActionListener(e -> updateHistoryTable());
        return comboBox;
    }

    // Create doctor combo box
    private JComboBox<String> createDoctorComboBox() {
        JComboBox<String> comboBox = new JComboBox<>(new String[]{"All"});
        Set<String> uniqueDoctors = new HashSet<>(doctorSpecialties.keySet());
        for (String doctor : uniqueDoctors) {
            comboBox.addItem(doctor);
        }
        comboBox.setFont(COMBOBOX_FONT);
        comboBox.addActionListener(e -> updateHistoryTable());
        return comboBox;
    }

    // Create motive filter text field. Dynamic search type of filter
    // Every time one of those actions is done, it will call the method to update the table
    private JTextField createMotiveFilterField() {
        JTextField textField = new JTextField(20);
        textField.setFont(COMBOBOX_FONT);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateHistoryTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateHistoryTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateHistoryTable(); }
        });
        return textField;
    }

    // Create the history table panel
    private JPanel createHistoryTablePanel() {
        historyTableModel = new DefaultTableModel(new String[]{"Fecha", "Especialidad", "Doctor", "Motivo de Consulta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setFont(MAIN_FONT);
        historyTable.setRowHeight(25);
        historyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        updateHistoryTable();
        addTableClickListener();

        JScrollPane scrollPane = new JScrollPane(historyTable);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // Add double-click listener to open report details
    private void addTableClickListener() {
        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int selectedRow = historyTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        MedicalReport selectedReport = medicalReports.get(selectedRow);
                        new ClinicalHistoryDetailsFrame(selectedReport);
                    }
                }
            }
        });
    }

    // Update the history table based on selected filters
    private void updateHistoryTable() {
        String selectedSpeciality = (String) specialityCombo.getSelectedItem();
        String selectedDoctor = (String) doctorCombo.getSelectedItem();
        String motiveFilter = motiveFilterField.getText().trim().toLowerCase();

        historyTableModel.setRowCount(0);

        for (MedicalReport report : medicalReports) {
            String doctorName = report.getDoctorDNI();
            String speciality = doctorSpecialties.getOrDefault(doctorName, "Unknown");
            String motive = report.getAppointmentMotive().toLowerCase();

            if (!"All".equals(selectedSpeciality) && !speciality.equals(selectedSpeciality)) continue;
            if (!"All".equals(selectedDoctor) && !doctorName.equals(selectedDoctor)) continue;
            if (!motive.contains(motiveFilter)) continue;

            historyTableModel.addRow(new Object[]{report.getVisitDate(), speciality, doctorName, report.getAppointmentMotive()});
        }
    }
}
