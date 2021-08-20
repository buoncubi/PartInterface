package com.teseotech.partsInterface.implementation.kernelEvaluation;

import com.teseotech.partsInterface.coreInterface.Feature;
import com.teseotech.partsInterface.coreInterface.Target;
import com.teseotech.partsInterface.implementation.owlInterface.KernelOWL;
import it.emarolab.amor.owlInterface.OWLReferences;

public class KernelString extends KernelOWL<String, Void> {
    public KernelString(Feature<String> feature, OWLReferences ontology) {
        super(feature, ontology);
    }
    public KernelString(String key, String value, OWLReferences ontology) {
        super(key, value, ontology);
    }

    @Override
    public float evaluate(Target<String, Void> target) {
        //float distance = (getFeature().getValue().floatValue() - target.getValue().floatValue()) / target.getValue().floatValue();
        // todo implement
        //return distance;
        return 0f;
    }
}