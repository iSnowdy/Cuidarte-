package Authentication.Swing;

import Authentication.Components.CustomizedButtonOutline;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;
import static Utils.Swing.Fonts.MAIN_FONT;

public class PanelCover extends JPanel {
    private final MigLayout layout;
    private ActionListener actionListener;
    private final JLabel logoLabel;
    private final JLabel title;
    private final JLabel description;
    private final CustomizedButtonOutline buttonOutline;
    private boolean isLogin;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.###");

    public PanelCover() {
        setOpaque(false);
        this.layout = new MigLayout("wrap, fill", "[center]", "push[]25[]15[]40[]push");
        setLayout(layout);

        // Logo at the top
        logoLabel = new JLabel(new ImageIcon(getClass().getResource("/General/app-logo.png")));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // UI Elements
        title = createLabel("¡Bienvenido!", MAIN_FONT, Color.WHITE);
        description = createLabel("Para poder continuar ha de registrarse", MAIN_FONT, Color.WHITE);
        buttonOutline = createButton("Iniciar sesión", Color.WHITE, Color.WHITE);

        add(logoLabel, "align center, gaptop 20");
        add(title);
        add(description);
        add(buttonOutline, "w 50%, h 40");
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private CustomizedButtonOutline createButton(String text, Color fg, Color bg) {
        CustomizedButtonOutline button = new CustomizedButtonOutline();
        button.setText(text);
        button.setForeground(fg);
        button.setBackground(bg);
        button.addActionListener(e -> {
            if (actionListener != null) {
                actionListener.actionPerformed(e);
            }
        });
        return button;
    }

    public void animateText(boolean isLogin) {
        this.isLogin = isLogin;
        updateText();
    }

    private void updateText() {
        if (isLogin) {
            title.setText("¡Bienvenido de vuelta!");
            description.setText("Para poder continuar ha de iniciar sesión");
            buttonOutline.setText("Registrarse");
        } else {
            title.setText("¡Bienvenido!");
            description.setText("Para poder continuar ha de registrarse");
            buttonOutline.setText("Iniciar sesión");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradientPaint = new GradientPaint(0, 0, MAIN_APP_COLOUR, getWidth(), getHeight(), SECONDARY_APP_COLOUR);
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void addEvent(ActionListener event) {
        this.actionListener = event;
    }
}
