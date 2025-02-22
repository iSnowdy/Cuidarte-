package Centres.Swing;

import AboutUs.Components.CustomScrollBar;
import Authentication.Components.CustomizedButton;
import LandingPage.Swing.HeaderPanel;
import Utils.Utility.GoogleMapsRedirect;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class CentresPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private ImageIconRedrawer iconRedrawer;

    public CentresPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.iconRedrawer = new ImageIconRedrawer();
        initPanel();
        addHeaderComponent();
        addContentComponent();
    }

    // Initialize the main panel's layout and background color
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    // Add the header component at the top
    private void addHeaderComponent() {
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    }

    // Build the content component and add it with a custom scroll pane
    private void addContentComponent() {
        contentPanel = buildContentPanel();
        scrollPane = buildScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Create the content panel with title and centre entries
    private JPanel buildContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 40, 40, 40)); // Uniform padding

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Add the main title
        JLabel titleLabel = new JLabel("Nuestros centros", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        panel.add(titleLabel, gbc);

        // Add each centre entry panel
        gbc.gridy++;
        panel.add(createCenterPanel("Mallorca 1", description1, "/Centres/mallorca_1.jpg", address1), gbc);
        gbc.gridy++;
        panel.add(createCenterPanel("Mallorca 2", description2, "/Centres/mallorca_2.jpg", address2), gbc);
        gbc.gridy++;
        panel.add(createCenterPanel("Mallorca 3", description3, "/Centres/mallorca_3.jpg", address3), gbc);
        gbc.gridy++;
        panel.add(createCenterPanel("Formentera", description4, "/Centres/formentera.jpg", address4), gbc);
        gbc.gridy++;
        panel.add(createCenterPanel("Ibiza", description5, "/Centres/ibiza.jpg", address5), gbc);
        gbc.gridy++;
        panel.add(createCenterPanel("Menorca", description6, "/Centres/menorca.jpg", address6), gbc);

        return panel;
    }

    // Build a scroll pane with a custom scrollbar for the provided panel
    private JScrollPane buildScrollPane(JPanel panel) {
        JScrollPane sp = new JScrollPane(panel);
        sp.getVerticalScrollBar().setUI(new CustomScrollBar());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        sp.getVerticalScrollBar().setUnitIncrement(30); // Increase mouse wheel scroll speed
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setBorder(null);
        return sp;
    }

    // Create a centre panel that contains an image and text information
    private JPanel createCenterPanel(String centerName, String description, String imagePath, String address) {
        JPanel roundPanel = createRoundedPanel();
        JPanel imagePanel = buildImagePanel(imagePath);
        JPanel textPanel = buildTextPanel(centerName, description, address);

        // Place the image on the left and text on the right
        roundPanel.add(imagePanel, BorderLayout.WEST);
        roundPanel.add(textPanel, BorderLayout.CENTER);
        return roundPanel;
    }

    // Create a panel with a custom rounded border background
    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Enable antialiasing for smooth rounded edges
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(600, 120));
        return panel;
    }

    // Build the image panel with a fixed size and a redrawn image
    private JPanel buildImagePanel(String imagePath) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(120, 120));

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(imagePath)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(100, 100);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(imageLabel);
        return panel;
    }

    // Build the text panel containing centre name, description, and the navigate button
    private JPanel buildTextPanel(String centerName, String description, String address) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Left margin separation

        JLabel nameLabel = new JLabel(centerName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        CustomizedButton navigateButton = buildNavigateButton(address);
        navigateButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(descriptionArea);
        panel.add(Box.createVerticalStrut(10));
        panel.add(navigateButton);

        return panel;
    }

    // Build the "Cómo llegar" button with custom styling and its action listener
    private CustomizedButton buildNavigateButton(String address) {
        CustomizedButton button = new CustomizedButton();
        button.setText("Cómo llegar");
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(MAIN_APP_COLOUR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(3, 10, 3, 10)); // Compact button margins

        // Action listener to open Google Maps with the given address
        ActionListener action = e -> GoogleMapsRedirect.openGoogleMaps(address, this);
        button.addActionListener(action);
        return button;
    }

    // Example data (to be extracted from MySQL in the future)
    private final String description1 = "Situado en el centro de Palma, ¡especializado en urgencias con un amplio aparcamiento que te permitirá no tener preocupación para aparcar!";
    private final String address1 = "Plaza España 24, 07002";
    private final String description2 = "Ubicado en el corazón de la isla con una amplia selección de especialidades a su disposición.";
    private final String address2 = "C/ Castillo de Bellver 69, 07300";
    private final String description3 = "Tenemos el mejor equipo en Traumatología.";
    private final String address3 = "C/ Simón Tort 19, 07500";
    private final String description4 = "Cuenta con el mejor equipo de Radioterapia";
    private final String address4 = "C/ Pla del rey 12, 07860";
    private final String description5 = "Para cuando salgas del after, especialistas en toxicología.";
    private final String address5 = "C/ Avenidas Gaspar de Bennazar 9, 07800";
    private final String description6 = "Cuenta con el mejor equipo de traumatología, con las mejores vistas al mar para ofrecer una buena esencia a nuestros pacientes.";
    private final String address6 = "C/ San Antonio Mª Claret, 07760";
}
