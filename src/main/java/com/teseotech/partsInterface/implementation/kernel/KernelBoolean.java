package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.utility.StaticLogger;

public class KernelBoolean extends BaseKernel<Boolean, Void> {
    public KernelBoolean(String targetKey, boolean targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelBoolean(String targetKey, boolean targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // `this` is the target, `actual` is the value from the ontology.
        Boolean actualValue = castBoolean(actual.getValue());
        if(actualValue == getValue())  // If the target is equal to the actual value.
            return 1f;
        else return 0f;
    }

    private Boolean castBoolean(Object r){
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
        boolean sameType = super.checkType(actual);
        boolean number = actual.getValue() instanceof Number;
        return sameType | number;
    }
}
