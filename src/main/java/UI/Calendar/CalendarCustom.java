package UI.Calendar;

import Database.AaModels.*;
import Database.DAO.*;
import Exceptions.DatabaseOpeningException;
import Exceptions.DatabaseQueryException;
import Components.NotificationPopUp;
import Utils.Utility.CustomLogger;
import Utils.Utility.ImageIconRedrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;

public class CalendarCustom extends JPanel {
    private final Logger LOGGER = CustomLogger.getLogger(CalendarCustom.class);

    private final Patient patient;
    private final ImageIconRedrawer iconRedrawer;
    private int month, year, clinicID;
    private String selectedClinic, selectedSpeciality;
    private Doctor selectedDoctorEntity;
    private String selectedDoctor;

    private DoctorDAO doctorDAO;
    private ClinicDAO clinicDAO;
    private AppointmentDAO appointmentDAO;
    private DoctorAvailabilityDAO availabilityDAO;
    private TimeSlotDAO timeSlotDAO;

    private JComboBox<String> doctorComboBox;
    private Map<String, List<LocalDate>> doctorScheduleMap;
    private Map<String, List<String>> doctorTimeSlots;
    private Set<LocalDate> appointmentDays = new HashSet<>();

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);

    private JLabel monthYearLabel;
    private PanelSlide slide;
    private JButton backButton, nextButton, changeDoctorButton;

    private JPanel rightPanel, leftPanel, selectionPanel, appointmentHistoryPanel;
    private PanelDate currentPanelDate;

    private DefaultListModel<String> historyModel = new DefaultListModel<>();
    private JList<String> historyList = new JList<>(historyModel);
    private boolean isSyncingAppointments = false;


    public CalendarCustom(Patient patient, String selectedClinic, String selectedSpeciality) {
        this.iconRedrawer = new ImageIconRedrawer();
        this.patient = patient;
        this.selectedClinic = selectedClinic;
        this.selectedSpeciality = selectedSpeciality;

        initDAOs();
        loadDoctorData();
        setupLayout();
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        thisMonth();

        initLeftPanel(); // Left panel for specific time scheduling
        initRightPanel(); // Right panel containing the calendar
        setupMainLayout();
    }

    private void initDAOs() {
        try {
            this.doctorDAO = new DoctorDAO();
            this.clinicDAO = new ClinicDAO();
            this.appointmentDAO = new AppointmentDAO();
            this.availabilityDAO = new DoctorAvailabilityDAO();
            this.timeSlotDAO = new TimeSlotDAO();
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

    private void loadDoctorData() {
        try {
            Optional<Integer> optionalClinicID = clinicDAO.getClinicIDByName(selectedClinic);
            if (optionalClinicID.isEmpty()) {
                NotificationPopUp.showErrorMessage(this, "Error", "No se ha encontrado la clínica seleccionada.");
                return;
            }
            this.clinicID = optionalClinicID.get();

            List<Doctor> doctors = doctorDAO.findDoctorsByClinicAndSpeciality(clinicID, selectedSpeciality);
            if (doctors.isEmpty()) {
                NotificationPopUp.showErrorMessage(this, "Error", "No hay doctores disponibles para la clínica y especialidad seleccionadas.");
                return;
            }

            doctorScheduleMap = new HashMap<>();
            doctorTimeSlots = new HashMap<>();
            doctorComboBox = new JComboBox<>();
            doctorComboBox.setPreferredSize(new Dimension(200, 30));

            Set<String> addedDoctors = new HashSet<>();

            // Withdraws the full name of the doctors and adds it to the first part of the map
            // This part will be used for the Doctor ComboBox
            // It also will get the days that specific Doctor is working in the selected clinic
            // and put them in the HashMap
            for (Doctor doctor : doctors) {
                String doctorFullName = doctor.getFirstName() + " " + doctor.getSurname();

                // If we have already added this doctor, skip to the next one
                if (!addedDoctors.add(doctorFullName)) {
                    continue;
                }

                List<DoctorAvailability> doctorAvailabilityList = availabilityDAO.getDoctorAvailableDays(doctor.getDNI(), clinicID);

                List<LocalDate> availableDates = doctorAvailabilityList.stream()
                        .map(DoctorAvailability::getDate)  // Filter to only retrieve the date
                        .map(java.sql.Date::toLocalDate) // And now convert the sql.Date to LocalDate
                        .toList();

                doctorScheduleMap.put(doctorFullName, availableDates);
                doctorComboBox.addItem(doctorFullName);
            }

            doctorComboBox.addActionListener(e -> handleDoctorSelection());

            // Adds doctors to the ComboBox selection
            SwingUtilities.invokeLater(() -> doctorComboBox.setSelectedIndex(-1));

        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Could not load all doctors from DB in Calendar", e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudieron cargar los médicos.");
        }
    }

    // Updates the displayed calendar based on the selected doctor's available days
    private void updateDoctorSchedule(boolean toRight) {
        syncAppointmentsWithHistory();

        List<LocalDate> availableDays;

        if (selectedDoctorEntity != null) {
            String doctorFullName = selectedDoctorEntity.getFirstName() + " " + selectedDoctorEntity.getSurname();
            availableDays = doctorScheduleMap.getOrDefault(doctorFullName, List.of());
        } else {
            availableDays = List.of(); // If no doctor is selected, then an empty List is passed
        }

        showMonthYear();

        currentPanelDate = new PanelDate(month, year, availableDays, this::loadAvailableTimeSlots, appointmentDays, this::showAppointmentDetails);

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

    private void loadAvailableTimeSlots(LocalDate selectedDate) {
        leftPanel.removeAll();
        JLabel dateLabel = new JLabel("Horarios disponibles para: " + selectedDate, SwingConstants.CENTER);
        dateLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        dateLabel.setForeground(Color.WHITE);
        leftPanel.add(dateLabel, BorderLayout.NORTH);

        JPanel timeSlotPanel = new JPanel(new GridLayout(5, 1, 5, 5));

        try {
            List<TimeSlot> availableTimeSlots = availabilityDAO.findAvailableTimeSlotsByDoctorAndDate(
                    selectedDoctorEntity.getDNI(), clinicID, selectedDate);

            if (!availableTimeSlots.isEmpty()) {
                for (TimeSlot timeSlot : availableTimeSlots) {
                    JButton slotButton = new JButton(timeSlot.getHour().toString());
                    slotButton.addActionListener(e -> confirmAppointment(selectedDate, timeSlot.getHour().toString()));
                    timeSlotPanel.add(slotButton);
                }
            } else {
                JLabel noSlotsLabel = new JLabel("No hay horarios disponibles", SwingConstants.CENTER);
                noSlotsLabel.setForeground(Color.WHITE);
                timeSlotPanel.add(noSlotsLabel);
            }

        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading available time slots for " + selectedDoctorEntity.getDNI() + " on " + selectedDate, e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudo cargar los horarios disponibles.");
        }

        leftPanel.add(timeSlotPanel, BorderLayout.CENTER);
        leftPanel.add(changeDoctorButton, BorderLayout.NORTH);

        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // Extracts the date from an appointment string in historyModel
    private LocalDate extractDateFromAppointment(String appointmentText) {
        try {
            // Extracts the date from the appointment String (it is a fixed format)
            String[] parts = appointmentText.split(" el ");
            if (parts.length < 2) return null;

            String dateString = parts[1].split(" a las ")[0].trim();
            LocalDate appointmentDate = LocalDate.parse(dateString, dateFormatter);

            // Queries an appointment by patient and date
            Optional<Appointment> optionalAppointment = appointmentDAO.getAppointmentsByPatientAndDate(patient.getDNI(), appointmentDate)
                    .stream()
                    .findFirst();

            return optionalAppointment.map(a -> a.getAppointmentDateTime().toLocalDateTime().toLocalDate()).orElse(null);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error extracting appointment date from text: " + appointmentText, e);
            return null;
        }
    }


    // Extracts the time from an appointment string in historyModel
    private LocalTime extractTimeFromAppointment(String appointmentText) {
        try {
            // Extracts the time from the appointment String
            String[] parts = appointmentText.split(" a las ");
            if (parts.length < 2) return null;

            String timeString = parts[1].trim();
            LocalTime appointmentTime = LocalTime.parse(timeString, timeFormatter);

            return appointmentTime;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error extracting appointment time from text: " + appointmentText, e);
            return null;
        }
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
        boolean confirmedAppointment = NotificationPopUp.showConfirmationMessage(
                this,
                "Confirmar cita",
                "Doctor: " + selectedDoctorEntity.getFirstName() + " " + selectedDoctorEntity.getSurname() +
                        "\nFecha: " + date + "\nHora: " + time +
                        "\n\n¿Desea confirmar esta cita?");
        if (confirmedAppointment) registerAppointment(date, time);
    }

    // Helps updating the historic of appointments
    private void registerAppointment(LocalDate date, String time) {
        try {
            String appointmentReason = JOptionPane.showInputDialog(
                    this, "Ingrese el motivo de la consulta: ", "Motivo de la cita", JOptionPane.QUESTION_MESSAGE
            );

            if (appointmentReason == null || appointmentReason.trim().isEmpty()) {
                NotificationPopUp.showErrorMessage(this, "Error", "Debe ingresar un motivo para la consulta");
                return;
            }

            String dateTimeString = date.toString() + " " + time; // "YYYY-MM-DD HH:mm:00"
            Timestamp appointmentTimestamp = Timestamp.valueOf(dateTimeString);

            Appointment newAppointment = new Appointment(
                    patient.getDNI(), selectedDoctorEntity.getDNI(), clinicID,
                    appointmentTimestamp, appointmentReason, AppointmentState.CONFIRMED
            );

            appointmentDAO.save(newAppointment);

            addAppointmentToHistory(
                    selectedDoctorEntity.getFirstName(), selectedDoctorEntity.getSurname(), date, String.valueOf(time)
            );

            appointmentDays.add(date);

            // Verifies if the doctor has any time slots available before removing
            doctorTimeSlots.computeIfAbsent(selectedDoctorEntity.getDNI(), k -> new ArrayList<>()).remove(time);

            updateDoctorSchedule(true);
            NotificationPopUp.showInfoMessage(this, "Cita confirmada con éxito.", "Éxito");

            returnToDoctorSelection();

        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error saving appointment", e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudo guardar la cita.");
        }
    }

    private void showAppointmentDetails(LocalDate date) {
        try {
            List<Appointment> appointments = appointmentDAO.getAppointmentsByPatientAndDate(patient.getDNI(), date);

            if (appointments.isEmpty()) {
                NotificationPopUp.showInfoMessage(this, "No hay detalles disponibles.", "Información");
                return;
            }

            Optional<Appointment> optionalAppointment = appointments.stream()
                    .filter(a -> a.getPatientDNI().equals(patient.getDNI()))
                    .findFirst();

            if (optionalAppointment.isEmpty()) {
                NotificationPopUp.showInfoMessage(this, "No hay citas disponibles para este paciente en la fecha seleccionada.", "Información");
                return;
            }

            Appointment appointment = optionalAppointment.get();
            boolean isFuture = date.isAfter(LocalDate.now());

            Optional<Doctor> doctorOptional = doctorDAO.findById(appointment.getDoctorDNI());
            String doctorName = doctorOptional
                    .map(d -> d.getSurname() + ", " + d.getFirstName())
                    .orElse("Doctor no encontrado");

            String detailsMessage = "Doctor: " + doctorName +
                    "\nFecha: " + date.format(dateFormatter) +
                    "\nHora: " + appointment.getAppointmentDateTime().toLocalDateTime().toLocalTime().format(timeFormatter) +
                    "\nEstado: " + appointment.getAppointmentState().getValue() +
                    "\nMotivo: " + appointment.getDescription();

            Object[] options;
            if (isFuture) {
                options = new Object[]{"Cancelar cita", "Cerrar"};
            } else {
                options = new Object[]{"Cerrar"};
            }

            int selection = JOptionPane.showOptionDialog(
                    this, detailsMessage, "Detalles de la Cita",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[options.length - 1]
            );

            if (isFuture && selection == 0) {
                confirmCancelAppointment(appointment);
            }
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading appointment details for date " + date, e);
            NotificationPopUp.showErrorMessage(this, "Error", "No se pudo cargar la información de la cita.");
        }
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

    // Handles doctor selection, ensuring time slots and calendar update dynamically
    private void handleDoctorSelection() {
        String selectedName = (String) doctorComboBox.getSelectedItem();

        System.out.println("SELECTED: " + selectedName);

        if (selectedName != null) {
            selectedDoctorEntity = doctorScheduleMap.keySet()
                    .stream()
                    .filter(name -> name.equals(selectedName))
                    .map(name -> new DoctorDAO().findByName(selectedName.split(" ")[0], selectedName.split(" ")[1]))
                    .flatMap(Optional::stream)
                    .findFirst()
                    .orElse(null);

            System.out.println("ENTITY IS: " + selectedDoctorEntity.getDNI());

            if (selectedDoctorEntity == null) {
                LOGGER.log(Level.SEVERE, "Doctor " + selectedName + " no encontrado en la base de datos.");
                NotificationPopUp.showErrorMessage(this, "Error", "Doctor " + selectedName + " no encontrado.");
                return;
            }

            selectedDoctor = selectedName;
            updateDoctorSchedule(true);
            changeDoctorButton.setVisible(true);
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
                        System.out.println("Selected appointment is: " + selectedAppointment);
                        LocalDate date = extractDateFromAppointment(selectedAppointment);
                        System.out.println("Date is: " + date);
                        showAppointmentDetails(date);
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

        // Loads the appointments of the patient from the DB
        loadAppointmentHistory();
    }

    private void confirmCancelAppointment(Appointment appointmentToCancel) {
        boolean confirmed =
                NotificationPopUp.showConfirmationMessage(this, "Cancelar cita",
                        "¿Estás seguro de que deseas cancelar esta cita?\n\n" + appointmentToCancel.formatedAppointment());

        if (confirmed) {
            try {
                appointmentToCancel.setAppointmentState(AppointmentState.CANCELLED);
                appointmentDAO.update(appointmentToCancel);

                NotificationPopUp.showInfoMessage(this, "Cita cancelada", "Cita cancelada con éxito.");

                // Refresh UI
                historyList.updateUI();
                syncAppointmentsWithHistory();
                updateDoctorSchedule(true);

            } catch (DatabaseQueryException e) {
                LOGGER.log(Level.SEVERE, "Error cancelando cita en BD", e);
                NotificationPopUp.showErrorMessage(this, "Error", "No se pudo cancelar la cita.");
            }
        }
    }

    private void loadAppointmentHistory() {
        try {
            historyModel.clear();

            List<Appointment> appointments = appointmentDAO.findAppointmentsByPatient(patient.getDNI());

            if (appointments.isEmpty()) {
                historyModel.addElement("No hay citas registradas.");
            } else {
                for (Appointment appointment : appointments) {
                    LocalDate date = appointment.getAppointmentDateTime()
                            .toLocalDateTime().toLocalDate();
                    LocalTime time = appointment.getAppointmentDateTime()
                            .toLocalDateTime().toLocalTime();

                    Optional<Doctor> doctorOptional = doctorDAO.findById(appointment.getDoctorDNI());
                    if (doctorOptional.isEmpty()) {
                        return;
                    }
                    Doctor doctor = doctorOptional.get();

                    addAppointmentToHistory(doctor.getFirstName(), doctor.getSurname(), date, String.valueOf(time));
                }
            }
        } catch (DatabaseQueryException e) {
            LOGGER.log(Level.SEVERE, "Error loading appointment history for patient: " + patient.getDNI(), e);
            NotificationPopUp.showErrorMessage(
                    this,
                    "Error",
                    "No se pudo cargar el historial de citas.");
        }
    }

    private void addAppointmentToHistory(String doctorFirstName, String doctorLastName, LocalDate date, String time) {
        // "hh:mm a" (example: "11:15 AM")
        String formattedTime = LocalTime.parse(time).format(timeFormatter);

        //  (YYYY-MM-DD -> 2025-03-10)
        String formattedDate = date.format(dateFormatter);

        // Same format always
        String appointmentText = "Cita con " + doctorLastName + ", " + doctorFirstName +
                " el " + formattedDate + " a las " + formattedTime;

        historyModel.addElement(appointmentText);
    }

    // Configures the right panel, which contains the calendar and navigation buttons
    private void initRightPanel() {
        rightPanel = new JPanel(new BorderLayout());

        JPanel monthPanel = new JPanel(new BorderLayout());

        backButton = createNavigationButton("/LandingPage/previous.png", e -> goToPreviousMonth());
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton = createNavigationButton("/LandingPage/next.png", e -> goToNextMonth());
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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
