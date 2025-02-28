package Utils.Utility;

import javax.swing.*;
import java.io.File;

public class DirectorySelector {

    // TODO: Implement this
    // Opens a file chooser dialog to select a directory
    // Returns the selected folder path or null if canceled
    public static String selectDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar carpeta de destino");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only allow folder selection
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null); // Show dialog

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = fileChooser.getSelectedFile();
            return selectedDirectory.getAbsolutePath(); // Return selected directory path
        }

        return null; // Return null if user cancels
    }
}
