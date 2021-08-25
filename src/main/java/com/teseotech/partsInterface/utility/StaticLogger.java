package com.teseotech.partsInterface.utility;

import java.util.logging.*;

public class StaticLogger{
    private static Logger logger = null;

    private StaticLogger(){} // StaticLogger is not constructable

    static public void setLogger(Level level){
        logger = configure(level);
    }
    private static Logger configure(Level level) {
        final Logger logger = Logger.getLogger("");
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

            logger.setLevel(level);
            for(Handler h: Logger.getLogger("").getHandlers())
                logger.removeHandler(h);  // clear all header to print only once (this is a rough simplification).
            logger.addHandler(consoleHandler);
        } catch (Exception e) {
            e.printStackTrace();
            return StaticLogger.logger;
        }
        return logger;
    }

    static public void log(Level level, String content){
        if(logger != null)
            logger.log(level, content);
        else System.out.println( level.getName() + ":\t" + content);
    }
    static public void logVerbose(String content){
        log(Level.FINE, content);
    }
    static public void logInfo(String content){
        log(Level.INFO, content);
    }
    static public void logWarning(String content){
        log(Level.WARNING, content);
    }
    static public void logError(String content){
        log(Level.SEVERE, content);
    }
}