package UI.Centres;

import Components.CustomScrollBar;
import Components.CustomizedButton;
import Database.DAO.ClinicDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Components.NotificationPopUp;
import MainApplication.NavigationController;
import Database.Models.Clinic;
import Utils.Utility.CustomLogger;
import Utils.Utility.GoogleMapsRedirect;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.PANEL_TITLE_FONT;

public class CentresPanel extends JPanel {
    private final Logger LOGGER = CustomLogger.getLogger(CentresPanel.class);

    private final JFrame parentFrame;
    private final NavigationController navigationController;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private final ImageIconRedrawer iconRedrawer;

    public CentresPanel(JFrame parentFrame, NavigationController navigationController) {
        this.parentFrame = parentFrame;
        this.navigationController = navigationController;
        this.iconRedrawer = new ImageIconRedrawer();

        initPanel();
        addContentComponent();
    }

    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    private void addContentComponent() {
        contentPanel = buildContentPanel();
        scrollPane = buildScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Content panel with titles and centre entries
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
        JLabel titleLabel = new JLabel("Nuestros Centros", JLabel.CENTER);
        titleLabel.setFont(PANEL_TITLE_FONT);
        titleLabel.setForeground(MAIN_APP_COLOUR);
        panel.add(titleLabel, gbc);

        String[] clinicImagePaths = {
                "/Centres/mallorca_1.jpg", "/Centres/mallorca_2.jpg",
                "/Centres/mallorca_3.jpg", "/Centres/formentera.jpg",
                "/Centres/ibiza.jpg", "/Centres/menorca.jpg"
        };

        List<Clinic> clinics = loadClinicsFromDB();
        // Checks if it's empty. If it is, then just add a random label as a placeholder
        // If not, then iterate the list adding each clinic as a panel
        if (clinics.isEmpty()) {
            gbc.gridy++;
            JLabel noDataLabel = new JLabel("No hay clínicas disponibles en este momento.", JLabel.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            panel.add(noDataLabel, gbc);
        } else {
            for (int i = 0; i < clinics.size(); i++) {
                gbc.gridy++;
                // Add each centre entry panel
                panel.add(createCenterPanel(clinics.get(i), clinicImagePaths[i]), gbc);

            }
        }
        return panel;
    }

    // Retrieves all clinics from the DB using the DAO
    private List<Clinic> loadClinicsFromDB() {
        try {
            ClinicDAO clinicDAO = new ClinicDAO();
            return clinicDAO.findAll();
        } catch (DatabaseOpeningException | DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading all clinics from DB for CentresPanel", e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se han podido cargar correctamente las clínicas."
            );
            return List.of();
        }
    }

    // Centre panel that contains an image and text information
    private JPanel createCenterPanel(Clinic clinic, String imagePath) {
        JPanel roundPanel = createRoundedPanel();
        JPanel imagePanel = buildImagePanel(imagePath);
        JPanel textPanel = buildTextPanel(clinic);

        // Place the image on the left and text on the right
        roundPanel.add(imagePanel, BorderLayout.WEST);
        roundPanel.add(textPanel, BorderLayout.CENTER);
        return roundPanel;
    }

    // Creates the rounded panel for each centre
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
        panel.setPreferredSize(new Dimension(600, 150));
        return panel;
    }

    // Builds the image panel for a centre
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

    // Builds the entire Text Panel by using the information contained inside the Clinic
    // Model retrieved from the DB
    private JPanel buildTextPanel(Clinic clinic) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 20, 0, 0)); // Left margin separation

        JLabel nameLabel = new JLabel(clinic.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel addressLabel = new JLabel(clinic.getAddress());
        addressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        addressLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel phoneLabel = new JLabel(clinic.getPhoneNumber());
        phoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel(clinic.getEmail());
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        CustomizedButton navigateButton = buildNavigateButton(clinic.getAddress());
        navigateButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(addressLabel);
        panel.add(phoneLabel);
        panel.add(emailLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(navigateButton);

        return panel;
    }

    // Creates a navigation button that opens Google Maps
    private CustomizedButton buildNavigateButton(String address) {
        CustomizedButton button = new CustomizedButton();
        button.setText("Cómo llegar");
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setBackground(MAIN_APP_COLOUR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setMargin(new Insets(3, 10, 3, 10));

        // Action listener to open Google Maps
        button.addActionListener(e -> GoogleMapsRedirect.openGoogleMaps(address, this));
        return button;
    }

    //Builds a scroll pane with a custom scrollbar
    private JScrollPane buildScrollPane(JPanel panel) {
        JScrollPane sp = new JScrollPane(panel);
        sp.getVerticalScrollBar().setUI(new CustomScrollBar());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        sp.getVerticalScrollBar().setUnitIncrement(30); // Increase mouse wheel scroll speed
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setBorder(null);
        return sp;
    }
}
