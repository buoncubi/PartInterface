package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.coreInterface.Feature;
import com.teseotech.partsInterface.coreInterface.KernelInterface;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.FullDataPropertyDesc;

public abstract class KernelOWL<F,P> extends KernelInterface<F,P> {
    private final FullDataPropertyDesc propDescriptor; // TODO use dedicated OWLOOP descriptor

    public KernelOWL(Feature<F> feature, OWLReferences ontology) {
        super(feature);
        this.propDescriptor = createOWLDescriptor(ontology);
    }
    public KernelOWL(String key, F value, OWLReferences ontology) {
        super(key, value);
        this.propDescriptor = createOWLDescriptor(ontology);
    }
    private FullDataPropertyDesc createOWLDescriptor(OWLReferences ontology){
        try {
            FullDataPropertyDesc propDescr = new FullDataPropertyDesc(this.getFeatureKey(), ontology);
            propDescr.readAxioms();
            return propDescr;
        } catch (Exception e){
            logError("cannot use ontology '" + ontology + "'!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean exists(){ // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        return !this.propDescriptor.getRangeRestrictions().isEmpty();
    }

    @Override
    public void addFeature() {
        if(this.shouldAdd()) {
            this.propDescriptor.addRangeDataRestriction(this.getFeatureType());
            this.propDescriptor.writeAxioms();
        }
    }
    @Override
    public void removeFeature() {
        if(this.shouldRemove()) {
            this.propDescriptor.removeRangeDataRestriction(this.getFeatureType());
            this.propDescriptor.writeAxioms();
        }
    }

    public FullDataPropertyDesc getOWLDescriptor() {
        return this.propDescriptor;
    }
}
