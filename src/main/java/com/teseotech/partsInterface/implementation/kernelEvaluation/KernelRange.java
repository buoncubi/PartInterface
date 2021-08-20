package com.teseotech.partsInterface.implementation.kernelEvaluation;

import com.teseotech.partsInterface.coreInterface.Feature;
import com.teseotech.partsInterface.coreInterface.Target;
import com.teseotech.partsInterface.implementation.owlInterface.KernelOWL;
import it.emarolab.amor.owlInterface.OWLReferences;

class KernelRangeParam{}

public class KernelRange extends KernelOWL<Number,KernelRangeParam> {
    public KernelRange(Feature<Number> feature, OWLReferences ontology) {
        super(feature, ontology);
    }
    public KernelRange(String key, Number value, OWLReferences ontology) {
        super(key, value, ontology);
    }

    @Override
    public float evaluate(Target<Number, KernelRangeParam> target) {
        //float distance = (getFeature().getValue().floatValue() - target.getValue().floatValue()) / target.getValue().floatValue();
        // todo implement
        //return distance;
        return 0f;
    }
}