package Utils.Utility;

import javax.swing.*;
import java.awt.*;

// TODO: I'm using this class A LOT. Maybe consider making the method static

public class ImageIconRedrawer {
    ImageIcon imageIcon;

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImageIcon redrawImageIcon(int width, int height) {
        Image currentImage = imageIcon.getImage();
        Image scaledImage = currentImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        this.imageIcon = null; // Clears it for future implementations

        return scaledImageIcon;
    }
}
