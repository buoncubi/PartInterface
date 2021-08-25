package com.teseotech.partsInterface.utility;

import java.text.SimpleDateFormat;

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
            case VERBOSE: return " VERBOSE:  ";
            case INFO:    return "    INFO:  ";
            case WARNING: return " WARNING:  ";
            case ERROR:   return "   ERROR:  ";
            default:      return "     ???:  ";
        }
    }
}