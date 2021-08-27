package com.teseotech.partsInterface.core;

import com.teseotech.partsInterface.utility.AddRemoveChecker;

/*
 * This class extends `BaseFeature<V>` and add abstract function for adding
 * (and removing) Features from (to) a data structure, e.g., an ontology.
 * This class is intended to be used as a Feature of `BasePart`, which
 * invokes adding and removing operations.
 */
public abstract class WritableFeature<V> extends BaseFeature<V> implements AddRemoveChecker {  // `AddRemoveChecker` simplifies ontology manipulation.
    public WritableFeature(String key, V value) {
        super(key, value);
    }

    // Intended add/remove generic definition of a Feature, e.g., change the TBox of an ontology.
    abstract public void addFeature();
    abstract public void removeFeature();

    @Override
    public String getCheckerLog() {  // Defined by `AddRemoveChecker`.
        // It defines a string used to log information while checking if a Part should be added/removed from/to the ontology.
        return "feature " +  this.toDescription();
    }
    // abstract public boolean exists() added by `AddRemoveChecker`
}
