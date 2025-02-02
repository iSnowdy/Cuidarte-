package Authentication.Swing;

import Authentication.Components.CustomizedButtonOutline;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import static Utils.Colors.*;
import static Utils.Fonts.MAIN_FONT;

public class PanelCover extends JPanel {
    private MigLayout migLayout;
    private ActionListener actionlistener;
    private JLabel title;
    private JLabel description;
    private JLabel secondaryDescription;
    private CustomizedButtonOutline buttonOutline; // what is this?
    private boolean isLogin;
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.###"); // Same as in Main


    public PanelCover() {
        setOpaque(false);
        //setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        init(); // TODO: Too much code in here. Refactor it. Extract methods etc
    }

    private void init() {
        this.migLayout = new MigLayout("wrap, fill", "[center]", "push[]25[]10[]25[]push");
        setLayout(migLayout);

        this.title = new JLabel("Title 1"); // TODO: Stylize it
        this.title.setFont(MAIN_FONT);
        this.title.setForeground(Color.RED);
        this.add(title);

        this.description = new JLabel("Description 1"); // TODO: Stylize it
        this.description.setFont(MAIN_FONT);
        this.description.setForeground(Color.YELLOW);
        this.add(description);

        this.secondaryDescription = new JLabel("Secondary Description 1"); // TODO: Stylize it
        this.secondaryDescription.setFont(MAIN_FONT);
        this.secondaryDescription.setForeground(Color.YELLOW);
        this.add(secondaryDescription);

        this.buttonOutline = new CustomizedButtonOutline();
        this.buttonOutline.setText("Button Outline");
        this.buttonOutline.setBackground(Color.YELLOW);
        this.buttonOutline.setForeground(Color.RED);
        this.buttonOutline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                actionlistener.actionPerformed(actionEvent);
            }
        });
        // Play with this. It must be the same values as the other elements
        this.add(buttonOutline, "w 60%, h 40");
    }

    private void jButtonActionPerformed(ActionEvent e) {
        this.actionlistener.actionPerformed(e);
    }

    // Animation for the text
    public void registerLeft(double value) {
        value = Double.valueOf(decimalFormat.format(value));
        login(false);
        this.migLayout.setComponentConstraints(this.title, "pad 0 -" + value + "% 0 0");
        this.migLayout.setComponentConstraints(this.description, "pad 0 -" + value + "% 0 0");
        this.migLayout.setComponentConstraints(this.secondaryDescription, "pad 0 -" + value + "% 0 0");
    }

    public void registerRight(double value) {
        value = Double.valueOf(decimalFormat.format(value));
        login(false);
        this.migLayout.setComponentConstraints(this.title, "pad 0 -" + value + "% 0 0");
        this.migLayout.setComponentConstraints(this.description, "pad 0 -" + value + "% 0 0");
        this.migLayout.setComponentConstraints(this.secondaryDescription, "pad 0 -" + value + "% 0 0");
    }

    public void loginLeft(double value) {
        value = Double.valueOf(decimalFormat.format(value));
        login(true);
        this.migLayout.setComponentConstraints(this.title, "pad 0 " + value + "% 0 " + value + "%");
        this.migLayout.setComponentConstraints(this.description, "pad 0 " + value + "% 0 " + value + "%");
        this.migLayout.setComponentConstraints(this.secondaryDescription, "pad 0 " + value + "% 0 " + value + "%");
    }

    public void loginRight(double value) {
        value = Double.valueOf(decimalFormat.format(value));
        login(true);
        this.migLayout.setComponentConstraints(this.title, "pad 0 " + value + "% 0 " + value + "%");
        this.migLayout.setComponentConstraints(this.description, "pad 0 " + value + "% 0 " + value + "%");
        this.migLayout.setComponentConstraints(this.secondaryDescription, "pad 0 " + value + "% 0 " + value + "%");
    }

    // Changes text according to what panel are we in (Login / Register)
    // It only updates the text if the state of isLogin is changed, optimizing the UI
    // First time running the program it takes the creation values in init()
    private void login(boolean login) {
        if (this.isLogin != login) {
            if (login) {
                title.setText("Login True Title");
                description.setText("Login True Description");
                secondaryDescription.setText("Login True Secondary Description");
                buttonOutline.setText("Login True ButtonOutline");
            } else {
                title.setText("Login False Title");
                description.setText("Login False Description");
                secondaryDescription.setText("Login False Secondary Description");
                buttonOutline.setText("Login False ButtonOutline");
            }
            this.isLogin = login;
        }
    }

    // Paints the coloured background for the Login / Register
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0,0,MAIN_APP_COLOUR,100,100, SECONDARY_APP_COLOUR);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addEvent(ActionListener event) {
        this.actionlistener = event;
    }
}
