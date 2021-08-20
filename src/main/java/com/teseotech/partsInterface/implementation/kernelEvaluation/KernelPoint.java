package com.teseotech.partsInterface.implementation.kernelEvaluation;

import com.teseotech.partsInterface.coreInterface.Feature;
import com.teseotech.partsInterface.coreInterface.Target;
import com.teseotech.partsInterface.implementation.owlInterface.KernelOWL;
import it.emarolab.amor.owlInterface.OWLReferences;

class KernelPointParam{}

public class KernelPoint extends KernelOWL<Number,KernelPointParam> {
    public KernelPoint(Feature<Number> feature, OWLReferences ontology) {
        super(feature, ontology);
    }
    public KernelPoint(String key, Number value, OWLReferences ontology) {
        super(key, value, ontology);
    }

    @Override
    public float evaluate(Target<Number, KernelPointParam> target) {
        //float distance = (getFeature().getValue().floatValue() - target.getValue().floatValue()) / target.getValue().floatValue();
        // todo implement
        //return distance;
        return 0f;
    }
}