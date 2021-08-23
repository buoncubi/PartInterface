package com.teseotech.partsInterface.implementation.partEvaluation.impl.affinity;

import com.teseotech.partsInterface.implementation.partEvaluation.core.Affinity;
import com.teseotech.partsInterface.implementation.partEvaluation.core.Kernel;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;
import com.teseotech.partsInterface.implementation.partEvaluation.impl.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.impl.owlInterface.OWLPart;
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
    public Affinity queryAffinity(Set<Kernel<?,?>> targets) {
        int cnt = 0;
        float sum = 0;
        for (Kernel<?,?> k: targets) {

            // search for actual value
            OWLFeature<?> found = null;
            for (OWLFeature<?> f : getFeatures()) {
                if(k.getKey().equals(f.getKey())){
                    found = f;
                    break;
                }
            }

            // compute weighted average
            if(found != null) {
                sum += k.getWeight() * k.evaluate(found);
                cnt += k.getWeight();
            }else StaticLogger.logWarning("Target " + k + " not found in " + getFeatures() + '.');
        }
        float weightedAvg = sum/cnt;
        return new Affinity(weightedAvg, this.getID());
    }
}
