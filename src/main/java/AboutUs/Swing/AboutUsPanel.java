package AboutUs.Swing;

import AboutUs.Components.CustomScrollBar;
import LandingPage.Swing.HeaderPanel;
import Utils.Utility.ImageIconRedrawer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class AboutUsPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel contentPanel;
    private JScrollPane scrollPane;
    private ImageIconRedrawer iconRedrawer;

    private final int IMAGE_WIDTH = 300;
    private final int IMAGE_HEIGHT = 240;

    public AboutUsPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.iconRedrawer = new ImageIconRedrawer();
        initPanel();
        addHeader();
        addContent();
        // Limit the size of the component to 70% of the parent frame
        Dimension parentSize = parentFrame.getSize();
        int newWidth = (int) (parentSize.width * 0.7);
        setPreferredSize(new Dimension(newWidth, parentSize.height));
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

    private JPanel createProfilePanel(String doctorName, String doctorTitle, String doctorImagePath, String description) {
        // Main Panel
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Image Panel with a fixed size to ensure the correct width of the image, so it does
        // not move the text besides it
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

        // A border to the left to separate the image from text
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

    // TODO: From DB soonTM
    private final String firstDescription = "Médico Cirujano, Especialista en Administración Hospitalaria\n" +
            "El Dr. Ramírez es un líder visionario con más de 20 años de experiencia en gestión hospitalaria. " +
            "Su formación en cirugía y administración le permite dirigir el hospital con un enfoque centrado en " +
            "la calidad del servicio y la eficiencia operativa. Es conocido por su compromiso con la innovación " +
            "en salud y su capacidad para motivar a su equipo.";

    private final String secondDescription = "Licenciada en Enfermería, Máster en Gestión Sanitaria\n" +
            "Con más de 15 años en el ámbito hospitalario, la Lic. Torres lidera el equipo de enfermería con " +
            "dedicación y empatía. Su experiencia en gestión sanitaria le ha permitido implementar mejoras en la " +
            "atención al paciente y en la capacitación del personal. Es reconocida por su trato humano y su " +
            "habilidad para resolver problemas en momentos críticos.";

    private final String thirdDescription = "Médico Especialista en Medicina de Urgencias y Emergencias\n" +
            "El Dr. Gómez es un profesional altamente capacitado en la atención de emergencias críticas. Con " +
            "18 años de experiencia en urgencias hospitalarias, destaca por su capacidad de tomar decisiones " +
            "rápidas y efectivas bajo presión. Su liderazgo es fundamental para coordinar al equipo en " +
            "situaciones de alta demanda y garantizar una atención oportuna.";

    private final String fourthDescription = "Médica Cirujana, Especialista en Cirugía General y Laparoscópica\n" +
            "La Dra. Fernández cuenta con una trayectoria de 20 años en cirugía y es reconocida por su precisión " +
            "y compromiso con la seguridad del paciente. Ha liderado múltiples proyectos de innovación quirúrgica " +
            "y formación para nuevos cirujanos. Su liderazgo se basa en la excelencia técnica y en el trabajo en " +
            "equipo.";
}
