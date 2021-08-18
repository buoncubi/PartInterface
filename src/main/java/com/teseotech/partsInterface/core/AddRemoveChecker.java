package com.teseotech.partsInterface.core;

public interface AddRemoveChecker {

    boolean exists();
    //todo @Deprecated  // set as deprecate to simulate `getCheckerLog` as private but used in this interface
    String getCheckerLog();

    default boolean shouldAdd(){
        return shouldAdd(true);
    }
    default boolean shouldAdd(boolean shouldLog){
        if(this.exists()) {
            if(shouldLog)
                System.out.println("WARNING: cannot add an already existing " + getCheckerLog()+ ".");
            return false;
        }
        if(shouldLog)
            System.out.println("INFO: adding " + getCheckerLog() + '.');  // todo use logging system
        return true;
    }

    default boolean shouldRemove(){
        return shouldRemove(true);
    }
    default boolean shouldRemove(boolean shouldLog){
        if(!this.exists()) {
            if(shouldLog)
                System.out.println("WARNING: cannot remove a missing " + getCheckerLog() + ".");
            return false;
        }
        if(shouldLog)
            System.out.println("INFO: removing " + getCheckerLog() + '.');
        return true;
    }
}
