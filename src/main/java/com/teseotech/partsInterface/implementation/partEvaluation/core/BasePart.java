package com.teseotech.partsInterface.implementation.partEvaluation.core;

import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.AddRemoveChecker;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;

import java.util.Set;

// the interface to add instances and query affinity.
public abstract class BasePart<F extends BaseFeature<?>> implements AddRemoveChecker {
    private final Set<? extends F> features;
    private final String identifier;

    public BasePart(String identifier, Set<? extends F> features){  // remember to set Instance name on super constructor!
        if(identifier != null)
            this.identifier = identifier;
        else this.identifier = BasePart.createIdentifier();
        this.features = features;
    }

    // Intended to change the TBox.
    public abstract void addPart();
    public abstract void removePart();

    // intended to change the ABox.
    public abstract void addInstance();
    public abstract void removeInstance();

    // It queries the ontology and rank the affinity on the basis of `Kernel.evaluate()`.
    public abstract Affinity queryAffinity(Set<BaseKernel<?,?>> targets);

    public String getID(){
        return identifier;
    }

    protected Set<? extends F> getFeatures() {
        return this.features;
    }

    @Override
    public String getCheckerLog() {
        return "part " + this;
    }
    @Override
    public String toString() {
        return  getID()  + "->" + features;
    }

    // Generate Part identifier based on timestamp.
    private static int idCnt = 0;
    private static long lastId = System.currentTimeMillis();
    public static String createIdentifier(){
        long now = System.currentTimeMillis();
        String id;
        if(now != lastId){
            idCnt = 0;
            id = now + "-" + idCnt;
        } else {
            try {
                id = now + "-" + (++idCnt);
            } catch (Exception e) {
                StaticLogger.logError("Cannot generate part ID!");
                id = "";
                idCnt = 0;
            }

        }
        lastId = now;
        return id;
    }
}
