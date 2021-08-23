package com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel;

import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseKernel;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;

public class KernelString extends BaseKernel<String, Void> {
    public KernelString(String targetKey, String targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelString(String targetKey, String targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluate(X actual) {
        // `this` is the target, `actual` is the value from the ontology.
        if(actual.getValue() instanceof String) {  // Check if the type is consistent.
            if(this.checkFeaturesKey(actual)) {   // Check if the key is consistent.
                String actualValue = ((String) actual.getValue()).trim();
                if(actualValue.equalsIgnoreCase(getValue().trim()))  // If the target is equal to the actual value.
                    return 1f;
                else return 0f;
            }
        } else StaticLogger.logError("Cannot evaluate features that are not `String`; " + actual.getType() + " given instead.");
        return null;
    }
}
