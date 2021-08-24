package com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel;

import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseKernel;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;

import java.util.Objects;

/*  RANGE SPECIFICATIONS:
        if actual.min <= target.min  &  actual.max >= target.max
                -> [OVERLAPS]     100%
        if actual.max <  target.min  |  actual.min > target.max
                -> [OUTSIDE]      0%
        if actual.min <= target.min  &  target.min < actual.max < target.max
                -> [OVERLAPS_MIN] (actual.max-target.min)/(target.max-target.min)
        if actual.max >= target.max  &  target.min < actual.min < target.max
                -> [OVERLAPS_MAX] (target.max-actual.min)/(target.max-target.min)
        if actual.min >  target.min  &  actual.max < target.max
                -> [WITHIN]       (actual.max-actual.min)/(target.max-target.min)
*/

public class KernelRange  extends BaseKernel<Range, Void> {
    public KernelRange(String targetKey, Range targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelRange(String targetKey, Range targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        Range actualValue = castRange(actual.getValue());
        Range targetValue = this.getValue();
        switch (targetValue.checkOverlaps(actualValue)){
            case WITHIN:
                return (actualValue.getMax() - actualValue.getMin()) / (targetValue.getMax() - targetValue.getMin());
            case OVERLAPS:
                return 1.0f;
            case OVERLAPS_MIN:
                return (actualValue.getMax() - targetValue.getMin()) / (targetValue.getMax() - targetValue.getMin());
            case OVERLAPS_MAX:
                return (targetValue.getMax() - actualValue.getMin()) / (targetValue.getMax() - targetValue.getMin());
            case OUTSIDE:
                return 0.0f;
        }
        return null;
    }

    private Range castRange(Object r){
        if(r instanceof Range)
            return (Range) r;
        if(r instanceof Number) {
            Number n = (Number) r;
            return new Range(n,n);
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
