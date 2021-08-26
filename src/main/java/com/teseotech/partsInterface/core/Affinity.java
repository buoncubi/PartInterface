package com.teseotech.partsInterface.core;

import java.util.Objects;

/*
 * This is the main output of the software architecture, which consists on a pair `<String:I, float:d>`,
 * where `I` is a Part identifier, and `d` is its degree of affinity in [0,1].
 * This class is instantiated by `BasePart.evaluateAffinity(Set<BaseKernel>`.
 */
public class Affinity {
    private final float degree;
    private final String identifier;

    public Affinity(float degree, String id){
        this.degree = degree;
        this.identifier = id;
    }

    public float getDegree() {
        return degree;
    }
    public String getID() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Affinity affinity = (Affinity) o;
        return Float.compare(affinity.degree, degree) == 0 && Objects.equals(identifier, affinity.identifier);
    }
    @Override
    public int hashCode() {
        return Objects.hash(degree, identifier);
    }

    @Override
    public String toString() {
        return '(' + identifier + ',' + degree + ')';
    }
}
