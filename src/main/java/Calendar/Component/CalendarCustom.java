package Calendar.Component;

import Calendar.Main.TemporaryAppointment;
import Calendar.Swing.PanelSlide;
import LandingPage.Components.NotificationPopUp;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;

public class CalendarCustom extends JPanel {
    private final ImageIconRedrawer iconRedrawer;
    private int month;
    private int year;

    private JLabel monthYearLabel;
    private PanelSlide slide;
    private JButton backButton;
    private JButton nextButton;
    private JButton changeDoctorButton;

    private JComboBox<String> doctorComboBox;
    private Map<String, List<Integer>> doctorScheduleMap;
    private Map<String, List<String>> doctorTimeSlots; // Stores doctor-specific time slots
    private Set<LocalDate> appointmentDays = new HashSet<>();

    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel selectionPanel;
    private JPanel appointmentHistoryPanel;
    private PanelDate currentPanelDate;

    private String selectedDoctor;

    // TODO: For testing purposes only!
    private final List<TemporaryAppointment> confirmedAppointments = new ArrayList<>();
    private DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JList<String> historyList;

    private boolean isSyncingAppointments = false;


    public CalendarCustom() {
        this.iconRedrawer = new ImageIconRedrawer();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        thisMonth(); // Initialize current month and year

        initDoctorSelection(); // Dropdown for selecting a doctor
        initLeftPanel(); // Left panel for specific time scheduling
        initRightPanel(); // Right panel containing the calendar

        setupMainLayout();
    }

    // Updates the displayed calendar based on the selected doctor's available days
    private void updateDoctorSchedule(boolean toRight) {
        syncAppointmentsWithHistory();

        List<Integer> availableDays = selectedDoctor != null ? doctorScheduleMap.get(selectedDoctor) : List.of();
        showMonthYear();

        currentPanelDate = new PanelDate(month, year, availableDays, this::updateTimeSlots, appointmentDays, this::showAppointmentDetails);

        if (toRight) slide.show(currentPanelDate, PanelSlide.AnimationType.TO_LEFT);
        else slide.show(currentPanelDate, PanelSlide.AnimationType.TO_RIGHT);
    }

    private void syncAppointmentsWithHistory() {
        if (isSyncingAppointments) return;
        isSyncingAppointments = true;

        appointmentDays.clear();

        for (int i = 0; i < historyModel.getSize(); i++) {
            String appointment = historyModel.get(i);
            LocalDate date = extractDateFromAppointment(appointment);
            if (date != null) {
                appointmentDays.add(date);
            }
        }

        // Force calendar update
        updateDoctorSchedule(true);

        isSyncingAppointments = false;
    }

    // TODO: Temporary
    // Extracts the date from an appointment
    private LocalDate extractDateFromAppointment(String appointmentText) {
        return confirmedAppointments.stream()
                .filter(a -> a.toString().equals(appointmentText))
                .map(TemporaryAppointment::getDate)
                .findFirst()
                .orElse(null);
    }

    // Updates the left panel to display available time slots for a selected day
    private void updateTimeSlots(LocalDate selectedDate) {
        leftPanel.removeAll();

        JLabel dateLabel = new JLabel("Available slots for: " + selectedDate, SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dateLabel.setForeground(Color.WHITE);
        leftPanel.add(dateLabel, BorderLayout.NORTH);

        JPanel timeSlotPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        List<String> availableTimeSlots = doctorTimeSlots.get(selectedDoctor);

        if (availableTimeSlots != null) {
            for (String timeSlot : availableTimeSlots) {
                JButton slotButton = new JButton(timeSlot);
                slotButton.addActionListener(e -> confirmAppointment(selectedDate, timeSlot));
                timeSlotPanel.add(slotButton);
            }
        } else {
            JLabel noSlotsLabel = new JLabel("No slots available", SwingConstants.CENTER);
            noSlotsLabel.setForeground(Color.WHITE);
            timeSlotPanel.add(noSlotsLabel);
        }

        leftPanel.add(timeSlotPanel, BorderLayout.CENTER);
        leftPanel.add(changeDoctorButton, BorderLayout.NORTH); // Keeps the button to change doctor here too

        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void confirmAppointment(LocalDate date, String time) {
        showConfirmationDialog(date, time);
    }

    // Confirmation popup
    private void showConfirmationDialog(LocalDate date, String time) {
        String message = "Doctor: " + selectedDoctor +
                "\nFecha: " + date +
                "\nHora: " + time +
                "\n\n¿Desea confirmar esta cita?";

        boolean confirmedAppointment = NotificationPopUp.showConfirmationMessage(this, "Confirmar cita", message);

        if (confirmedAppointment) {
            registerAppointment(date, time);
        }
    }

    // Helps updating the historic of appointments
    private void registerAppointment(LocalDate date, String time) {
        TemporaryAppointment appointment = new TemporaryAppointment(selectedDoctor, date, time);
        confirmedAppointments.add(appointment);
        historyModel.addElement(appointment.toString()); // Adds this specific appointment to the historic
        appointmentDays.add(date); // Adds it to the list of appointments injected to PanelDate

        NotificationPopUp.showInfoMessage(this, "Cita confirmada con éxito.", "Éxito");

        if (appointmentHistoryPanel.getParent() == null) {
            leftPanel.add(appointmentHistoryPanel, BorderLayout.SOUTH);
            leftPanel.revalidate();
            leftPanel.repaint();
        }
        updateDoctorSchedule(true);
    }

    public void showAppointmentDetails(LocalDate date) {
        TemporaryAppointment appointment = confirmedAppointments.stream()
                .filter(a -> a.getDate().equals(date))
                .findFirst()
                .orElse(null);

        if (appointment == null) {
            NotificationPopUp.showInfoMessage(this, "No hay detalles disponibles.", "Información");
            return;
        }

        boolean isFuture = date.isAfter(LocalDate.now());

        Object[] options;
        if (isFuture) options = new Object[]{"Cancelar cita", "Cerrar"};
        else options = new Object[]{"Cerrar"};

        int selection = JOptionPane.showOptionDialog(
                this,
                "Detalles de la Cita:\n" + appointment.toString(),
                "Información de Cita",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[options.length - 1]
        );

        if (isFuture && selection == 0) {
            cancelAppointment(date);

            // Force UI update to remove the appointment marker from the calendar
            historyList.updateUI();
        }
    }

    private void cancelAppointment(LocalDate date) {
        // Remove from confirmed appointments
        confirmedAppointments.removeIf(a -> a.getDate().equals(date));
        appointmentDays.remove(date);

        // Remove from history model
        for (int i = 0; i < historyModel.getSize(); i++) {
            LocalDate appointmentDate = extractDateFromAppointment(historyModel.getElementAt(i));
            if (appointmentDate != null && appointmentDate.equals(date)) {
                historyModel.remove(i);
                break;
            }
        }

        NotificationPopUp.showInfoMessage(this, "Cita cancelada", "Cita cancelada con éxito");

        // Force UI update
        historyList.updateUI();
        syncAppointmentsWithHistory();
        updateDoctorSchedule(true);
    }


    // Returns to doctor selection without clearing appointments
    private void returnToDoctorSelection() {
        leftPanel.removeAll();
        leftPanel.add(selectionPanel, BorderLayout.NORTH);

        // We make sure to add again the appointment historic panel
        leftPanel.add(appointmentHistoryPanel, BorderLayout.SOUTH);

        leftPanel.revalidate();
        leftPanel.repaint();

        showEmptyCalendar();
        changeDoctorButton.setVisible(selectedDoctor != null); // Hide the button again
    }

    private void thisMonth() {
        month = LocalDate.now().getMonthValue();
        year = LocalDate.now().getYear();
    }

    // Initializes the dropdown for doctor selection and loads mock data
    private void initDoctorSelection() {
        // TODO: Test data. Should be loaded from DB
        doctorScheduleMap = new HashMap<>();
        doctorScheduleMap.put("Dr. Smith", List.of(2, 4, 6, 8, 10, 12, 14)); // Even days
        doctorScheduleMap.put("Dr. Johnson", List.of(1, 3, 5, 7, 9, 11, 13)); // Odd days
        doctorScheduleMap.put("Dr. Adams", List.of(5, 10, 15, 20, 25)); // Random

        // Define specific time slots for each doctor
        doctorTimeSlots = new HashMap<>();
        doctorTimeSlots.put("Dr. Smith", List.of("09:00 AM", "10:30 AM", "12:00 PM"));
        doctorTimeSlots.put("Dr. Johnson", List.of("02:00 PM", "03:30 PM", "05:00 PM"));
        doctorTimeSlots.put("Dr. Adams", List.of("08:00 AM", "09:45 AM", "11:30 AM", "01:00 PM"));

        doctorComboBox = new JComboBox<>(doctorScheduleMap.keySet().toArray(new String[0]));
        doctorComboBox.setPreferredSize(new Dimension(200, 30));
        doctorComboBox.addActionListener(e -> handleDoctorSelection());

        selectedDoctor = null; // Ensure no doctor is preselected
    }

    // Handles doctor selection, ensuring time slots and calendar update dynamically
    private void handleDoctorSelection() {
        selectedDoctor = (String) doctorComboBox.getSelectedItem();
        if (selectedDoctor != null) {
            updateDoctorSchedule(true);
            changeDoctorButton.setVisible(true); // Now show the button to go back
        }
    }

    // Configures the left panel where time slots will be displayed
    private void initLeftPanel() {
        leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(SECONDARY_APP_COLOUR);

        selectionPanel = new JPanel(new BorderLayout());
        selectionPanel.setBackground(SECONDARY_APP_COLOUR);

        JLabel titleLabel = new JLabel("Selecciona un doctor:", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        // Button to allow switching doctor
        changeDoctorButton = new JButton("← Cambiar Doctor");
        changeDoctorButton.setVisible(false); // Initially hidden
        changeDoctorButton.addActionListener(e -> returnToDoctorSelection());

        selectionPanel.add(titleLabel, BorderLayout.NORTH);
        selectionPanel.add(doctorComboBox, BorderLayout.CENTER);
        selectionPanel.add(changeDoctorButton, BorderLayout.SOUTH);

        leftPanel.add(selectionPanel, BorderLayout.NORTH);

        initHistoryPanel();
    }

    private void initHistoryPanel() {
        appointmentHistoryPanel = new JPanel(new BorderLayout());
        appointmentHistoryPanel.setBackground(SECONDARY_APP_COLOUR);

        JLabel historyLabel = new JLabel("Historial de Citas", SwingConstants.CENTER);
        historyLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        historyLabel.setForeground(Color.WHITE);

        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historyList.setBackground(Color.WHITE);
        historyList.setFont(new Font("SansSerif", Font.PLAIN, 14));

        historyList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) { // Double click
                    int index = historyList.getSelectedIndex();
                    if (index >= 0) {
                        String selectedAppointment = historyModel.getElementAt(index);
                        confirmCancelAppointment(index, selectedAppointment);
                    }
                }
            }
        });

        // Adds a scroll to the list
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        appointmentHistoryPanel.add(historyLabel, BorderLayout.NORTH);
        appointmentHistoryPanel.add(scrollPane, BorderLayout.CENTER);

        leftPanel.add(appointmentHistoryPanel, BorderLayout.SOUTH);

        // Testing elements
        historyModel.addElement("Cita con Dr. Smith el 2024-03-01 a las 10:30 AM");
        historyModel.addElement("Cita con Dr. Johnson el 2024-03-02 a las 02:00 PM");
        historyModel.addElement("Cita con Dr. Johnson el 2024-27-02 a las 02:00 PM");
    }

    private void confirmCancelAppointment(int index, String selectedAppointment) {
        boolean confirmed =
                NotificationPopUp.showConfirmationMessage(this, "Cancelar cita",
                        "¿Estás seguro de que deseas cancelar esta cita?\n\n" + selectedAppointment);

        if (confirmed) {
            historyModel.remove(index);
            LocalDate date = extractDateFromAppointment(selectedAppointment);
            appointmentDays.remove(date);

            NotificationPopUp.showInfoMessage(this, "Cita cancelada con éxito", "Cita cancelada");

            updateDoctorSchedule(true);

            if (selectedDoctor == null) {
                returnToDoctorSelection();
            }
        }
    }

    // Configures the right panel, which contains the calendar and navigation buttons
    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());

        JPanel monthPanel = new JPanel(new BorderLayout());

        backButton = createNavigationButton("/LandingPage/previous.png", e -> goToPreviousMonth());
        nextButton = createNavigationButton("/LandingPage/next.png", e -> goToNextMonth());

        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        monthYearLabel.setForeground(SECONDARY_APP_COLOUR);

        monthPanel.add(backButton, BorderLayout.WEST);
        monthPanel.add(monthYearLabel, BorderLayout.CENTER);
        monthPanel.add(nextButton, BorderLayout.EAST);
        rightPanel.add(monthPanel, BorderLayout.NORTH);

        slide = new PanelSlide();
        slide.setBackground(Color.WHITE);
        rightPanel.add(slide, BorderLayout.CENTER);

        // Display an empty calendar until a doctor is selected
        showEmptyCalendar();
    }

    // Shows an empty calendar initially
    private void showEmptyCalendar() {
        currentPanelDate = new PanelDate(month, year, List.of(), this::updateTimeSlots, appointmentDays, this::showAppointmentDetails);
        slide.show(currentPanelDate, PanelSlide.AnimationType.TO_LEFT);
        showMonthYear();
    }

    private void setupMainLayout() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(false);
        add(splitPane, BorderLayout.CENTER);
    }

    private JButton createNavigationButton(String imagePath, ActionListener actionListener) {
        iconRedrawer.setImageIcon(new ImageIcon(getClass().getResource(imagePath)));
        ImageIcon icon = iconRedrawer.redrawImageIcon(20, 20);
        JButton button = new JButton(icon);
        styleNavigationButton(button);
        button.addActionListener(actionListener);
        return button;
    }

    private void styleNavigationButton(JButton button) {
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    private void showMonthYear() {
        LocalDate currentMonth = LocalDate.of(year, month, 1);
        String monthName = currentMonth.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new java.util.Locale("es", "ES"));
        monthName = monthName.substring(0, 1).toUpperCase() + monthName.substring(1);
        monthYearLabel.setText(monthName + " " + year);
    }

    private void goToPreviousMonth() {
        if (month == 1) {
            month = 12;
            year--;
        } else {
            month--;
        }
        updateDoctorSchedule(false);
    }

    private void goToNextMonth() {
        if (month == 12) {
            month = 1;
            year++;
        } else {
            month++;
        }
        updateDoctorSchedule(true);
    }
}
