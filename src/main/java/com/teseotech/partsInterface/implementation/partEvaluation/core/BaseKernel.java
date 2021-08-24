package com.teseotech.partsInterface.implementation.partEvaluation.core;

import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;

// The interface to define and evaluate features.
// See the definition of `F` and `P` in `Feature` and `Target` respectively.
public abstract class BaseKernel<V,P> extends BaseFeature<V> {
    private final P parameters;
    private final float weight;

    public BaseKernel(String targetKey, V targetValue, P parameter) {
        super(targetKey, targetValue);
        this.parameters = parameter;
        this.weight = 1.0f;
    }
    public BaseKernel(String targetKey, V targetValue, P parameter, float weight) {
        super(targetKey, targetValue);
        this.parameters = parameter;
        this.weight = weight;
    }

    // intended to be used while evaluating `Part.queryAffinity()`.
    abstract public <X extends BaseFeature<?>> Float evaluateChecked(X actual);  // The returning value should be in [0,1]; `null` is given in case of issues.
    public <X extends BaseFeature<?>> Float evaluate(X actual) {  // The returning value should be in [0,1]; `null` is given in case of issues.
        Float evaluation = null;
        if(actual != null) {
            if(this.checkType(actual)) {// Check if the type is consistent.
                if(this.checkKey(actual)) {   // Check if the key is consistent.
                    evaluation = evaluateChecked(actual);
                    StaticLogger.logVerbose("Kernel " + this + " evaluating " + actual + " with target " + actual + "(=" + evaluation +").");
                } else StaticLogger.logError("I cannot evaluate different `keys`, i.e., " + this + " != " + actual + '.');
            } else StaticLogger.logError("Cannot evaluate features that are not of the same type (" + actual.getType() + "!=" + getType() + '.');
        } else StaticLogger.logError("I cannot evaluate kernel since actual value is not found (i.e., `null`).");
        return evaluation;
    }
    protected <X extends BaseFeature<?>> boolean checkKey(X actual){
        return this.getKey().equals(actual.getKey());
    }
    protected <X extends BaseFeature<?>> boolean checkType(X actual){
        return actual.getType() == this.getType();
    }

    public P getParameters() {
        return parameters;
    }
    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(w:" + weight + ")"; // + parameters;
    }
    public String toDescription() {
        return this.getClass().getSimpleName() + "(w:" + weight + ", v:" + getValue() + ", k:" + getKey() + ", p:" + parameters + ')';
    }
}
