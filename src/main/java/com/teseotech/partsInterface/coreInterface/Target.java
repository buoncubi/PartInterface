package com.teseotech.partsInterface.coreInterface;

// The desired `Feature` evaluated by a specific `Kernel`.
// `F` is the desired value, while `P` defines the parameters to evaluate the `Kernel`.
public class Target<F,P>{
    private final Feature<F> targetValue;
    private final P parameters;

    public Target(Feature<F> targetValue, P parameters){
        this.targetValue = targetValue;
        this.parameters = parameters;
    }
    public Target(String key, F targetValue, P parameters){
        this.targetValue = new Feature<>(key, targetValue);
        this.parameters = parameters;
    }

    public Feature<F> getTargetValue() {
        return targetValue;
    }

    public P getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Target(" + targetValue + ", params=" + parameters + ')';
    }
}
