package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.implementation.owlInterface.owloopDescriptor.FeaturePropertyDescr;
import com.teseotech.partsInterface.utility.StaticLogger;
import com.teseotech.partsInterface.core.WritableFeature;
import it.emarolab.amor.owlInterface.OWLReferences;

public class OWLFeature<V> extends WritableFeature<V> {
    private FeaturePropertyDescr propDescriptor = null;

    // This is the constructor to be used if the Feature is inside a Part. It requires to invoke `setOWLDescriptor`.
    public OWLFeature(String key, V value) {
        super(key, value);
    }
    public OWLFeature(String key, V value, OWLReferences ontology) {
        super(key, value);
        setOWLDescriptor(ontology);
    }
    public void setOWLDescriptor(OWLReferences ontology){
        try {
            this.propDescriptor = new FeaturePropertyDescr(this.getKey(), ontology);
            this.propDescriptor.readAxioms();
        } catch (Exception e){
            StaticLogger.logError("cannot use ontology '" + ontology + "'!");
            e.printStackTrace();
            this.propDescriptor = null;
        }
    }
    public FeaturePropertyDescr getOWLDescriptor() {
        return this.propDescriptor;
    }

    public boolean isOWLDescriptorInitialised(){
        return this.propDescriptor != null;
    }

    @Override
    public Boolean exists(){ // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        if(!isOWLDescriptorInitialised()){
            StaticLogger.logError("An OWL feature (i.e.," + this.getKey() + ") not coupled with an ontology!");
            return null;
        }
        return !this.propDescriptor.getRangeRestrictions().isEmpty();
    }

    @Override
    public void addFeature() {
        if(this.shouldAdd()) {
            this.propDescriptor.addRangeDataRestriction(this.getType());
            this.propDescriptor.writeAxioms();
        }
    }
    @Override
    public void removeFeature() {
        if(this.shouldRemove()) {
            this.propDescriptor.removeRangeDataRestriction(this.getType());
            this.propDescriptor.writeAxioms();
        }
    }
}
