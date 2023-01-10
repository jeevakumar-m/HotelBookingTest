package com.payconiq.cucumber.util.Logger;

import java.io.IOException;
import java.time.Instant;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerFile {
    static Logger logger = null;

    static {
        FileHandler fh;
        try {
            System.setProperty(
                    "java.util.logging.SimpleFormatter.format",
                    "[%1$tF %1$tT] [%4$-7s] %5$s %n");
            logger = Logger.getLogger(LoggerFile.class.getName());
            fh = new FileHandler("Reports/Logs/RUN_" + Instant.now().toString().trim().replace(":", "_").replace(".", "_") + ".log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        logger.info(message);
    }
}
