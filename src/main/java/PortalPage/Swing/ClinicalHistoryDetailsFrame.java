package PortalPage.Swing;

import AboutUs.Components.CustomScrollBar;
import PortalPage.TempModels.ClinicalHistory;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class ClinicalHistoryDetailsFrame extends JFrame {
    private ClinicalHistory clinicalHistory;

    public ClinicalHistoryDetailsFrame(ClinicalHistory clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
        initializeFrame();
        addComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Detalle de Historia Clínica");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Detalle de Historia Clínica", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Doctor Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(clinicalHistory.getDoctorName()), gbc);

        // Specialty
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(clinicalHistory.getSpecialty()), gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(clinicalHistory.getDate());
        detailsPanel.add(new JLabel(formattedDate), gbc);

        // Details
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Detalles:"), gbc);
        gbc.gridy = 4;
        JTextArea txtDetails = new JTextArea(clinicalHistory.getDetails());
        txtDetails.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDetails.setLineWrap(true);
        txtDetails.setWrapStyleWord(true);
        txtDetails.setEditable(false);
        txtDetails.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
        // Increase preferred size for the text area
        JScrollPane scrollPane = new JScrollPane(txtDetails);
        scrollPane.setPreferredSize(new Dimension(500, 250));
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        detailsPanel.add(scrollPane, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
