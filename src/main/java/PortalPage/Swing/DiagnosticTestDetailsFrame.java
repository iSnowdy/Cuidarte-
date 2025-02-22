package PortalPage.Swing;

import AboutUs.Components.CustomScrollBar;
import PortalPage.TempModels.DiagnosticTest;
import Utils.Swing.Colors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class DiagnosticTestDetailsFrame extends JFrame {
    private DiagnosticTest diagnosticTest;

    public DiagnosticTestDetailsFrame(DiagnosticTest diagnosticTest) {
        this.diagnosticTest = diagnosticTest;
        initializeFrame();
        addComponents();
        setVisible(true);
    }

    // Set frame properties
    private void initializeFrame() {
        setTitle("Detalle de Prueba Diagnóstica");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Build the UI components to display diagnostic test details
    private void addComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel titleLabel = new JLabel("Detalle de Prueba Diagnóstica", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Colors.MAIN_APP_COLOUR);
        titleLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Test Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Tipo de Prueba:"), gbc);
        gbc.gridx = 1;
        detailsPanel.add(new JLabel(diagnosticTest.getTestType().getValue()), gbc);

        // Date
        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(diagnosticTest.getTestDate());
        detailsPanel.add(new JLabel(formattedDate), gbc);

        // Results
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        detailsPanel.add(new JLabel("Resultados:"), gbc);
        gbc.gridy = 3;
        JTextArea txtResults = new JTextArea(diagnosticTest.getTestResults());
        txtResults.setFont(new Font("Arial", Font.PLAIN, 16));
        txtResults.setLineWrap(true);
        txtResults.setWrapStyleWord(true);
        txtResults.setEditable(false);
        txtResults.setBackground(Colors.TEXTFIELD_BACKGROUND_COLOUR);
        JScrollPane scrollPane = new JScrollPane(txtResults);
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.setPreferredSize(new Dimension(500, 250));
        detailsPanel.add(scrollPane, gbc);

        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }
}
