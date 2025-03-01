package Components;

import MainApplication.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

public class DropDownMenu extends JPopupMenu {
    private final String[] menuItems = {"Página Principal", "Mi Portal", "Nuestros Centros", "Quiénes Somos"};
    private final NavigationController navigationController;
    private JPanel selectedPanel = null;

    public DropDownMenu(NavigationController navigationController) {
        this.navigationController = navigationController;
        initDropDownMenu();
    }

    // Initializes the dropdown menu UI
    private void initDropDownMenu() {
        setBorder(BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(180, 160)); // Menu size
        addMenuItems();
    }

    // Adds items to the dropdown menu
    private void addMenuItems() {
        for (String menuItem : menuItems) {
            JPanel itemPanel = createMenuItem(menuItem);
            add(itemPanel);
        }
    }

    // Creates a menu item with hover effects
    private JPanel createMenuItem(String menuItemText) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15)); // Padding
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

    // Highlights the selected menu item
    private void highlightSelectedItem(JPanel selected) {
        if (selectedPanel != null) {
            selectedPanel.setBackground(Color.WHITE);
            JLabel previousLabel = (JLabel) selectedPanel.getComponent(0);
            previousLabel.setForeground(Color.BLACK);
        }

        selected.setBackground(MAIN_APP_COLOUR);
        JLabel newLabel = (JLabel) selected.getComponent(0);
        newLabel.setForeground(Color.WHITE);

        selectedPanel = selected;
    }

    // Displays the dropdown menu with adjusted right margin
    public void showMenu(Component component) {
        int menuWidth = getPreferredSize().width; // Get the dropdown menu width
        int menuHeight = getPreferredSize().height; // Get the dropdown menu height

        // Get screen location of the component (menu button)
        Point location = component.getLocationOnScreen();

        // Get the rightmost position of the app frame
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int componentRightEdge = location.x + component.getWidth();

        // Calculate offset to prevent it from touching the right border and the buttons
        int xOffset = (componentRightEdge + menuWidth > screenWidth) ? -menuWidth + component.getWidth() : 0;
        int yOffset = component.getHeight() + 10; // y axis spacing

        show(component, xOffset, yOffset);
    }
}