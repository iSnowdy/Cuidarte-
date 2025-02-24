package Calendar.Main;

import Calendar.Component.CalendarCustom;
import Calendar.Component.ClinicSpecialitySelectionDialog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JFrame {

    private CalendarCustom calendarPanel;
    private String selectedClinic;
    private String selectedSpecialty;

    public Main() {
        setTitle("Calendario - Citas Médicas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null); // Centers the window in the middle of the screen

        // Show clinic-specialty selection before initializing components
        if (!showClinicSpecialtySelection()) {
            System.exit(0); // Exit if the user cancels the selection
        }

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        //calendarPanel = new CalendarCustom();

        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // True of the user selects something
    private boolean showClinicSpecialtySelection() {
        Map<String, List<String>> clinicData = new HashMap<>();
        clinicData.put("Clinic A", List.of("Cardiology", "Neurology", "General Medicine"));
        clinicData.put("Clinic B", List.of("Dermatology", "Pediatrics", "Ophthalmology"));
        clinicData.put("Clinic C", List.of("Radiology", "Orthopedics", "Surgery"));

        //ClinicSpecialitySelectionDialog selectionDialog = new ClinicSpecialitySelectionDialog(this, clinicData);
        //selectionDialog.setVisible(true);

        // Retrieve selected values
        //selectedClinic = selectionDialog.getSelectedClinic();
        //selectedSpecialty = selectionDialog.getSelectedSpecialty();

        return selectedClinic != null && selectedSpecialty != null;
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
