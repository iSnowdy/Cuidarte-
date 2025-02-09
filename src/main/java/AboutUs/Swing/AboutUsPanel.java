package AboutUs.Swing;

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

    public AboutUsPanel(JFrame parentFrame) {
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
        // How the element will expand inside the cell. Like this, it will expand horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // If the element does not fully fill the cell, this defines how it will behave. Here to the top left corner
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titleLabel = new JLabel("Quiénes Somos", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(MAIN_APP_COLOUR);
        contentPanel.add(titleLabel, gbc);

        gbc.gridy++;
        JLabel subTitleLabel = new JLabel("Lore ipssum Lore ipssum Lore ipssum Lore ipssum Lore ipssum Lore ipssum");
        subTitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subTitleLabel.setHorizontalAlignment(JLabel.CENTER);
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

        // TODO: Reaaally ugly Scroll. Stylize it somehow?
        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(null); // Removes that ugly af border around the scroll panel
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createProfilePanel(String doctorName, String doctorTitle, String doctorImagePath, String description) {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        //profilePanel.setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Doctor image
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(doctorImagePath)));
        ImageIcon doctorProfilePicture = iconRedrawer.redrawImageIcon(300, 240);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 3;
        JLabel profileImage = new JLabel(doctorProfilePicture);
        profilePanel.add(profileImage, gbc);

        // Element containing the text so it displays to the right of the image
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(Color.WHITE);

        // Name
        JLabel nameLabel = new JLabel(doctorName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        textPanel.add(nameLabel, BorderLayout.NORTH);

        // Title
        JLabel titleLabel = new JLabel(doctorTitle);
        titleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        textPanel.add(titleLabel, BorderLayout.CENTER);

        // Description
        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        textPanel.add(descriptionArea, BorderLayout.SOUTH);

        profilePanel.add(textPanel, gbc);

        return profilePanel;
    }

    // TODO: Extract this information from MySQL

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
