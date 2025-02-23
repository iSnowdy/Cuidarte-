package Authentication.Swing;

import Authentication.Components.CustomTextField;
import Authentication.Components.CustomizedButtonOutline;
import Authentication.Components.PanelRound;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;

import static Utils.Swing.Colors.MAIN_APP_COLOUR;
import static Utils.Swing.Colors.MY_RED;
import static Utils.Swing.Fonts.MAIN_FONT;


public class PanelVerifyCode extends JPanel {
    private JLabel titleLabel;
    private JLabel instructionsLabel;
    private CustomTextField inputCode;
    private CustomizedButtonOutline acceptButton;
    private CustomizedButtonOutline cancelButton;
    private PanelRound verifyCodePanel;

    public PanelVerifyCode() {
        setOpaque(false);
        initComponents();
    }

    // Initializes all UI components
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(250, 500, 250, 500)); // TODO: Resize if needed

        verifyCodePanel = createPanelRound();
        titleLabel = createLabel("Código de Verificación", MAIN_FONT, new Color(63, 63, 63));
        instructionsLabel = createLabel("Recibirás un código de verificación en tu correo.", MAIN_FONT, new Color(63, 63, 63));

        inputCode = new CustomTextField();
        inputCode.setHorizontalAlignment(JTextField.CENTER);

        acceptButton = createButton("Aceptar", new Color(18, 138, 62));
        cancelButton = createButton("Cancelar", MY_RED);
        cancelButton.addActionListener(e -> hidePanel());

        JPanel buttonPanel = createButtonPanel(acceptButton, cancelButton);

        // Add components to panel
        verifyCodePanel.add(titleLabel);
        verifyCodePanel.add(Box.createVerticalStrut(10));
        verifyCodePanel.add(instructionsLabel);
        verifyCodePanel.add(Box.createVerticalStrut(20));
        verifyCodePanel.add(inputCode);
        verifyCodePanel.add(Box.createVerticalStrut(40));
        verifyCodePanel.add(buttonPanel);
        verifyCodePanel.add(Box.createVerticalStrut(20));

        add(verifyCodePanel, BorderLayout.CENTER);
    }

    private PanelRound createPanelRound() {
        PanelRound panel = new PanelRound();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Padding
        return panel;
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private CustomizedButtonOutline createButton(String text, Color background) {
        CustomizedButtonOutline button = new CustomizedButtonOutline();
        button.setText(text);
        button.setBackground(background);
        button.setPreferredSize(new Dimension(95, 30));
        return button;
    }

    private JPanel createButtonPanel(CustomizedButtonOutline accept, CustomizedButtonOutline cancel) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setOpaque(false);
        panel.add(accept);
        panel.add(cancel);
        return panel;
    }

    // Shows the verification panel
    public void showPanel() {
        setVisible(true);
        inputCode.requestFocusInWindow();
        inputCode.setText("");
    }

    // Hides the verification panel
    public void hidePanel() {
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setColor(new Color(50, 50, 50));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
        super.paintComponent(graphics);
    }

    public String getInputCode() {
        return inputCode.getText().trim();
    }

    public Optional<Integer> getInputCodeAsInt() {
        try {
            return Optional.of(Integer.parseInt(getInputCode()));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public void addEventButtonOK(ActionListener event) {
        acceptButton.addActionListener(event);
    }
}
