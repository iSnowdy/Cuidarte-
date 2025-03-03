package UI.LandingPage;

import Components.CustomScrollBar;
import Components.NotificationPopUp;
import Database.DAO.AppointmentDAO;
import Database.DAO.DoctorDAO;
import Database.DAO.MessageDAO;
import Database.Models.Doctor;
import Database.Models.Message;
import Exceptions.DatabaseDeleteException;
import Exceptions.DatabaseInsertException;
import Exceptions.DatabaseQueryException;
import Utils.Utility.CustomLogger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;

public class MessagePanel extends JPanel {
    private final Logger LOGGER = CustomLogger.getLogger(MessagePanel.class);

    private final String patientDNI;
    private JList<String> messageList;
    private DefaultListModel<String> messageModel;
    private JButton newMessageButton;
    private List<Message> messages;

    public MessagePanel(String patientDNI) {
        this.patientDNI = patientDNI;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        initComponents();
        loadMessages();
    }

    // Initializes UI components
    private void initComponents() {
        messageModel = new DefaultListModel<>();
        messageList = new JList<>(messageModel);
        messageList.setCellRenderer(new CustomMessageRenderer());
        messageList.setSelectionBackground(new Color(173, 216, 230));
        messageList.setSelectionForeground(Color.BLACK);
        messageList.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        // Adds double-click listener for deleting messages
        messageList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = messageList.getSelectedIndex();
                    if (index >= 0) {
                        confirmAndDeleteMessage(index);
                    }
                }
            }
        });

        // ScrollPane using CustomScrollBar
        JScrollPane scrollPane = new JScrollPane(messageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBar());

        // "New Message" button
        newMessageButton = new JButton("Nuevo Mensaje");
        newMessageButton.setBackground(MAIN_APP_COLOUR);
        newMessageButton.setForeground(Color.WHITE);
        newMessageButton.setFocusPainted(false);
        newMessageButton.setFont(new Font("Arial", Font.BOLD, 14));
        newMessageButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        newMessageButton.addActionListener(e -> openNewMessageDialog());

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(newMessageButton);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Loads messages for the logged-in patient
    private void loadMessages() {
        messageModel.clear();
        try {
            MessageDAO messageDAO = new MessageDAO();
            messages = messageDAO.findMessagesByPatient(patientDNI);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.ENGLISH);

            for (Message message : messages) {
                String formattedMessage = formatter.format(message.getDate()) + " - " + message.getMessageContent();
                messageModel.addElement(formattedMessage);
            }
        } catch (DatabaseQueryException e) {
            NotificationPopUp.showErrorMessage(this, "Error", "Error loading messages.");
            LOGGER.log(Level.SEVERE, "Error fetching messages for patient: " + patientDNI, e);
        }
    }

    // Opens the dialog for composing a new message
    private void openNewMessageDialog() {
        JDialog messageDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Enviar Mensaje", true);
        messageDialog.setSize(400, 300);
        messageDialog.setLocationRelativeTo(this);
        messageDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.weightx = 1.0;

        JComboBox<String> doctorComboBox = new JComboBox<>();
        List<Doctor> doctors = loadDoctors();

        if (doctors.isEmpty()) {
            doctorComboBox.addItem("No hay doctores disponibles");
            doctorComboBox.setEnabled(false);
        } else {
            for (Doctor doctor : doctors) {
                String fullName = doctor.getSurname() + ", " + doctor.getFirstName();
                doctorComboBox.addItem(fullName);
            }
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Seleccionar Doctor:"), gbc);
        gbc.gridy = 1;
        panel.add(doctorComboBox, gbc);

        JTextArea messageTextArea = new JTextArea(5, 30);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageTextArea);

        gbc.gridy = 2;
        panel.add(new JLabel("Mensaje:"), gbc);
        gbc.gridy = 3;
        panel.add(scrollPane, gbc);

        JButton sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            int selectedIndex = doctorComboBox.getSelectedIndex();
            if (selectedIndex < 0 || doctors.isEmpty()) {
                NotificationPopUp.showErrorMessage(messageDialog, "Error", "Debe seleccionar un doctor.");
                return;
            }

            String messageContent = messageTextArea.getText().trim();
            if (messageContent.isEmpty()) {
                NotificationPopUp.showErrorMessage(messageDialog, "Error", "El mensaje no puede estar vacío.");
                return;
            }

            Doctor selectedDoctor = doctors.get(selectedIndex);
            Message message = new Message(0, patientDNI, selectedDoctor.getDNI(), Timestamp.valueOf(LocalDateTime.now()), messageContent);

            try {
                MessageDAO messageDAO = new MessageDAO();
                messageDAO.save(message);
                NotificationPopUp.showInfoMessage(messageDialog, "Éxito", "Mensaje enviado correctamente.");
                messageDialog.dispose();
                loadMessages();
            } catch (DatabaseInsertException ex) {
                LOGGER.log(Level.SEVERE, "Error sending message", ex);
                NotificationPopUp.showErrorMessage(messageDialog, "Error", "Error al enviar el mensaje.");
            }
        });

        gbc.gridy = 4;
        panel.add(sendButton, gbc);
        messageDialog.add(panel);
        messageDialog.setVisible(true);
    }

    // Confirms and deletes a selected message
    private void confirmAndDeleteMessage(int index) {
        Message selectedMessage = messages.get(index);
        boolean confirm = NotificationPopUp.showConfirmationMessage(this, "Eliminar Mensaje", "¿Estás seguro de que quieres eliminar este mensaje?");

        if (confirm) {
            try {
                MessageDAO messageDAO = new MessageDAO();
                messageDAO.delete(selectedMessage.getId());
                messages.remove(index);
                messageModel.remove(index);
                NotificationPopUp.showInfoMessage(this, "Eliminado", "Mensaje eliminado con éxito.");
            } catch (DatabaseDeleteException e) {
                NotificationPopUp.showErrorMessage(this, "Error", "Error al eliminar el mensaje.");
            }
        }
    }

    // Loads doctors with whom the patient has had previous appointments
    private List<Doctor> loadDoctors() {
        try {
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            DoctorDAO doctorDAO = new DoctorDAO();
            List<String> doctorDNIs = appointmentDAO.findDoctorDNIsByPatient(patientDNI);
            return doctorDAO.findDoctorsByDNIs(doctorDNIs);
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors for patient: " + patientDNI, e);
            return List.of();
        }
    }

    static class CustomMessageRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            label.setBorder(new EmptyBorder(10, 10, 10, 10));
            if (!isSelected) {
                label.setBackground(index % 2 == 0 ? new Color(240, 248, 255) : Color.WHITE);
            }
            return label;
        }
    }
}
