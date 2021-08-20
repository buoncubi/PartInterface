package com.teseotech.partsInterface.coreInterface;

import com.teseotech.partsInterface.utility.LoggerInterface;

import java.util.Objects;

// A Pair like structure as <key,value>, e.g., <hasLength,0.2>.
// The datatype is inferred from the given `value`.
// `F` should be of type: `Integer, Float, Double, Long or String`.
public class Feature<F> implements LoggerInterface {
    private String key = null;
    private F value = null;

    public Feature(String key, F value){
        this.setKey(key);
        this.setValue(value);
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(F value) {
        this.value = value;
        if(!((value instanceof Integer) | (value instanceof Float) | (value instanceof Double)
                | (value instanceof Long) | (value instanceof String)))
            logWarning("The given feature value (i.e., " + value + " is of unknown type." );
    }

    public String getKey() {
        return key;
    }
    public F getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feature<?> feature = (Feature<?>) o;
        return Objects.equals(key, feature.key) && Objects.equals(value, feature.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return '<' + key + ':' + value + '>';
    }
    public String toDescription(){
        return  '<' + key + ":" + value.getClass().getSimpleName() + '>';
    }
}