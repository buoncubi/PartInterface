package com.teseotech.partsInterface.implementation.partEvaluation.core.utility;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

public class StaticLogger{
    static Logger logger = configure(Level.INFO);

    private StaticLogger(){} // not constructable

    static public void setLogger(Level level){
        logger = configure(level);
    }
    private static Logger configure(Level level) {
        final Logger app = Logger.getLogger("app");
        try {
            /*// Load a properties file from class path that way can't be achieved with java.util.logging.config.file
            final LogManager logManager = LogManager.getLogManager();
            try (final InputStream is = getClass().getResourceAsStream("/logging.properties"))
                logManager.readConfiguration(is);*/

            // Programmatic configuration
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS.%1$tL %4$-7s %5$s %6$s%n");  // ... %4$-7s [%3$s] (%2$s) %5$s %6$s%n");

            final ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(level);
            consoleHandler.setFormatter(new SimpleFormatter());

            app.setLevel(level);
            app.addHandler(consoleHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return app;
    }

    static public void logVerbose(String content){
        logger.log(Level.FINE, content);
    }
    static public void logInfo(String content){
        logger.log(Level.INFO, content);
    }
    static public void logWarning(String content){
        logger.log(Level.WARNING, content);
    }
    static public void logError(String content){
        logger.log(Level.SEVERE, content);
    }
}