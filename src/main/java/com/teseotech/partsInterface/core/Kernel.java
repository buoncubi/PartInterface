package com.teseotech.partsInterface.core;

// the interface to define and evaluate features.
public abstract class Kernel implements AddRemoveChecker {
    private final Feature<?> feature;

    public Kernel(Feature<?> feature){
        this.feature = feature;
    }
    public <T> Kernel(String key, T value){
        this.feature = new Feature<>(key, value);
    }

    // intended to change the TBox.
    abstract public void addFeature();
    abstract public void removeFeature();

    // intended to be used by `Part.queryAffinity()`.
    abstract public float evaluate(Object... targetParams);

    public Feature<?> getFeature() {
        return this.feature;
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
