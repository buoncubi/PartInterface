package com.teseotech.partsInterface.coreInterface;

import com.teseotech.partsInterface.utility.AddRemoveChecker;

// The interface to define and evaluate features.
// See the definition of `F` and `P` in `Feature` and `Target` respectively.
public abstract class KernelInterface<F,P> implements AddRemoveChecker {
    private final Feature<F> feature;

    public KernelInterface(Feature<F> feature){
        this.feature = feature;
    }
    public KernelInterface(String key, F value){
        this.feature = new Feature<>(key, value);
    }

    // intended to change the TBox to define a generic feature with a given `key` and datatype derived from a `value`.
    abstract public void addFeature();
    abstract public void removeFeature();

    // intended to be used while evaluating `Part.queryAffinity()`.
    abstract public float evaluate(Target<F,P> target);

    public Feature<F> getFeature() {
        return this.feature;
    }
    public F getFeatureValue(){
        return this.getFeature().getValue();
    }
    public Class getFeatureType(){
        return this.getFeatureValue().getClass();
    }
    public String getFeatureKey(){
        return this.getFeature().getKey();
    }


    @Override
    public String getCheckerLog() {
        return "feature " +  getFeature().toDescription();
    }
    @Override
    public String toString() {
        return  getFeature().toDescription() + '@' + this.getClass().getSimpleName();
    }
}
