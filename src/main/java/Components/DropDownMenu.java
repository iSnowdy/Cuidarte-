package Components;

import MainApplication.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

/**
 * Custom dropdown menu for navigation between different sections.
 */
public class DropDownMenu extends JPopupMenu {
    private final String[] menuItems = {"Página Principal", "Mi Portal", "Nuestros Centros", "Quiénes Somos"};
    private final NavigationController navigationController;
    private JPanel selectedPanel = null;

    /**
     * Constructor that initializes the dropdown menu.
     *
     * @param navigationController The controller responsible for switching panels.
     */
    public DropDownMenu(NavigationController navigationController) {
        this.navigationController = navigationController;
        initDropDownMenu();
    }

    /**
     * Initializes the dropdown menu UI.
     */
    private void initDropDownMenu() {
        setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1));
        setBackground(Color.WHITE);
        addMenuItems();
    }

    /**
     * Adds items to the dropdown menu.
     */
    private void addMenuItems() {
        for (String menuItem : menuItems) {
            JPanel itemPanel = createMenuItem(menuItem);
            add(itemPanel);
        }
    }

    /**
     * Creates a styled menu item with hover and click effects.
     *
     * @param menuItemText The text displayed in the menu item.
     * @return A JPanel representing the menu item.
     */
    private JPanel createMenuItem(String menuItemText) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel itemLabel = new JLabel(menuItemText);
        itemLabel.setFont(MAIN_FONT);
        itemLabel.setForeground(Color.BLACK);

        itemPanel.add(itemLabel, BorderLayout.CENTER);

        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(MAIN_APP_COLOUR);
                itemLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (itemPanel != selectedPanel) {
                    itemPanel.setBackground(Color.WHITE);
                    itemLabel.setForeground(Color.BLACK);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                navigationController.switchPanel(menuItemText);
                highlightSelectedItem(itemPanel);
            }
        });

        return itemPanel;
    }

    /**
     * Highlights the selected menu item by changing its background color.
     *
     * @param selected The menu item panel that was clicked.
     */
    private void highlightSelectedItem(JPanel selected) {
        if (selectedPanel != null) {
            // Reset previous panel color
            selectedPanel.setBackground(Color.WHITE);

            // Reset previous text color
            JLabel previousLabel = (JLabel) selectedPanel.getComponent(0);
            previousLabel.setForeground(Color.BLACK);
        }

        // Apply highlight effect to new selection
        selected.setBackground(MAIN_APP_COLOUR);
        JLabel newLabel = (JLabel) selected.getComponent(0);
        newLabel.setForeground(Color.WHITE); // Ensure text color changes

        selectedPanel = selected;
    }


    /**
     * Displays the dropdown menu under the specified component.
     *
     * @param component The component that triggers the dropdown menu.
     */
    public void showMenu(Component component) {
        show(component, 0, component.getHeight());
    }
}
