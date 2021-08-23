package com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface;

import com.teseotech.partsInterface.implementation.partEvaluation.core.WritableFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.FullDataPropertyDesc;

public class OWLFeature<F> extends WritableFeature<F> {
    private final FullDataPropertyDesc propDescriptor; // TODO use dedicated OWLOOP descriptor

    public OWLFeature(String key, F value, OWLReferences ontology) {
        super(key, value);
        this.propDescriptor = createOWLDescriptor(ontology);
    }
    private FullDataPropertyDesc createOWLDescriptor(OWLReferences ontology){
        try {
            FullDataPropertyDesc propDescr = new FullDataPropertyDesc(this.getKey(), ontology);
            propDescr.readAxioms();
            return propDescr;
        } catch (Exception e){
            StaticLogger.logError("cannot use ontology '" + ontology + "'!");
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

    public FullDataPropertyDesc getOWLDescriptor() {
        return this.propDescriptor;
    }
}
