package PortalPage.Swing;

import LandingPage.Swing.HeaderPanel;
import PortalPage.TempModels.TestPatient;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class PatientPortalPanel extends JPanel {
    private JFrame parentFrame;
    private TestPatient patient; // Test patient passed from main
    private JPanel contentPanel;
    private JPanel gridPanel;
    private ImageIconRedrawer iconRedrawer;

    // Constructor accepting a TestPatient instance
    public PatientPortalPanel(JFrame parentFrame, TestPatient patient) {
        this.parentFrame = parentFrame;
        this.patient = patient;
        this.iconRedrawer = new ImageIconRedrawer();
        initPanel();
        addHeader();
        addContent();
    }

    // Initialize the main panel with BorderLayout and white background
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    // Add header component at the top of the panel
    private void addHeader() {
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    }

    // Build the content panel (title and grid of cards) and add it to the main panel
    private void addContent() {
        this.contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Portal del Paciente", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        addGridPanel();
        contentPanel.add(gridPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    // Build the grid panel containing all the action cards
    private void addGridPanel() {
        this.gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Space between cards
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 0;
        gridPanel.add(createCard("Historia Clínica", "/PortalPacienteImgs/healthcare-hospital-medical-43-svgrepo-com.png", "HC"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Datos Paciente", "/PortalPacienteImgs/datos_pacientes.png", "DP"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gridPanel.add(createCard("Informes Hospitalización", "/PortalPacienteImgs/informes_hospitalizacion.png", "IH"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Pruebas Diagnósticas", "/PortalPacienteImgs/pruebas_diagnosticas.png", "PD"), gbc);

        contentPanel.add(gridPanel, BorderLayout.CENTER);
    }

    // Create an individual card with fixed dimensions
    private JPanel createCard(String title, String iconPath, String identifier) {
        // Create card panel with fixed size (250x100)
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1, true));
        cardPanel.setBackground(Color.WHITE);
        Dimension cardSize = new Dimension(250, 100);
        cardPanel.setPreferredSize(cardSize);
        cardPanel.setMaximumSize(cardSize);
        cardPanel.setMinimumSize(cardSize);

        // Create an icon panel with fixed size to ensure uniformity
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(250, 80));

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(50, 50);
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(50, 50));
        iconPanel.add(iconLabel);

        // Create a text label with fixed height
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setPreferredSize(new Dimension(250, 20));

        cardPanel.add(iconPanel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.SOUTH);

        // Add hover effect to the card
        addHoverEffect(cardPanel);

        // On click, open a new frame dedicated to this action
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                openFrameForIdentifier(identifier);
            }
        });

        return cardPanel;
    }

    // Add hover effect: change background color on mouse enter/exit
    private void addHoverEffect(JPanel cardPanel) {
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cardPanel.setBackground(Color.LIGHT_GRAY);
                cardPanel.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cardPanel.setBackground(Color.WHITE);
                cardPanel.repaint();
            }
        });
    }

    // Open a new frame based on the card's identifier using a switch that calls dedicated methods
    private void openFrameForIdentifier(String identifier) {
        switch (identifier) {
            case "DP" -> openPatientDataFrame();
            case "HC" -> openClinicalHistoryFrame();
            case "IH" -> openHospitalizationReportsFrame();
            case "PD" -> openAnalyticsFrame();
        }
    }

    // Open the frame dedicated to patient data
    private void openPatientDataFrame() {
        new PatientDataFrame(patient);
    }

    // Open the frame dedicated to clinical history
    private void openClinicalHistoryFrame() {
        new ClinicalHistoryFrame(patient);
    }

    private void openHospitalizationReportsFrame() {
        new HospitalizationReportFrame(patient);
    }

    private void openAnalyticsFrame() {
        new DiagnosticTestFrame(patient);
    }
}
