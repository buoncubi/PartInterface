package com.teseotech.partsInterface.core;

import com.teseotech.partsInterface.utility.StaticLogger;

/*
 * It is the class that represents a target `BaseFeature<V>`, and it defines the `evalute()`
 * function to compare the target with another `Feature<?>` instance, i.e., the actual value.
 * it defines a possible parameter of the generic type `P`, which can be used during evaluation.
 * Also, it encodes a `weight` that will be used by `Part.evaluateAffinity()`, which by default is set to 1.
 */
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

    // The returning value should be in [0,1]; `null` is returned in case of issues. It is invoked by `evaluate()`
    abstract protected  <X extends BaseFeature<?>> Float evaluateChecked(X actual);
    // If the Datatypes of the `actual` Feature is consistent with `this` Kernel, It returns the value computed by `evaluateChecked()`.
    public <X extends BaseFeature<?>> Float evaluate(X actual) {
        Float evaluation = null;
        if(actual != null) {
            if(this.checkType(actual)) {  // Check if the type is consistent.
                if(this.checkKey(actual)) {  // Check if the key is consistent.
                    evaluation = evaluateChecked(actual);
                    if(evaluation < 0 | evaluation > 1)
                        StaticLogger.logWarning("Kernel evaluation of " + this + "is out of [0,1] bounds!");
                    StaticLogger.logVerbose("Kernel " + this + " evaluating " + actual + " with target " + actual + "(=" + evaluation +").");
                } else StaticLogger.logError("I cannot evaluate different `keys`, i.e., " + this + " != " + actual + '.');
            } else StaticLogger.logError("Cannot evaluate features with ket `" + getKey() + "` since have inconsistent types (i..e, "
                    + actual.getType().getSimpleName() + " and " + getType().getSimpleName() + ").");
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
        return this.getClass().getSimpleName() + "(w:" + weight + ")";
    }
    public String toDescription() {  // It returns a more complete representation of `this` object than `toString()`.
        String paramLog = "";
        if(parameters != null)
            paramLog = ", p:" + parameters;
        return this.getClass().getSimpleName() + "(w:" + weight + ", <" + getKey() + ':' + getValue() + '>' + paramLog + ')';
    }
}
