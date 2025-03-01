package UI.Authentication;

import Components.CustomizedButtonOutline;
import Components.ShadowedLabel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.SECONDARY_APP_COLOUR;
import static Utils.Swing.Fonts.*;

public class PanelCover extends JPanel {
    private final MigLayout layout;
    private ActionListener actionListener;
    private final JLabel logoLabel;
    private final ShadowedLabel title;
    private final JLabel description;
    private final CustomizedButtonOutline buttonOutline;
    private boolean isLogin;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("##0.###");

    public PanelCover() {
        setOpaque(false);
        this.layout = new MigLayout("wrap, fill", "[center]", "push[]100[]15[]40[]push");
        setLayout(layout);

        // Logo at the top
        logoLabel = new JLabel(new ImageIcon(getClass().getResource("/General/white-app-logo.png")));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // UI Elements
        title = createTitleShadowedLabel("Bienvenido");
        description = createLabel("Para poder continuar ha de registrarse");
        buttonOutline = createButton("Iniciar sesión", Color.WHITE, Color.WHITE);

        add(logoLabel, "align center, gaptop 20");
        add(title);
        add(description);
        add(buttonOutline, "w 50%, h 40");
    }

    private ShadowedLabel createTitleShadowedLabel(String title) {
        return new ShadowedLabel(title, PANEL_TITLE_FONT, Color.WHITE);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    private CustomizedButtonOutline createButton(String text, Color fg, Color bg) {
        CustomizedButtonOutline button = new CustomizedButtonOutline();
        button.setText(text);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setFont(COMBOBOX_FONT);
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
