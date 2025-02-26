package PortalPage.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

public class FilterComponent extends JPanel {
    // Combo box for selecting filter type
    private JComboBox<String> filterTypeCombo;
    // Panel that holds the dynamic criteria component (using CardLayout)
    private JPanel criteriaPanel;
    // Layout manager for criteriaPanel to switch between controls
    private CardLayout cardLayout;

    // Receives an array of filter types and a map of corresponding criteria components
    public FilterComponent(String[] filterTypes, Map<String, JComponent> criteriaComponents) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 0, 10, 0));

        // Create filter type combo box
        filterTypeCombo = new JComboBox<>(filterTypes);
        filterTypeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        filterTypeCombo.setMaximumSize(new Dimension(150, 25));
        add(filterTypeCombo);

        add(Box.createHorizontalStrut(40)); // Spacer between controls

        // Create the dynamic criteria panel with CardLayout
        cardLayout = new CardLayout();
        criteriaPanel = new JPanel(cardLayout);
        criteriaPanel.setBackground(Color.WHITE);
        criteriaPanel.setMaximumSize(new Dimension(150, 25));

        // Add each criteria component from the map
        for (Map.Entry<String, JComponent> entry : criteriaComponents.entrySet()) {
            JComponent comp = entry.getValue();
            comp.setName(entry.getKey());
            criteriaPanel.add(comp, entry.getKey());
        }
        add(criteriaPanel);

        // Show the first filter type by default
        cardLayout.show(criteriaPanel, filterTypes[0]);

        // Add action listener to update criteria when filter type changes
        filterTypeCombo.addActionListener(e -> cardLayout.show(criteriaPanel, (String) filterTypeCombo.getSelectedItem()));
    }

    // Returns the selected filter type from the combo box
    public String getSelectedFilterType() {
        return (String) filterTypeCombo.getSelectedItem();
    }

    // Returns the currently visible criteria component
    public JComponent getCurrentCriteriaComponent() {
        for (Component comp : criteriaPanel.getComponents()) {
            if (comp.isVisible()) {
                return (JComponent) comp;
            }
        }
        return null;
    }

    // Adds an action listener to the filter type combo box
    public void addFilterActionListener(ActionListener listener) {
        filterTypeCombo.addActionListener(listener);
    }
}
