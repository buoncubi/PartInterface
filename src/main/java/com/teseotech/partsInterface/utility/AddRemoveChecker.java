package com.teseotech.partsInterface.utility;

/*
 * An interface to add or remove instances from a data structure based on its existence.
 */
public interface AddRemoveChecker {

    Boolean exists();  // Access the data structure to known if a new items should be added or removed.
    String getCheckerLog();  // It should returns a static tag used for logging.


    // Return `true` if data should be added to the structure; `false` otherwise.
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
    // Enable adding logs.
    default boolean shouldAdd(){
        return shouldAdd(true);
    }

    // Return `true` if data should be removed from the structure; `false` otherwise.
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
    // Enable removing logs.
    default boolean shouldRemove(){
        return shouldRemove(true);
    }
}
