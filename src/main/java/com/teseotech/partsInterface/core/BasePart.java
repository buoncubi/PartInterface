package com.teseotech.partsInterface.core;

import com.teseotech.partsInterface.utility.AddRemoveChecker;
import com.teseotech.partsInterface.utility.StaticLogger;

import java.util.List;
import java.util.Set;

/*
 * The interface to represent a Part as made by some `F extends BaseFeature<?>`.
 * It defines abstract function to add (and remove) to (from) the ontology Part instance `I` (in the ABox),
 * as well as property `r` (in the Tbox).
 * This class implements the `AddRemoveChecker`, which simplify the operation involved for manipulating the ontology.
 * In addition, it define the function to evaluate the affinity of `this` Part based on some target Kernels.
 */
public abstract class BasePart<F extends BaseFeature<?>> implements AddRemoveChecker {
    private final Set<? extends F> features;
    private final String identifier;

    public BasePart(String identifier, Set<? extends F> features){
        if(identifier != null)
            this.identifier = identifier;
        else this.identifier = BasePart.createIdentifier();
        this.features = features;
    }

    // Intended to change the TBox, i.e, add/remove generic logic definition of a Part to/from the ontology.
    public abstract void addPart();
    public abstract void removePart();

    // Intended to change the ABox, i.e., add/remove Part Instances `I` from/to the ontology.
    public abstract void addInstance();
    public abstract void removeInstance();

    // It queries the ontology and rank the affinity on the basis of `Kernel.evaluate()`.
    public abstract Affinity evaluateAffinity(Set<BaseKernel<?,?>> targets);

    public String getID(){
        return identifier;
    }
    protected Set<? extends F> getFeatures() {
        return this.features;
    }

    @Override
    public String getCheckerLog() {
        // It defines a string used to log information while checking if a Part should be added/removed from/to the ontology.
        return "part " + this;
    }

    @Override
    public String toString() {
        return  getID()  + "->" + features;
    }

    // Sort Affinities based on their value in ascended order.
    public static void sortAffinities(List<Affinity> affinities){
        affinities.sort((o1, o2) -> Float.compare(o1.getDegree(), (o2.getDegree())));
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
