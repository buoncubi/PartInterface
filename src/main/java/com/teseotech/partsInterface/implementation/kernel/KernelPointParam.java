package com.teseotech.partsInterface.implementation.kernel;

import java.util.Objects;

/*
 * The parameter of a `KernelPoint`. It is a `<float:value, float:degree>` pair used to represent
 * discontinuity point in a fuzzy membership function.
 */
public class KernelPointParam {
    private final float value;
    private final float degree;

    public KernelPointParam(float value, float degree) {
        this.value = value;
        this.degree = degree;
    }

    public float getValue() {
        return value;
    }
    public float getDegree() {
        return degree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KernelPointParam that = (KernelPointParam) o;
        return Float.compare(that.value, value) == 0 && Float.compare(that.degree, degree) == 0;
    }
    @Override
    public int hashCode() {
        return Objects.hash(value, degree);
    }

    @Override
    public String toString() {
        return "(" + value + ',' + degree + ')';
    }
}
