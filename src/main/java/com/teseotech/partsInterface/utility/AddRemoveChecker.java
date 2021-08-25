package com.teseotech.partsInterface.utility;

public interface AddRemoveChecker {

    Boolean exists();
    String getCheckerLog();

    default boolean shouldAdd(){
        return shouldAdd(true);
    }
    default boolean shouldAdd(boolean shouldLog){
        Boolean exists = exists();
        if(exists == null)
            return false;
        if(exists) {
            if(shouldLog)
                StaticLogger.logVerbose("WARNING: cannot add an already existing " + getCheckerLog()+ ".");
            return false;
        }
        if(shouldLog)
            StaticLogger.logInfo("adding " + getCheckerLog() + '.');
        return true;
    }

    default boolean shouldRemove(){
        return shouldRemove(true);
    }
    default boolean shouldRemove(boolean shouldLog){
        Boolean exists = exists();
        if(exists == null)
            return false;
        if(!exists) {
            if(shouldLog)
                StaticLogger.logVerbose("WARNING: cannot remove a missing " + getCheckerLog() + ".");
            return false;
        }
        if(shouldLog)
            StaticLogger.logInfo("removing " + getCheckerLog() + '.');
        return true;
    }
}
