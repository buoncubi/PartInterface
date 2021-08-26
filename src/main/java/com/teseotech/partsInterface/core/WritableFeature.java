package com.teseotech.partsInterface.core;

import com.teseotech.partsInterface.utility.AddRemoveChecker;

/*
 * This class extends `BaseFeature<V>` and add abstract function for adding
 * (and removing) Features from (to) the ontology.
 * This class implements the `AddRemoveChecker`, which simplify the operation
 * involved for manipulating the ontology.
 */
public abstract class WritableFeature<V> extends BaseFeature<V> implements AddRemoveChecker {
    public WritableFeature(String key, V value) {
        super(key, value);
    }

    // Intended to change the TBox, i.e, add/remove generic logic definition of a Feature to/from the ontology.
    abstract public void addFeature();
    abstract public void removeFeature();

    @Override
    public String getCheckerLog() {
        // It defines a string used to log information while checking if a Part should be added/removed from/to the ontology.
        return "feature " +  this.toDescription();
    }
    // abstract public boolean exists() added by `AddRemoveChecker`
}
