package com.teseotech.partsInterface.kernelImplementation;

import com.teseotech.partsInterface.core.Feature;
import com.teseotech.partsInterface.core.Kernel;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.Restrictions;
import it.emarolab.owloop.descriptor.utility.dataPropertyDescriptor.FullDataPropertyDesc;

public class KernelPoint extends Kernel {
    private final FullDataPropertyDesc propDescr; // TODO use dedicated OWLOOP descriptor

    public KernelPoint(Feature<?> feature, OWLReferences onto) {
        super(feature);
        this.propDescr = new FullDataPropertyDesc(this.getFeature().getKey(), onto);
        this.propDescr.readAxioms();
    }

    public <T> KernelPoint(String key, T value, OWLReferences onto) {
        super(new Feature<>(key, value));
        this.propDescr = new FullDataPropertyDesc(this.getFeature().getKey(), onto);
        this.propDescr.readAxioms();
    }

    @Override
    public boolean exists(){
        Restrictions r = this.propDescr.getRangeRestrictions();
        return !r.isEmpty();
    }

    @Override
    public void addFeature() {
        if(this.shouldAdd()) {
            this.propDescr.addRangeDataRestriction(this.getFeature().getValue().getClass());
            this.propDescr.writeAxioms();
        }
    }

    @Override
    public void removeFeature() {
        if(this.shouldRemove()) {
            this.propDescr.removeRangeDataRestriction(this.getFeature().getValue().getClass());
            this.propDescr.writeAxioms();
        }
    }

    @Override
    public float evaluate(Object... targetParams) {
        // ...
        return 0;
    }
}
