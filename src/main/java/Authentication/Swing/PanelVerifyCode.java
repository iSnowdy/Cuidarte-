package Authentication.Swing;

import Authentication.Components.CustomTextField;
import Authentication.Components.CustomizedButton;
import Authentication.Components.CustomizedButtonOutline;
import Authentication.Components.PanelRound;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;

import static Utils.Colors.MY_RED;
import static Utils.Fonts.MAIN_FONT;

public class PanelVerifyCode extends JPanel {
    private JLabel titleLabel;
    private JLabel instructionsLabel;
    private CustomTextField inputCode;
    private CustomizedButtonOutline acceptButton;
    private CustomizedButtonOutline cancelButton;
    private PanelRound verifyCodePanel;

    public PanelVerifyCode() {
        setOpaque(false);
        setFocusCycleRoot(true);
        super.setVisible(false);

        initComponents();
        // To prevent the user from clicking outside the generated panel
        addMouseListener(new MouseAdapter() {});
    }
    // TODO: Refactor all of this
    // TODO: Stylize properly
    private void initComponents() {
        // Title JLabel
        titleLabel = new JLabel("Verificar Código");
        titleLabel.setFont(MAIN_FONT);
        titleLabel.setForeground(new Color(63, 63, 63));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Instructions
        instructionsLabel = new JLabel("Tendrá el código de verificación en el correo introduzido");
        instructionsLabel.setForeground(new Color(63, 63, 63));
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Input code field
        inputCode = new CustomTextField();
        inputCode.setHorizontalAlignment(JTextField.CENTER);

        acceptButton = new CustomizedButtonOutline();
        acceptButton.setText("Aceptar");
        acceptButton.setBackground(new Color(18, 138, 62));
        // TODO: Check button(s) size
        acceptButton.setPreferredSize(new Dimension(95, 30));

        // Upon pressing cancel, the Panel hides again
        cancelButton = new CustomizedButtonOutline();
        cancelButton.setText("Cancelar");
        cancelButton.setBackground(MY_RED);
        cancelButton.setPreferredSize(new Dimension(95, 30));
        cancelButton.addActionListener(e -> setVisible(false));

        // Panel for the text labels
        verifyCodePanel = new PanelRound();
        verifyCodePanel.setBackground(Color.WHITE);
        // BoxLayout to display elements from top to bottom
        verifyCodePanel.setLayout(new BoxLayout(verifyCodePanel, BoxLayout.Y_AXIS));
        verifyCodePanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Resize?

        // Add all the created components and vertical padding between them
        verifyCodePanel.add(titleLabel);
        verifyCodePanel.add(Box.createVerticalStrut(10));
        verifyCodePanel.add(instructionsLabel);
        verifyCodePanel.add(Box.createVerticalStrut(20));
        verifyCodePanel.add(inputCode);
        verifyCodePanel.add(Box.createVerticalStrut(40));

        // Panel for the buttons. Need another one because we now want a horizontal
        // linear layout instead of a vertical one
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelButtons.setOpaque(false);

        panelButtons.add(acceptButton);
        panelButtons.add(cancelButton);

        verifyCodePanel.add(panelButtons);
        verifyCodePanel.add(Box.createVerticalStrut(20));

        // Main layout that will contain the panel (and the button panel inside the latter)
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(250, 500, 250, 500)); // TODO: Resize it
        add(verifyCodePanel, BorderLayout.CENTER);
    }

    // If the visibility is changed, focus to text and empty the field content
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            inputCode.requestFocusInWindow();
            inputCode.setText("");
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics.create();

        graphics2D.setColor(new Color(50, 50, 50));
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphics2D.fillRect(0, 0, getWidth(), getHeight());

        graphics2D.dispose();

        super.paintComponent(graphics);
    }


    public String getInputCode() {
        return inputCode.getText().trim();
    }

    public void addEventButtonOK(ActionListener event) {
        acceptButton.addActionListener(event);
    }
}
