import Utils.Utility.CustomLogger;

import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = CustomLogger.getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.info("Application started successfully.");
        LOGGER.warning("This is a warning message.");
        LOGGER.severe("This is a critical error!");
    }
}
