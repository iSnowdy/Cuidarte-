package PortalPage.Swing;

import AboutUs.Components.CustomScrollBar;
import PortalPage.TempModels.HospitalizationReport;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class HospitalizationReportDetailsFrame extends JFrame {
    private HospitalizationReport report;

    public HospitalizationReportDetailsFrame(HospitalizationReport report) {
        this.report = report;
        initializeFrame();
        addComponents();
        setVisible(true);
    }

    // Set frame properties
    private void initializeFrame() {
        setTitle("Detalle de Informe de Hospitalización");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Build the UI components to display hospitalization report details
    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel titleLabel = new JLabel("Detalle de Informe de Hospitalización", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(10,0,20,0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Admission Date
        gbc.gridx = 0; gbc.gridy = 0;
        detailsPanel.add(new JLabel("Fecha de Ingreso:"), gbc);
        gbc.gridx = 1;
        String admission = new SimpleDateFormat("dd/MM/yyyy").format(report.getAdmissionDate());
        detailsPanel.add(new JLabel(admission), gbc);

        // Discharge Date
        gbc.gridx = 0; gbc.gridy = 1;
        detailsPanel.add(new JLabel("Fecha de Alta:"), gbc);
        gbc.gridx = 1;
        String discharge = new SimpleDateFormat("dd/MM/yyyy").format(report.getDischargeDate());
        detailsPanel.add(new JLabel(discharge), gbc);

        // Diagnosis
        gbc.gridx = 0; gbc.gridy = 2;
        detailsPanel.add(new JLabel("Diagnóstico:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(report.getDiagnosis()), gbc);

        // Procedures
        gbc.gridx = 0; gbc.gridy = 3;
        detailsPanel.add(new JLabel("Procedimientos:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(report.getProcedures()), gbc);

        // Notes
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Notas:"), gbc);
        gbc.gridy = 5;
        JTextArea txtNotes = new JTextArea(report.getNotes());
        txtNotes.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNotes.setLineWrap(true);
        txtNotes.setWrapStyleWord(true);
        txtNotes.setEditable(false);
        txtNotes.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
        JScrollPane scrollPane = new JScrollPane(txtNotes);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.setPreferredSize(new Dimension(500, 200));
        detailsPanel.add(scrollPane, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
