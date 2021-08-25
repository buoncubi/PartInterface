package com.teseotech.partsInterface.utility;

import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticLogger{
    // StaticLogger is not constructable.
    private StaticLogger(){}

    // Constants.
    public static final int VERBOSE = 0;
    public static final int INFO = 1;
    public static final int WARNING = 2;
    public static final int ERROR = 3;

    // Initialisation and Configurations
    private static int actualLevel = WARNING;
    public static void setLevel(int level){
        actualLevel = level;
    }
    /*public static void setGlobalLevel(int logging) {  // Attempt to get logs from dependencies.
        StaticLogger.setLevel(logging);  // SEVERE (i.e., error), WARNING, INFO, FINE (i.e., verbose debug).
        Logger logger = Logger.getLogger("");
        switch (logging){
            case StaticLogger.VERBOSE:  logger.setLevel(Level.FINE);    break;
            case StaticLogger.INFO:     logger.setLevel(Level.INFO);    break;
            case StaticLogger.WARNING:  logger.setLevel(Level.WARNING); break;
            case StaticLogger.ERROR:    logger.setLevel(Level.SEVERE);  break;
        }
    }*/

    // Logging Methods.
    static public void logVerbose(String content){
        log(VERBOSE, content);
    }
    static public void logInfo(String content){
        log(INFO, content);
    }
    static public void logWarning(String content){
        log(WARNING, content);
    }
    static public void logError(String content){
        log(ERROR, content);
    }


    // Utilities.
    static private void log(int level, String content){
        if(level >= actualLevel)
            System.out.println(now() + level2str(level) + content);
    }
    static private String now(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.S");
        return sdf.format(System.currentTimeMillis());
    }
    static private String level2str(int level){
        switch (level){
            case VERBOSE: return "\tVERBOSE:  ";
            case INFO:    return "\t   INFO:  ";
            case WARNING: return "\tWARNING:  ";
            case ERROR:   return "\t  ERROR:  ";
            default:      return "\t    ???:  ";
        }
    }
}