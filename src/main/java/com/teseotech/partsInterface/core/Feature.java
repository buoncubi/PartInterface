package com.teseotech.partsInterface.core;

import java.util.Objects;

// a pair like structure as <key,value>, e.g., <hasLength,0.2>
public class Feature<T>{
    private String key = null;
    private T value = null;

    public Feature(String key, T value){
        this.setKey(key);
        this.setValue(value);
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(T value) {
        this.value = value;

    }

    public String getKey() {
        return key;
    }
    public T getValue() {
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
