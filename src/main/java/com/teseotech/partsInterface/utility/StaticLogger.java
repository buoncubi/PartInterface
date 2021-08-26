package com.teseotech.partsInterface.utility;

import it.emarolab.owloop.core.Axiom;

import java.text.SimpleDateFormat;

/*
 * A static class to manage logging (and logging levels) in a simple manner.
 */
public class StaticLogger{
    // StaticLogger is not constructable.
    private StaticLogger(){}
    static {
        activateOWLOOPLogger(false);
    }

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

    static public void activateOWLOOPLogger(boolean activate){
        Axiom.Descriptor.OntologyReference.activateAMORlogging(activate); // Disabling OWLOOP and aMOE logs.
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