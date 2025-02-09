package LandingPage.Components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

public class DropDownMenu extends JPopupMenu {
    private final String[] menuItems;

    public DropDownMenu() {
        this.menuItems = new String[]{"Mi Portal", "Contacto", "Nuestros Centros", "Quiénes Somos"};


        initDropDownMenu();
    }

    private void initDropDownMenu() {
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAIN_APP_COLOUR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        setOpaque(true);
        setBackground(Color.WHITE);

        addMenuItems();
    }

    private void addMenuItems() {
        for (String menuItem : menuItems) {
            JPanel item = createMenuItem(menuItem);
            add(item);
        }
    }

    // Style each item of the menu
    private JPanel createMenuItem(String menuItemText) {
        JPanel itemPanel = new JPanel(new BorderLayout());

        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        itemPanel.setBackground(Color.WHITE);
        itemPanel.setOpaque(true);
        itemPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel itemLabel = new JLabel(menuItemText);
        itemLabel.setFont(MAIN_FONT);
        itemLabel.setForeground(Color.BLACK);
        itemLabel.setOpaque(false);

        itemPanel.add(itemLabel, BorderLayout.CENTER);

        // Hover effect
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                itemPanel.setBackground(MAIN_APP_COLOUR);
                itemLabel.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                itemPanel.setBackground(Color.WHITE);
                itemLabel.setForeground(Color.BLACK);
            }

            // Future implementation
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Seleccionaste: " + menuItemText);
            }
        });

        return itemPanel;
    }

    // Summons the dropdown menu on that component in those coordinates. Makes it a bit more flexible
    public void showMenu(Component component) {
        show(component, 0, component.getHeight());
    }
}
