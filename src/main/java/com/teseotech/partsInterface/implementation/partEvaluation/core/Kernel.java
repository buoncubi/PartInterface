package com.teseotech.partsInterface.implementation.partEvaluation.core;

// The interface to define and evaluate features.
// See the definition of `F` and `P` in `Feature` and `Target` respectively.
public abstract class Kernel<F,P> extends BaseFeature<F> {
    private final P parameters;
    private final float weight;

    public Kernel(String key, F target, P parameter) {
        super(key, target);
        this.parameters = parameter;
        this.weight = 1.0f;
    }
    public Kernel(String key, F target, P parameter, float weight) {
        super(key, target);
        this.parameters = parameter;
        this.weight = weight;
    }

    // intended to be used while evaluating `Part.queryAffinity()`.
    abstract public <X extends BaseFeature<?>> float evaluate(X actual);

    public P getParameters() {
        return parameters;
    }
    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + weight + ")(" + parameters + ')';
    }
}
