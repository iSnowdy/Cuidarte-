package Calendar.Component;

import Calendar.Swing.PanelSlide;
import DAO.AppointmentDAO;
import DAO.ClinicDAO;
import DAO.DoctorDAO;
import DAO.PatientDAO;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import LandingPage.Components.NotificationPopUp;
import Models.Appointment;
import Models.Doctor;
import Models.Patient;
import Utils.Utility.CustomLogger;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;

public class CalendarCustom extends JPanel {
    private final Logger LOGGER = CustomLogger.getLogger(CalendarCustom.class);

    private Patient patient;

    private DoctorDAO doctorDAO;
    private ClinicDAO clinicDAO;
    private AppointmentDAO appointmentDAO;
    private PatientDAO patientDAO;

    private final ImageIconRedrawer iconRedrawer;
    private int month;
    private int year;

    private JLabel monthYearLabel;
    private PanelSlide slide;
    private JButton backButton;
    private JButton nextButton;
    private JButton changeDoctorButton;

    private String selectedClinic;
    private String selectedSpeciality;
    private int clinicID;

    private JComboBox<String> doctorComboBox;
    private Map<String, List<Integer>> doctorScheduleMap;
    private Map<String, List<String>> doctorTimeSlots; // Stores doctor-specific time slots
    private Set<LocalDate> appointmentDays = new HashSet<>();

    private JPanel rightPanel;
    private JPanel leftPanel;
    private JPanel selectionPanel;
    private JPanel appointmentHistoryPanel;
    private PanelDate currentPanelDate;

    private Doctor selectedDoctorEntity;
    private String selectedDoctor;

    private final List<Appointment> confirmedAppointments = new ArrayList<>();
    private DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JList<String> historyList;

    private boolean isSyncingAppointments = false;


    public CalendarCustom(Patient patient, String selectedClinic, String selectedSpeciality) {
        this.iconRedrawer = new ImageIconRedrawer();
        this.patient = patient;
        this.selectedClinic = selectedClinic;
        this.selectedSpeciality = selectedSpeciality;

        initDAOs();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        thisMonth(); // Initialize current month and year

        loadDoctorDataFromDB();

        //initDoctorSelection(); // Dropdown for selecting a doctor
        initLeftPanel(); // Left panel for specific time scheduling
        initRightPanel(); // Right panel containing the calendar

        setupMainLayout();
    }

    private void initDAOs() {
        try {
            this.doctorDAO = new DoctorDAO();
            this.clinicDAO = new ClinicDAO();
            this.appointmentDAO = new AppointmentDAO();
            this.patientDAO = new PatientDAO();
        } catch (DatabaseOpeningException e) {
            LOGGER.log(Level.SEVERE, "Error opening DAOs for Calendar", e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se ha podido inicializar el calendario correctamente"
            );
            // TODO: How to dispose of the frame?
        }
    }

    // DB stuff

    private void loadDoctorDataFromDB() {
        try {
            Optional<Integer> optionalClinicID = clinicDAO.getClinicIDByName(selectedClinic);
            if (optionalClinicID.isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this,
                        "Error",
                        "No se ha encontrado la clínica seleccionada."
                );
                return;
            }
            this.clinicID = optionalClinicID.get();

            // Load doctors from the clinic selected and of that specific speciality
            List<Doctor> doctors = doctorDAO.findDoctorsByClinicAndSpeciality(clinicID, selectedSpeciality);
            if (doctors.isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this,
                        "Error",
                        "No hay doctores disponibles para la clínica y especialidad seleccionadas."
                );
                return;
            }

            // Now populate the ComboBox
            doctorScheduleMap = new HashMap<>();
            doctorTimeSlots = new HashMap<>();

            // Adds an ArrayList as a placeholder for the time slots later
            for (Doctor doctor : doctors) {
                doctorScheduleMap.put(doctor.getFirstName() + " " + doctor.getSurname(), new ArrayList<>());
            }

            doctorTimeSlots = doctorDAO.getDoctorAvailableTimeSlots(clinicID, java.sql.Date.valueOf(LocalDate.now()));

            SwingUtilities.invokeLater(() -> {
                doctorComboBox.setModel(new DefaultComboBoxModel<>(doctorScheduleMap.keySet().toArray(new String[0])));
                doctorComboBox.setSelectedIndex(-1);
            });

        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Could not load all doctors from DB in Calendar");
            NotificationPopUp.showErrorMessage(
                    this, "Error", "No se pudieron cargar los médicos."
            );
        }
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

    // Extracts the date from an appointment
    private LocalDate extractDateFromAppointment(String appointmentText) {
        return confirmedAppointments.stream()
                .filter(a -> a.toString().equals(appointmentText))
                .map(a -> a.getAppointmentDateTime().toLocalDateTime().toLocalDate())
                .findFirst()
                .orElse(null);
    }

    // Extracts the time from an appointment
    private LocalTime extractTimeFromAppointment(String appointmentText) {
        return confirmedAppointments.stream()
                .filter(a -> a.toString().equals(appointmentText))
                .map(a -> a.getAppointmentDateTime().toLocalDateTime().toLocalTime())
                .findFirst()
                .orElse(null);
    }


    // Updates the left panel to display available time slots for a selected day
    private void updateTimeSlots(LocalDate selectedDate) {
        leftPanel.removeAll();

        JLabel dateLabel = new JLabel("Horarios disponibles para: " + selectedDate, SwingConstants.CENTER);
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
            JLabel noSlotsLabel = new JLabel("No hay horarios disponibles", SwingConstants.CENTER);
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
        try {
            // Ask for the patient motive of appointment
            String appointmentReason = JOptionPane.showInputDialog(
                    this,
                    "Ingrese el motivo de la consulta: ",
                    "Motivo de la cita",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (appointmentReason == null || appointmentReason.trim().isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this,
                        "Error",
                        "Debe ingresar un motivo para la consulta"
                );
                return;
            }

            String dateTimeString = date + " " + time; // Result: YYYY-MM-DD HH:MM
            Timestamp appointmentTimestamp = Timestamp.valueOf(dateTimeString + ":00"); // :00 seconds

            Appointment newAppointment = new Appointment(
                    patient.getDNI(), selectedDoctorEntity.getDNI(), clinicID,
                    appointmentTimestamp, appointmentReason
            );

            appointmentDAO.save(newAppointment);

            confirmedAppointments.add(newAppointment);
            // Adds this specific appointment to the historic
            historyModel.addElement("Cita con " + selectedDoctor + " el " + date + " a las " + time);
            appointmentDays.add(date); // Adds it to the list of appointments injected to PanelDate

            NotificationPopUp.showInfoMessage(this, "Cita confirmada con éxito.", "Éxito");

            // Removes booked slot
            doctorTimeSlots.get(selectedDoctor).remove(time);

            if (appointmentHistoryPanel.getParent() == null) {
                leftPanel.add(appointmentHistoryPanel, BorderLayout.SOUTH);
                leftPanel.revalidate();
                leftPanel.repaint();
            }
            updateDoctorSchedule(true);
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error saving appointment", e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se pudo guardar la cita."
            );
        }
    }

    public void showAppointmentDetails(LocalDate date) {
        Appointment appointment = confirmedAppointments.stream()
                .filter(a -> a.getAppointmentDateTime().toLocalDateTime().toLocalDate().equals(date))
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
                "Detalles de la Cita:\n" + appointment.formatedAppointment(),
                "Información de Cita",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[options.length - 1]
        );

        if (isFuture && selection == 0) {
            cancelAppointment(appointment);

            // Force UI update to remove the appointment marker from the calendar
            historyList.updateUI();
        }
    }

    private void cancelAppointment(Appointment appointmentToCancel) {
        LocalDate date = appointmentToCancel.getAppointmentDateTime().toLocalDateTime().toLocalDate();
        String time = appointmentToCancel.getAppointmentDateTime().toLocalDateTime().toLocalTime().toString();

        // Remove from confirmed appointments
        confirmedAppointments.removeIf(a -> a.getAppointmentDateTime().equals(appointmentToCancel.getAppointmentDateTime()));
        appointmentDays.remove(date);

        doctorTimeSlots.get(selectedDoctor).add(time); // Frees the slot

        // Remove from history model
        for (int i = 0; i < historyModel.getSize(); i++) {
            LocalDate appointmentDate = extractDateFromAppointment(historyModel.getElementAt(i));
            LocalTime appointmentTime = extractTimeFromAppointment(historyModel.getElementAt(i));
            if (appointmentDate != null && appointmentDate.equals(date) && appointmentTime.equals(time)) {
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
        doctorComboBox.setPreferredSize(new Dimension(200, 30));
        doctorComboBox.addActionListener(e -> handleDoctorSelection());

        selectedDoctor = null; // Ensure no doctor is preselected
    }

    // Handles doctor selection, ensuring time slots and calendar update dynamically
    private void handleDoctorSelection() {
        selectedDoctor = (String) doctorComboBox.getSelectedItem();
        if (selectedDoctor != null) {
            loadDoctorEntityFromDB(selectedDoctor);
            updateDoctorSchedule(true);
            changeDoctorButton.setVisible(true); // Now show the button to go back
        }
    }

    private void loadDoctorEntityFromDB(String doctorName) {
        try {
            // Extract the name from the ComboBox selectedDoctor String
            String[] nameParts = doctorName.split(" ");
            if (nameParts.length < 1) {
                LOGGER.log(Level.SEVERE, "Invalid doctor name format: " + doctorName);
                return;
            }
            String firstName = nameParts[0];
            String lastName = String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length));

            Optional<Doctor> doctorOptional = doctorDAO.findByName(firstName, lastName);
            if (doctorOptional.isEmpty()) {
                NotificationPopUp.showErrorMessage(
                        this,
                        "Error",
                        "Doctor " + doctorName + " no encontrado"
                );
            }
            this.selectedDoctorEntity = doctorOptional.get();

        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error fetching doctors with name " + doctorName, e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se ha podido cargar la información del doctor."
            );
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
            LocalTime time = extractTimeFromAppointment(selectedAppointment);
            appointmentDays.remove(date);
            doctorTimeSlots.get(selectedDoctor).add(time.toString());

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
