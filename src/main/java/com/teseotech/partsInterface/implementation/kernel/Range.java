package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.utility.StaticLogger;

import java.util.Objects;

enum RangeEval{WITHIN, OVERLAPS, OVERLAPS_MIN, OVERLAPS_MAX, OUTSIDE, UNKNOWN}

public class Range {
    private final Number max;
    private final Number min;

    public Range(Number n1, Number n2) {
        if(n1.floatValue() < n2.floatValue()){
            this.min = n1;
            this.max = n2;
        } else {
            this.min = n2;
            this.max = n1;
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

    public RangeEval checkOverlaps(Range actual) {
        // The target range (i.e., `this`) is shown graphically is the comments below as '|' where `min` is before `max`
        float targetMin = this.getMin();
        float targetMax = this.getMax();
        // The actual rage (i.e., to test against target) is shown graphically as '------' where `min` and `max` are the beginning and end respectively.
        float actualMin = actual.getMin();
        float actualMax = actual.getMax();

        if (actualMin <= targetMin & actualMax >= targetMax)
            return RangeEval.OVERLAPS;  // i.e., ...---|--------------|---...
        if (actualMax < targetMin | actualMin > targetMax)
            return RangeEval.OUTSIDE;  // i.e., ----..|..............|...... or ......|..............|..----
        if (actualMin >= targetMin & actualMax <= targetMax)
            return RangeEval.WITHIN;  // i.e., ......|.....-------..|......
        if (actualMin <= targetMin & targetMin < actualMax & actualMax < targetMax)
            return RangeEval.OVERLAPS_MIN;  // i.e., ...---|------........|......
        if (actualMax >= targetMax & targetMin < actualMin & actualMin < targetMax)
            return RangeEval.OVERLAPS_MAX;  // i.e., ......|........------|---...
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
        return min + "-" + max;
    }
}
