package Centres.Swing;

import LandingPage.Swing.HeaderPanel;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class CentresPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private ImageIconRedrawer iconRedrawer;

    public CentresPanel (JFrame parentFrame) {
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

    private void addHeader() {
        HeaderPanel headerPanel = new HeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    }

    private void addContent() {
        this.contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Nuestros centros", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        contentPanel.add(titleLabel, gbc);

        // 3 in Palma, 1 in Formentera, 1 in Ibiza, 1 in Menorca (6 total)
        gbc.gridy++;
        contentPanel.add(createCenterPanel("Mallorca 1", description1, "/Centres/mallorca_1.jpg", address1), gbc);

        gbc.gridy++;
        contentPanel.add(createCenterPanel("Mallorca 2", description2, "/Centres/mallorca_2.jpg", address2), gbc);

        gbc.gridy++;
        contentPanel.add(createCenterPanel("Mallorca 3", description3, "/Centres/mallorca_3.jpg", address3), gbc);

        gbc.gridy++;
        contentPanel.add(createCenterPanel("Formentera", description4, "/Centres/formentera.jpg", address4), gbc);

        gbc.gridy++;
        contentPanel.add(createCenterPanel("Ibiza", description5, "/Centres/ibiza.jpg", address5), gbc);

        gbc.gridy++;
        contentPanel.add(createCenterPanel("Menorca", description6, "/Centres/menorca.jpg", address6), gbc);

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createCenterPanel(String centerName, String description, String imagePath, String address) {
        JPanel centerPanel = new JPanel(new GridBagLayout()) {
            // Draws the surrounding border with round corners
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.LIGHT_GRAY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };

        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setPreferredSize(new Dimension(600, 120));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Image
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(imagePath)));
        ImageIcon centerIcon = iconRedrawer.redrawImageIcon(100, 100);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        JLabel centerImage = new JLabel(centerIcon);
        centerPanel.add(centerImage, gbc);

        // Text panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(centerName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textPanel.add(nameLabel, BorderLayout.NORTH);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        textPanel.add(descriptionLabel, BorderLayout.CENTER);

        JButton navigateButton = new JButton("Cómo llegar");
        navigateButton.setBackground(MAIN_APP_COLOUR);
        navigateButton.setForeground(Color.WHITE);
        navigateButton.setFocusPainted(false);
        navigateButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        // TODO: Future implementation. Redirect to Google Maps somehow :)
        navigateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Navegando a: " + centerName + ". Dirección: " + address);
            }
        });

        textPanel.add(navigateButton, BorderLayout.SOUTH);
        centerPanel.add(textPanel, gbc);

        return centerPanel;
    }

    // TODO: Extract all this information from MySQL
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

    private final String description6 = "Cuenta con el mejor equipo de traumatología), Con las mejores vistas al mar para ofrecer una buena esencia a nuestros pacientes.";
    private final String address6 = "C/ San Antonio Mª Claret, 07760";


}
