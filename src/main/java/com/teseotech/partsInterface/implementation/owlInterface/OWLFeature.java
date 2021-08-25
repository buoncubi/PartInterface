package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.utility.StaticLogger;
import com.teseotech.partsInterface.core.WritableFeature;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.FullDataPropertyDesc;

public class OWLFeature<F> extends WritableFeature<F> {
    private FullDataPropertyDesc propDescriptor = null; // TODO use dedicated OWLOOP descriptor

    public OWLFeature(String key, F value) {  // This is the constructor to be used if the Feature is inside a Part.
        super(key, value);
    }
    public OWLFeature(String key, F value, OWLReferences ontology) {
        super(key, value);
        setOWLDescriptor(ontology);
    }
    public void setOWLDescriptor(OWLReferences ontology){
        try {
            FullDataPropertyDesc propDescr = new FullDataPropertyDesc(this.getKey(), ontology);
            propDescr.readAxioms();
            this.propDescriptor = propDescr;
        } catch (Exception e){
            StaticLogger.logError("cannot use ontology '" + ontology + "'!");
            e.printStackTrace();
            this.propDescriptor = null;
        }
    }
    public FullDataPropertyDesc getOWLDescriptor() {
        return this.propDescriptor;
    }

    public boolean isOWLDescriptorInitialised(){
        return this.propDescriptor == null;
    }

    @Override
    public Boolean exists(){ // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        if(isOWLDescriptorInitialised()){
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
