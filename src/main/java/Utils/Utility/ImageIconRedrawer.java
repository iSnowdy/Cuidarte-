package Utils.Utility;

import javax.swing.*;
import java.awt.*;

public class ImageIconRedrawer {
    ImageIcon imageIcon;

    public void setImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImageIcon redrawImageIcon(int width, int height) {
        System.out.println("Scaled image");
        Image currentImage = imageIcon.getImage();
        Image scaledImage = currentImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledImageIcon = new ImageIcon(scaledImage);
        System.out.println("Sizes are: " + scaledImageIcon.getIconWidth() + " : " + scaledImageIcon.getIconHeight());
        return scaledImageIcon;
    }
}
