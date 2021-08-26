package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.implementation.kernel.Range;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;

/*
 * It manages the OWL Data Property used to represent a Feature with a `Range` Dataype.
 * In particular, it adds to the ontology two properties with a `Float` Datatype to represent
 * the `min` and `max` value of a `Range`.
 */
public class OWLRangeFeature extends OWLFeature<Range> {
    // This is the constructor to be used if the Feature should be coupled with a Part.
    public OWLRangeFeature(String key, Range value) {
        super(key, value);
    }
    // This is the constructor to be used if the Feature should be used standalone.
    public OWLRangeFeature(String key, Range value, OWLReferences ontology) {
        super(key, value);
        setOWLDescriptor(ontology);
    }

    @Override
    public Boolean exists(){ // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        if(!isOWLDescriptorInitialised()){
            StaticLogger.logError("An OWL feature (i.e.," + this.getKey() + ") not coupled with an ontology!");
            return null;
        }
        return this.getOWLDescriptor().getRangeRestrictions().size() == 2;
    }

    @Override
    public void addFeature() {
        if(this.shouldAdd()) {
            this.getOWLDescriptor().addRangeDataRestriction(Float.class);
            this.getOWLDescriptor().writeAxioms();
        }
    }
    @Override
    public void removeFeature() {
        if(this.shouldRemove()) {
            this.getOWLDescriptor().removeRangeDataRestriction(Float.class);
            this.getOWLDescriptor().writeAxioms();
        }
    }
}
