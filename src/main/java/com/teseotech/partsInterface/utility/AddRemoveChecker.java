package com.teseotech.partsInterface.utility;

public interface AddRemoveChecker  extends LoggerInterface{

    boolean exists();
    String getCheckerLog();

    default boolean shouldAdd(){
        return shouldAdd(true);
    }
    default boolean shouldAdd(boolean shouldLog){
        if(this.exists()) {
            if(shouldLog)
                logVerbose("WARNING: cannot add an already existing " + getCheckerLog()+ ".");
            return false;
        }
        if(shouldLog)
            logVerbose("adding " + getCheckerLog() + '.');
        return true;
    }

    default boolean shouldRemove(){
        return shouldRemove(true);
    }
    default boolean shouldRemove(boolean shouldLog){
        if(!this.exists()) {
            if(shouldLog)
                logVerbose("WARNING: cannot remove a missing " + getCheckerLog() + ".");
            return false;
        }
        if(shouldLog)
            logVerbose("removing " + getCheckerLog() + '.');
        return true;
    }
}
