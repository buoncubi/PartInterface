package com.teseotech.partsInterface.utility;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

public interface LoggerInterface {

    default Logger getLogger(){
         return LoggerInterface.getLogger(this.getClass());
    }

    default void logVerbose(String content){
        LoggerInterface.logVerbose(this.getClass(), content);
    }
    default void logInfo(String content){
        LoggerInterface.logInfo(this.getClass(), content);
    }
    default void logWarning(String content){
        LoggerInterface.logWarning(this.getClass(), content);
    }
    default void logError(String content){
        LoggerInterface.logError(this.getClass(), content);
    }


    static Logger getLogger(Class<?> producer){
        return Logger.getLogger(producer.getSimpleName());
    }

    static void logVerbose(Class<?> producer, String content){
        LoggerInterface.getLogger(producer).log(Level.FINE, content);
    }
    static void logInfo(Class<?> producer, String content){
        LoggerInterface.getLogger(producer).log(Level.INFO, content);
    }
    static void logWarning(Class<?> producer, String content){
        LoggerInterface.getLogger(producer).log(Level.WARNING, content);
    }
    static void logError(Class<?> producer, String content){
        LoggerInterface.getLogger(producer).log(Level.SEVERE, content);
    }

    static void configure(Level level) {
        try {
            /*// Load a properties file from class path that way can't be achieved with java.util.logging.config.file
            final LogManager logManager = LogManager.getLogManager();
            try (final InputStream is = getClass().getResourceAsStream("/logging.properties"))
                logManager.readConfiguration(is);*/

            // Programmatic configuration
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s [%3$s] %5$s %6$s%n");  // ... [%3$s] (%2$s) %5$s %6$s%n");

            final ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(level);
            consoleHandler.setFormatter(new SimpleFormatter());

            final Logger app = Logger.getLogger("app");
            app.setLevel(Level.FINEST);
            app.addHandler(consoleHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
