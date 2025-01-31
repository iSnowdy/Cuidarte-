package Authentication.Main;

import Authentication.Swing.PanelCover;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private javax.swing.JLayeredPane backGround;

    private MigLayout layout;
    private PanelCover cover;

    private final int addSize = 30;
    private final int coverSize = 40;


    public Main() {
        setTitle("Authentication Login / Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 1000); // TODO: Check appropriate sizes

        initComponents();
        start();
    }

    private void start() {
        // Layout Constraints | Column Constraints
        this.layout = new MigLayout("fill, insets 0");
        this.cover = new PanelCover();

        this.backGround.setLayout(layout);
        this.backGround.add(cover, "width " + coverSize + "%, pos 0al 0 n 100%");
        this.cover.addEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked");
            }
        });
    }

    private void initComponents() {
        this.backGround = new JLayeredPane();
        this.backGround.setLayout(new BorderLayout());
        this.backGround.setBackground(Color.WHITE);
        this.backGround.setOpaque(true);

        this.add(backGround, BorderLayout.CENTER);
    }


    public static void main(String[] args) {


        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}