package PortalPage.Swing;

import LandingPage.Swing.HeaderPanel;
import MainApplication.NavigationController;
import PortalPage.TempModels.TestPatient;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class PatientPortalPanel extends JPanel {
    private final JFrame parentFrame;
    private final NavigationController navigationController;
    private final TestPatient patient;
    private JPanel contentPanel;
    private JPanel gridPanel;
    private final ImageIconRedrawer iconRedrawer;

    /**
     * Constructor for PatientPortalPanel.
     *
     * @param parentFrame          The main application frame.
     * @param navigationController The navigation controller instance.
     * @param patient              The test patient instance.
     */
    public PatientPortalPanel(JFrame parentFrame, NavigationController navigationController, TestPatient patient) {
        this.parentFrame = parentFrame;
        this.navigationController = navigationController;
        this.patient = patient;
        this.iconRedrawer = new ImageIconRedrawer();

        initPanel();
        //addHeader();
        addContent();
    }

    /**
     * Initializes the main panel layout and background color.
     */
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    /**
     * Adds the header panel at the top.
     */
    private void addHeader() {
        HeaderPanel headerPanel = new HeaderPanel(parentFrame, navigationController);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Builds and adds the content section.
     */
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

    /**
     * Builds the grid panel containing all the action cards.
     */
    private void addGridPanel() {
        this.gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Space between cards
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gridPanel.add(createCard("Historia Clínica", "/PortalPacienteImgs/healthcare-hospital-medical-43-svgrepo-com.png", "HC"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Datos Paciente", "/PortalPacienteImgs/datos_pacientes.png", "DP"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gridPanel.add(createCard("Informes Hospitalización", "/PortalPacienteImgs/informes_hospitalizacion.png", "IH"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Pruebas Diagnósticas", "/PortalPacienteImgs/pruebas_diagnosticas.png", "PD"), gbc);

        contentPanel.add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * Creates an individual card with fixed dimensions.
     */
    private JPanel createCard(String title, String iconPath, String identifier) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1, true));
        cardPanel.setBackground(Color.WHITE);
        Dimension cardSize = new Dimension(250, 100);
        cardPanel.setPreferredSize(cardSize);
        cardPanel.setMaximumSize(cardSize);
        cardPanel.setMinimumSize(cardSize);

        JPanel iconPanel = buildIconPanel(iconPath);
        JLabel titleLabel = buildTitleLabel(title);

        cardPanel.add(iconPanel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.SOUTH);

        addHoverEffect(cardPanel);
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                openFrameForIdentifier(identifier);
            }
        });

        return cardPanel;
    }

    /**
     * Builds the icon panel for a card.
     */
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

    /**
     * Builds the title label for a card.
     */
    private JLabel buildTitleLabel(String title) {
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setPreferredSize(new Dimension(250, 20));
        return titleLabel;
    }

    /**
     * Adds a hover effect to a card.
     */
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

    /**
     * Opens a new frame based on the card's identifier.
     */
    private void openFrameForIdentifier(String identifier) {
        switch (identifier) {
            case "DP" -> openPatientDataFrame();
            case "HC" -> openClinicalHistoryFrame();
            case "IH" -> openHospitalizationReportsFrame();
            case "PD" -> openAnalyticsFrame();
        }
    }

    /**
     * Opens the frame dedicated to patient data.
     */
    private void openPatientDataFrame() {
        new PatientDataFrame(patient);
    }

    /**
     * Opens the frame dedicated to clinical history.
     */
    private void openClinicalHistoryFrame() {
        new ClinicalHistoryFrame(patient);
    }

    /**
     * Opens the frame dedicated to hospitalization reports.
     */
    private void openHospitalizationReportsFrame() {
        new HospitalizationReportFrame(patient);
    }

    /**
     * Opens the frame dedicated to diagnostic tests.
     */
    private void openAnalyticsFrame() {
        new DiagnosticTestFrame(patient);
    }
}
