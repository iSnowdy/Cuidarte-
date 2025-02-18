package Utils.Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class CustomLogger {
    private static final String LOG_DIRECTORY = "Logs/";

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false); // Prevents double logging, meaning printing in console

        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY));
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String logFileName = LOG_DIRECTORY + date + ".log";

            FileHandler fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%s] [%s]: %s%n",
                            record.getLevel(),
                            record.getSourceClassName(),
                            record.getMessage());
                }
            });

            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }
}
