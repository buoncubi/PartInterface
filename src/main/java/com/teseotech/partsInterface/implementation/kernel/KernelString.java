package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseFeature;
import com.teseotech.partsInterface.core.BaseKernel;

/*
 * It extends `BaseKernel` to implement a Kernel comparing two Features with a `String` Datatype.
 * It does not requires any parameters, and evaluates the comparison as 1 if the two Features
 * have an equal value; 0 otherwise.
 */
public class KernelString extends BaseKernel<String, Void> {
    public KernelString(String targetKey, String targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelString(String targetKey, String targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // `this` is the target, `actual` is the value from the ontology.
        String actualValue = ((String) actual.getValue()).trim();
        if(actualValue.equalsIgnoreCase(getValue().trim()))  // If the target is equal to the actual value.
            return 1f;
        else return 0f;
    }
}
