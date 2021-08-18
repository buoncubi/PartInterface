package com.teseotech.partsInterface.core;

import java.util.*;

// the interface to add instances and query affinity.
public abstract class Part implements AddRemoveChecker {
    private final String typeName;
    private final Set<Kernel> kernels;
    private String instanceID = null;

    public Part(String typeName, Set<Kernel> kernels){  // remember to set Instance name on super constructor!
        this.typeName = typeName;
        this.kernels = kernels;
    }

    public boolean initialised() {
        if (getInstanceID() == null) {
            System.out.println("ERROR: set the instance name (and related representation) for " + this);
            return false;
        }
        return true;
    }

    // intended to change the ABox.
    public abstract void addInstance();
    public abstract void removeInstance();

    // it is based on `Kernel.evaluate()`.
    public abstract List<?> queryAffinity(Set<Kernel> targets);  // TODO remove varargs? elsewhere

    protected void setInstanceID(String instanceID){
        this.instanceID = instanceID;
    }
    public String getInstanceID(){
        if(initialised())
            return this.instanceID;
        else return null;
    }

    protected String getTypeName() {
        return this.typeName;
    }

    protected Set<Kernel> getKernels() {
        return this.kernels;
    }

    @Override
    public String getCheckerLog() {
        return "part " + this;
    }
    @Override
    public String toString() {
        return  getInstanceID() + ':' + getTypeName() + "->" + kernels;
    }
}
