package PortalPage.Swing;

import LandingPage.Swing.HeaderPanel;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class PatientPortalPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel contentPanel;
    private JPanel gridPanel;
    private JPanel detailsPanel;
    private ImageIconRedrawer iconRedrawer;

    public PatientPortalPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        this.iconRedrawer = new ImageIconRedrawer();
        initPanel();
        addHeader();
        addContent();
        addDetailsPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    // Different approach of adding the header panel to it (as compared to Main in LandingPage)
    private void addHeader() {
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    }

    private void addContent() {
        this.contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Portal del Paciente", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34)); // TODO: Stylize
        titleLabel.setForeground(MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0)); // More space maybe?

        contentPanel.add(titleLabel, BorderLayout.NORTH);
        addGridPanel();
        contentPanel.add(gridPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void addGridPanel() {
        this.gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Space in-between cards
        gbc.fill = GridBagConstraints.NONE; // Avoids resizing so I can do it manually

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

    private JPanel createCard(String title, String iconPath, String identifier) {
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1, true)); // Good thickness?
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(250, 100)); // Play with dimensions


        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(iconPath)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(50, 50);
        JLabel iconLabel = new JLabel(icon, JLabel.CENTER);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        cardPanel.add(iconLabel, BorderLayout.CENTER);
        cardPanel.add(titleLabel, BorderLayout.SOUTH);

        addHoverEffect(cardPanel);

        // Updates the details panel using the identifier to know what to show
        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                updateDetailsPanel(identifier);
            }
        });

        return cardPanel;
    }

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

    private void addDetailsPanel() {
        this.detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(detailsPanel, BorderLayout.SOUTH);
    }

    private void updateDetailsPanel(String identifier) {
        detailsPanel.removeAll();

        switch (identifier) {
            case "HC" -> detailsPanel.add(createMedicalReportPanel());
            case "DP" -> detailsPanel.add(createPatientDataPanel());
            case "IH" -> detailsPanel.add(createHospitalizationReportsPanel());
            case "PD" -> detailsPanel.add(createAnalyticsPanel());
        }

        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    // TODO: Think about how to do these panels properly
    // TODO: Stylize it, etc

    private JPanel createMedicalReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Historial Clínico", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        DefaultListModel<String> singleReportTitles = new DefaultListModel<>();
        singleReportTitles.addElement("Consulta 3 - 10/01/2024");
        singleReportTitles.addElement("Consulta 2 - 05/01/2024");
        singleReportTitles.addElement("Consulta 1 - 29/10/2023");

        JList<String> list = new JList<>(singleReportTitles);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPatientDataPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.add(new JLabel("Nombre: Juan Pérez"));
        panel.add(new JLabel("Edad: 45 años"));
        panel.add(new JLabel("DNI: 12345678A"));
        panel.add(new JLabel("Teléfono: 600123456"));
        panel.add(new JLabel("Dirección: Calle Falsa 123"));

        return panel;
    }

    private JPanel createHospitalizationReportsPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Informes de hospitalización aún no disponibles."));
        return panel;
    }

    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Pruebas Diagnósticas", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        DefaultListModel<String> singleReportTitles = new DefaultListModel<>();
        singleReportTitles.addElement("Hemograma - 10/01/2024");
        singleReportTitles.addElement("Bioquímica - 05/01/2024");

        JList<String> list = new JList<>(singleReportTitles);
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        return panel;
    }
}
