package com.teseotech.partsInterface.coreInterface;

import com.teseotech.partsInterface.utility.AddRemoveChecker;
import com.teseotech.partsInterface.utility.LoggerInterface;

import java.util.Set;
import java.util.List;

// the interface to add instances and query affinity.
public abstract class PartInterface implements AddRemoveChecker {
    private final Set<KernelInterface<?,?>> kernels;
    private final String identifier;

    public PartInterface(String identifier, Set<KernelInterface<?,?>> kernels){  // remember to set Instance name on super constructor!
        if(identifier != null)
            this.identifier = identifier;
        else this.identifier = PartInterface.createIdentifier();
        this.kernels = kernels;
    }

    // Intended to change the TBox.
    public abstract void addPart();
    public abstract void removePart();

    // intended to change the ABox.
    public abstract void addInstance();
    public abstract void removeInstance();

    // It queries the ontology and rank the affinity on the basis of `Kernel.evaluate()`.
    public abstract List<Affinity> queryAffinity(Set<Target<?,?>> targets);

    public String getID(){
        return identifier;
    }

    protected Set<KernelInterface<?,?>> getKernels() {
        return this.kernels;
    }

    @Override
    public String getCheckerLog() {
        return "part " + this;
    }
    @Override
    public String toString() {
        return  getID()  + "->" + kernels;
    }

    // Generate Part identifier based on timestamp.
    private static int idCnt = 0;
    private static long lastId = System.currentTimeMillis();
    static String createIdentifier(){
        long now = System.currentTimeMillis();
        String id;
        if(now != lastId){
            id = now + "";
            idCnt = 0;
        } else {
            try {
                id = now + "-" + (++idCnt);
            } catch (Exception e) {
                LoggerInterface.logError(PartInterface.class, "Cannot generate part ID!");
                id = "";
            }

        }
        lastId = now;
        return id;
    }
}
