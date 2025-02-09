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
    private ImageIconRedrawer iconRedrawer;

    public PatientPortalPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        this.iconRedrawer = new ImageIconRedrawer();
        initPanel();
        addHeader();
        addContent();
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
        gridPanel.add(createCard("Historia Clínica", "/PortalPacienteImgs/healthcare-hospital-medical-43-svgrepo-com.png"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Datos Paciente", "/PortalPacienteImgs/datos_pacientes.png"), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gridPanel.add(createCard("Informes Hospitalización", "/PortalPacienteImgs/informes_hospitalizacion.png"), gbc);

        gbc.gridx = 1;
        gridPanel.add(createCard("Pruebas Diagnósticas", "/PortalPacienteImgs/pruebas_diagnosticas.png"), gbc);

        contentPanel.add(gridPanel, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String iconPath) {
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
}
