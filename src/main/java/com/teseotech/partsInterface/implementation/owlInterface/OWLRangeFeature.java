package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.implementation.kernel.Range;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;

public class OWLRangeFeature extends OWLFeature<Range> {
    public OWLRangeFeature(String key, Range value) {  // This is the constructor to be used if the Feature is inside a Part.
        super(key, value);
    }
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
