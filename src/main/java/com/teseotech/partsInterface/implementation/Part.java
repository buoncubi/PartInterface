package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.utility.StaticLogger;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.core.BaseKernel;
import it.emarolab.amor.owlInterface.OWLReferences;

import java.util.Set;

/*
 * It extends `OWLPart` and defined the `evaluateAffinity()` method with a
 * weighted average among the evaluation of `this` Features with the relative `target` Kernels.
 */
public class Part extends OWLPart {
    public Part(String identifier, String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(identifier, partType, features, ontology);
    }
    public Part(String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(partType, features, ontology);
    }

    @Override
    public Affinity evaluateAffinity(Set<BaseKernel<?,?>> targets) {
        int cnt = 0;
        float sum = 0;
        for (BaseKernel<?,?> k: targets) {
            // Search for actual value that match the `key` of a target Kernel.
            OWLFeature<?> found = findKernel(getFeatures(), k);
            // compute weighted average among `key` pair between actual and target values.
            if(found != null) {
                Float eval = k.evaluate(found);
                if(eval != null) {
                    sum += k.getWeight() * eval;
                    cnt += k.getWeight();
                }
            }else StaticLogger.logWarning("Target " + k.toDescription() + " not found in " + getFeatures() + '.');
        }
        float weightedAvg = sum/cnt;
        return new Affinity(weightedAvg, this.getID());
    }

    static public OWLFeature<?> findKernel(Set<? extends OWLFeature<?>> features, BaseKernel<?, ?> k){
        for (OWLFeature<?> f: features)
            if(k.getKey().equals(f.getKey()))
                return f;
        return null;
    }
}
