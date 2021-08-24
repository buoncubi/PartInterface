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

enum RangeEval{WITHIN, OVERLAPS, OVERLAPS_MIN, OVERLAPS_MAX, OUTSIDE, UNKNOWN}

class Range{
    private Number max;
    private Number min;

    public Range(Number min, Number max){
        this.min = min;
        this.max = max;
        if(min.floatValue() > max.floatValue()) {
            StaticLogger.logWarning("Range cannot have `min > max` values, swapping values!");
            Number tmp = this.min;
            this.min = this.max;
            this.max = tmp;
        }
    }

    public Number getMaxNumber() {
        return max;
    }
    public Number getMinNumber() {
        return min;
    }
    public float getMax() {
        return getMaxNumber().floatValue();
    }
    public float getMin() {
        return getMinNumber().floatValue();
    }

    public RangeEval checkOverlaps(Range actual){
        // The target range (i.e., `this`) is shown graphically is the comments below as '|' where `min` is before `max`
        float targetMin = this.getMin();
        float targetMax = this.getMax();
        // The actual rage (i.e., to test against target) is shown graphically as '------' where `min` and `max` are the beginning and end respectively.
        float actualMin = actual.getMin();
        float actualMax = actual.getMax();

        if(actualMin <= targetMin  &  actualMax >= targetMax)
            return RangeEval.OVERLAPS;  // i.e., ...---|--------------|---...
        if(actualMax <  targetMin  |  actualMin > targetMax)
            return RangeEval.OUTSIDE;  // i.e., ----..|..............|...... or ......|..............|..----
        if(actualMin <= targetMin   &  targetMin < actualMax & actualMax < targetMax)
            return RangeEval.OVERLAPS_MIN;  // i.e., ...---|------........|......
        if(actualMax >= targetMax   &  targetMin < actualMin & actualMin < targetMax)
            return RangeEval.OVERLAPS_MAX;  // i.e., ......|........------|---...
        if(actualMin > targetMin   &  actualMax < targetMax)
            return RangeEval.WITHIN;  // i.e., ......|.....-------..|......
        StaticLogger.logError("Cannot evaluate target range " + this + " and actual range " + actual);
        return RangeEval.UNKNOWN;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return Objects.equals(max, range.max) && Objects.equals(min, range.min);
    }
    @Override
    public int hashCode() {
        return Objects.hash(max, min);
    }

    @Override
    public String toString() {
        return "[" + min + ',' + max + "]";
    }
}

public class KernelRange  extends BaseKernel<Range, Void> {
    public KernelRange(String targetKey, Range targetValue) {
        super(targetKey, targetValue, null);
    }
    public KernelRange(String targetKey, Range targetValue, float weight) {
        super(targetKey, targetValue, null, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        Range actualValue = (Range) actual.getValue();
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
}
