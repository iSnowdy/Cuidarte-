package AboutUs.Swing;

import AboutUs.Components.CustomScrollBar;
import DAO.DoctorDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import LandingPage.Components.NotificationPopUp;
import MainApplication.NavigationController;
import Models.Doctor;
import Utils.Utility.CustomLogger;
import Utils.Utility.ImageIconRedrawer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.PANEL_TITLE_FONT;

public class AboutUsPanel extends JPanel {
    private final Logger LOGGER = CustomLogger.getLogger(AboutUsPanel.class);

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
        titleLabel.setFont(PANEL_TITLE_FONT);
        titleLabel.setForeground(MAIN_APP_COLOUR);
        contentPanel.add(titleLabel, gbc);

        String[] imagePaths = {
                "/AboutUs/alejandro_ramirez_director_hospital.jpeg", "/AboutUs/mariana_torres_jefa_enfermeria.jpeg",
                "/AboutUs/ricardo_gomez_jefe_urgencias.jpeg", "/AboutUs/laura_fernandez_jefa_cirugia.jpeg",
                "/AboutUs/doctor_random1.jpg", "/AboutUs/doctor_random2.jpg", "/AboutUs/doctor_random3.jpg"
        };

        List<Doctor> headDoctors = loadHeadDoctorsFromDB();
        if (headDoctors.isEmpty()) {
            gbc.gridy++;
            JLabel noDataLabel = new JLabel("No se han encontrado doctores en este momento.", JLabel.CENTER);
            noDataLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            contentPanel.add(noDataLabel, gbc);
        } else {
            for (int i = 0; i < headDoctors.size(); i++) {
                gbc.gridy++;
                contentPanel.add(createProfilePanel(headDoctors.get(i), imagePaths[i]), gbc);
            }
        }

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

    private List<Doctor> loadHeadDoctorsFromDB() {
        try {
            DoctorDAO doctorDAO = new DoctorDAO();
            return doctorDAO.findAllBosses();
        } catch (DatabaseOpeningException | DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading all head doctors from DB for AboutUsPanel", e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se han podido cargar correctamente los jefes de departamento"
            );
            return List.of();
        }
    }

    // Creates a profile panel for each staff member
    private JPanel createProfilePanel(Doctor doctor, String doctorImagePath) {
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

        JLabel nameLabel = new JLabel(doctor.getFirstName() + " " + doctor.getSurname());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel(doctor.getSpeciality());
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionArea = new JTextArea(doctor.getDescription());
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
}
