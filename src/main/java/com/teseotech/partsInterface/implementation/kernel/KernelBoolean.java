package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.utility.StaticLogger;

/*
 * It extends `BaseKernel` to implement a Kernel comparing two Features with a `Boolean` Datatype.
 * It does not requires any parameters, and evaluates the comparison as 1 if the two Features
 * have an equal value; 0 otherwise.
 * Remarkably, it can also evaluate `Number' number by considering them `true` if greater than zero.
 */
public class KernelBoolean extends BaseKernel<Boolean, Void> {
    public KernelBoolean(String targetKey, boolean targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelBoolean(String targetKey, boolean targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // `this` is the target, `actual` is the value of Part Feature the ontology.
        Boolean actualValue = castBoolean(actual.getValue());
        if(actualValue == getValue())  // If the target is equal to the actual value.
            return 1f;
        else return 0f;
    }

    private Boolean castBoolean(Object r){  // It is used to represent `Integer` as `Boolean`
        if(r instanceof Boolean)
            return (Boolean) r;
        if(r instanceof Number) {
            Number n = (Number) r;
            return n.floatValue() > 0;
        }
        StaticLogger.logError("Cannot cast in `Range` object of type: " + r.getClass());
        return null;
    }

    @Override
    protected <X extends BaseFeature<?>> boolean checkType(X actual) {
        // By default it accepts `Boolean` Datatype to be passed to `evaluateChecked()`.
        boolean sameType = super.checkType(actual);
        // Accept also `Number` Datatype to be passed to `evaluateChecked()`.
        boolean number = actual.getValue() instanceof Number;
        return sameType | number;
    }
}
