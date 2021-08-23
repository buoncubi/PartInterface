package com.teseotech.partsInterface.implementation.partEvaluation.implementation;

import com.teseotech.partsInterface.implementation.partEvaluation.core.Affinity;
import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseKernel;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPart;
import it.emarolab.amor.owlInterface.OWLReferences;

import java.util.Set;

public class Part extends OWLPart {
    public Part(String identifier, String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(identifier, partType, features, ontology);
    }
    public Part(String partType, Set<OWLFeature<?>> features, OWLReferences ontology) {
        super(partType, features, ontology);
    }

    @Override
    public Affinity queryAffinity(Set<BaseKernel<?,?>> targets) {
        int cnt = 0;
        float sum = 0;
        for (BaseKernel<?,?> k: targets) {
            // Search for actual value that match the `key` of a target Kernel.
            OWLFeature<?> found = findKernel(getFeatures(), k);
            // compute weighted average among `key` pair between actual and target values.
            if(found != null) {
                Float eval = k.evaluate(found);
                if(eval != null) {
                    sum += k.getWeight() * k.evaluate(found);
                    cnt += k.getWeight();
                }
            }else StaticLogger.logWarning("Target " + k + " not found in " + getFeatures() + '.');
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