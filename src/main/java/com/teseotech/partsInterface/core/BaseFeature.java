package com.teseotech.partsInterface.core;

import java.util.Objects;

// A Pair like structure as <key,value>, e.g., <hasLength,0.2>.
// The datatype is inferred from the given `value`.
// `F` should be of type: `Integer`, `Float`, `Double`, `Long`, `Boolean` or `String`.
public class BaseFeature<V> {
    private String key = null;
    private V value = null;

    public BaseFeature(String key, V value){
        this.setKey(key);
        this.setValue(value);
    }

    public void setKey(String key) {
        this.key = key;
    }
    public void setValue(V value) {
        this.value = value;
        // if(!((value instanceof Integer) | (value instanceof Float) | (value instanceof Double)
        //        | (value instanceof Long) | (value instanceof String)))
        //    StaticLogger.logWarning("The given feature value (i.e., " + value + ") is of unknown type." );
    }

    public String getKey() {
        return key;
    }
    public V getValue() {
        return value;
    }
    public Class<?> getType(){
        return this.getValue().getClass();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseFeature<?> feature = (BaseFeature<?>) o;
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