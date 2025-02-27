package PortalPage.Swing;

import Models.MedicalReport;
import Utils.Swing.Colors;
import Utils.Utility.PDFReportGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ClinicalHistoryDetailsFrame extends JFrame {
    private final MedicalReport medicalReport;
    private JButton generateMedicalReportButton;

    public ClinicalHistoryDetailsFrame(MedicalReport medicalReport) {
        this.medicalReport = medicalReport;
        initializeFrame();
        addComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Detalles de la Historia Clínica");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel superior con título y separador
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Detalles de la Historia Clínica", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Separator with explicit height
        JSeparator separator = new JSeparator();
        separator.setForeground(Colors.MAIN_APP_COLOUR);
        separator.setPreferredSize(new Dimension(1, 2)); // Ensure visibility

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(separator, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Details panel
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Adding details
        addDetailRow(detailsPanel, gbc, "Fecha:", new SimpleDateFormat("dd/MM/yyyy").format(medicalReport.getVisitDate()));
        addDetailRow(detailsPanel, gbc, "Doctor:", medicalReport.getDoctorDNI()); // Set as the doctor's name instead
        addDetailRow(detailsPanel, gbc, "Diagnóstico:", medicalReport.getDiagnosis());
        addDetailRow(detailsPanel, gbc, "Motivo de Consulta:", medicalReport.getAppointmentMotive());
        addDetailRow(detailsPanel, gbc, "Exploración Física:", medicalReport.getPhysicalExploration());
        addDetailRow(detailsPanel, gbc, "Tratamiento:", medicalReport.getTreatment());
        addDetailRow(detailsPanel, gbc, "Temperatura:", medicalReport.getTemperature() + "°C");
        addDetailRow(detailsPanel, gbc, "Presión:", medicalReport.getSystolic() + "/" + medicalReport.getDiastolic() + " mmHg");

        // Scroll panel in case content overflows
        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        generateMedicalReportButton = new JButton("Generar informe en PDF");
        generateMedicalReportButton.addActionListener(e -> {
            PDFReportGenerator.generateMedicalReport(medicalReport, "C:\\Users\\andyl\\IdeaProjects\\Cuidarte\\src\\main\\resources\\medicalreport.pdf");
        });
        mainPanel.add(generateMedicalReportButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Helper method to add rows with proper wrapping
    private void addDetailRow(JPanel panel, GridBagConstraints gbc, String label, String value) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(createDetailLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(createDetailValue(value), gbc);
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    private JTextArea createDetailValue(String value) {
        JTextArea textArea = new JTextArea(value);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setOpaque(false);
        return textArea;
    }
}
