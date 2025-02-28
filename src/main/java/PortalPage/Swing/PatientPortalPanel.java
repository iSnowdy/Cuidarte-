package PortalPage.Swing;

import LandingPage.Swing.HeaderPanel;
import MainApplication.NavigationController;
import Models.Patient;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;
import static Utils.Swing.Fonts.PANEL_TITLE_FONT;

public class PatientPortalPanel extends JPanel {
    private final Patient patient;
    private JPanel contentPanel;
    private JPanel gridPanel;
    private final ImageIconRedrawer iconRedrawer;

    public PatientPortalPanel(Patient patient) {
        this.patient = patient;
        this.iconRedrawer = new ImageIconRedrawer();

        initPanel();
        addContent();
    }

    // Initialize main panel properties
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    // Build the main content
    private void addContent() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = buildTitleLabel();
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        addGridPanel();
        contentPanel.add(gridPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    // Build the title label
    private JLabel buildTitleLabel() {
        JLabel titleLabel = new JLabel("Portal del Paciente", JLabel.CENTER);
        titleLabel.setFont(PANEL_TITLE_FONT);
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // Build the grid panel for the cards
    private void addGridPanel() {
        gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Space between cards
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 0;

        gbc.gridx = 0;
        gridPanel.add(createCard("Historia Clínica", "/PortalPacienteImgs/healthcare-hospital-medical-43-svgrepo-com.png", "HC"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Datos Paciente", "/PortalPacienteImgs/datos_pacientes.png", "DP"), gbc);

        gbc.gridx = 2;
        gridPanel.add(createCard("Pruebas Diagnósticas", "/PortalPacienteImgs/pruebas_diagnosticas.png", "PD"), gbc);

        contentPanel.add(gridPanel, BorderLayout.CENTER);
    }

    // Create a card component
    private JPanel createCard(String title, String iconPath, String identifier) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1, true));
        cardPanel.setBackground(Color.WHITE);
        Dimension cardSize = new Dimension(250, 100);
        cardPanel.setPreferredSize(cardSize);
        cardPanel.setMaximumSize(cardSize);
        cardPanel.setMinimumSize(cardSize);

        JPanel iconPanel = buildIconPanel(iconPath);
        JLabel titleLabel = buildCardTitleLabel(title);

        cardPanel.add(iconPanel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.SOUTH);

        addHoverEffect(cardPanel);
        addClickEvent(cardPanel, identifier);

        return cardPanel;
    }

    // Build the icon panel for a card
    private JPanel buildIconPanel(String iconPath) {
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setPreferredSize(new Dimension(250, 80));

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(50, 50);
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(50, 50));
        iconPanel.add(iconLabel);

        return iconPanel;
    }

    // Build the title label for a card
    private JLabel buildCardTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(MAIN_FONT.deriveFont(Font.BOLD));
        titleLabel.setPreferredSize(new Dimension(250, 20));
        return titleLabel;
    }

    // Add hover effect to a card
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

    // Add click event to a card
    private void addClickEvent(JPanel cardPanel, String identifier) {
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                openFrameForIdentifier(identifier);
            }
        });
    }

    // Open frame based on identifier
    private void openFrameForIdentifier(String identifier) {
        switch (identifier) {
            case "DP" -> openPatientDataFrame();
            case "HC" -> openClinicalHistoryFrame();
            case "PD" -> openAnalyticsFrame();
        }
    }

    // Open patient data frame
    private void openPatientDataFrame() {
        new PatientDataFrame(patient);
    }

    // Open clinical history frame
    private void openClinicalHistoryFrame() {
        new ClinicalHistoryFrame(patient);
    }

    // Open diagnostic test frame
    private void openAnalyticsFrame() {
        new DiagnosticTestFrame(patient);
    }
}
