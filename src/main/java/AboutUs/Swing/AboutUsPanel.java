package AboutUs.Swing;

import AboutUs.Components.CustomScrollBar;
import LandingPage.Swing.HeaderPanel;
import MainApplication.NavigationController;
import Utils.Utility.ImageIconRedrawer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class AboutUsPanel extends JPanel {
    private final JFrame parentFrame;
    private final NavigationController navigationController;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private final ImageIconRedrawer iconRedrawer;

    private final int IMAGE_WIDTH = 300;
    private final int IMAGE_HEIGHT = 240;

    /**
     * Constructor for AboutUsPanel.
     *
     * @param parentFrame The main application frame.
     * @param navigationController The navigation controller instance.
     */
    public AboutUsPanel(JFrame parentFrame, NavigationController navigationController) {
        this.parentFrame = parentFrame;
        this.navigationController = navigationController;
        this.iconRedrawer = new ImageIconRedrawer();

        initPanel();
        //addHeader();
        addContent();

        // Limit the size of the component to 70% of the parent frame
        Dimension parentSize = parentFrame.getSize();
        int newWidth = (int) (parentSize.width * 0.7);
        setPreferredSize(new Dimension(newWidth, parentSize.height));
    }

    /**
     * Initializes the panel settings.
     */
    private void initPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
    }

    /**
     * Adds the HeaderPanel at the top of the page.
     */
    private void addHeader() {
        HeaderPanel headerPanel = new HeaderPanel(parentFrame, navigationController);
        add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Adds the main content of the "About Us" page.
     */
    private void addContent() {
        contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Quiénes Somos", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        contentPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel subTitleLabel = new JLabel("Lore ipssum Lore ipssum Lore ipssum Lore ipssum Lore ipssum Lore ipssum", JLabel.CENTER);
        subTitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        contentPanel.add(subTitleLabel, gbc);

        gbc.gridy++;
        contentPanel.add(createProfilePanel("Dr. Alejandro Ramírez", "Director Médico",
                "/AboutUs/alejandro_ramirez_director_hospital.jpeg", firstDescription), gbc);

        gbc.gridy++;
        contentPanel.add(createProfilePanel("Lic. Mariana Torres", "Jefa de Enfermería",
                "/AboutUs/mariana_torres_jefa_enfermeria.jpeg", secondDescription), gbc);

        gbc.gridy++;
        contentPanel.add(createProfilePanel("Dr. Ricardo Gómez", "Jefe de Urgencias",
                "/AboutUs/ricardo_gomez_jefe_urgencias.jpeg", thirdDescription), gbc);

        gbc.gridy++;
        contentPanel.add(createProfilePanel("Dra. Laura Fernández", "Jefa de Cirugía",
                "/AboutUs/laura_fernandez_jefa_cirugia.jpeg", fourthDescription), gbc);

        scrollPane = new JScrollPane(contentPanel);
        // Custom ScrollBar
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        // Increase the speed of scrolling
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a profile panel for each staff member.
     *
     * @param doctorName      The doctor's name.
     * @param doctorTitle     The doctor's title.
     * @param doctorImagePath The image path of the doctor.
     * @param description     The description of the doctor.
     * @return A configured JPanel with profile details.
     */
    private JPanel createProfilePanel(String doctorName, String doctorTitle, String doctorImagePath, String description) {
        // Main Panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Image Panel
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));

        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(doctorImagePath)));
        ImageIcon doctorProfilePicture = iconRedrawer.redrawImageIcon(IMAGE_WIDTH, IMAGE_HEIGHT);
        JLabel profileImage = new JLabel(doctorProfilePicture);
        profileImage.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imagePanel.add(profileImage);

        // Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        textPanel.setBorder(new EmptyBorder(0, 20, 0, 0));

        JLabel nameLabel = new JLabel(doctorName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(doctorTitle);
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(10));
        textPanel.add(descriptionArea);

        profilePanel.add(imagePanel, BorderLayout.WEST);
        profilePanel.add(textPanel, BorderLayout.CENTER);

        return profilePanel;
    }

    // TODO: Move descriptions to a separate data class in the future
    private final String firstDescription = "Médico Cirujano, Especialista en Administración Hospitalaria...";

    private final String secondDescription = "Licenciada en Enfermería, Máster en Gestión Sanitaria...";

    private final String thirdDescription = "Médico Especialista en Medicina de Urgencias y Emergencias...";

    private final String fourthDescription = "Médica Cirujana, Especialista en Cirugía General y Laparoscópica...";
}
