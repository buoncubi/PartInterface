package com.teseotech.partsInterface.core;

import com.teseotech.partsInterface.utility.AddRemoveChecker;

public abstract class WritableFeature<V> extends BaseFeature<V> implements AddRemoveChecker {

    public WritableFeature(String key, V value) {
        super(key, value);
    }

    // intended to change the TBox to define a generic feature with a given `key` and datatype derived from a `value`.
    abstract public void addFeature();
    abstract public void removeFeature();
    // abstract public boolean exists() added by `AddRemoveChecker`

    @Override
    public String getCheckerLog() {
        return "feature " +  this.toDescription();
    }
}
