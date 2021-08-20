package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.coreInterface.KernelInterface;
import com.teseotech.partsInterface.coreInterface.PartInterface;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.utility.individualDescriptor.FullIndividualDesc;

import java.util.Set;

public abstract class PartOWL extends PartInterface {
    private final String partType;
    static private boolean LOG_FEATURE_ADDING = true;
    private final FullIndividualDesc indDescriptor;  // TODO use dedicated OWLOOP descriptors (see also elsewhere)

    public PartOWL(String identifier, String partType, Set<KernelInterface<?, ?>> kernels, OWLReferences ontology) {
        super(identifier, kernels);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
    }
    public PartOWL(String partType, Set<KernelInterface<?, ?>> kernels, OWLReferences ontology) {
        super(null, kernels);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
    }
    private FullIndividualDesc createOWLDescriptor(OWLReferences ontology){
        try {
            FullIndividualDesc indDescr = new FullIndividualDesc(getID(), ontology);
            indDescr.readAxioms();
            indDescr.addTypeIndividual(this.getType());
            return indDescr;
        } catch (Exception e){
            logError("Cannot use ontology '" + ontology + "'!");
            e.printStackTrace();
            return null;
        }
    }

    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void addPart() {}
    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void removePart() {}

    @Override
    public boolean exists() { // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        return !this.indDescriptor.getDataProperties().isEmpty();
    }

    @Override
    public void addInstance() {
        if(this.shouldAdd(LOG_FEATURE_ADDING)) {
            for (KernelInterface<?,?> k : this.getKernels()) {
                k.addFeature();  // It might make some changes to the TBox.
                this.indDescriptor.addData(k.getFeature().getKey(), k.getFeature().getValue());
            }
            this.indDescriptor.writeAxioms();
        }
    }
    @Override
    public void removeInstance() {
        if(this.shouldRemove(LOG_FEATURE_ADDING))
            removeInstance(indDescriptor.getOntologyReference(), getID()); // added `Features` are not removed.
    }
    static public void removeInstance(OWLReferences ontoRef, String instanceName){
        ontoRef.removeIndividual(instanceName);
    }

    public String getType() {
        return partType;
    }
    public FullIndividualDesc getOWLDescriptor() {
        return this.indDescriptor;
    }

    @Override
    public String toString() {
        return  getID()  + ':' + partType + "->" + getKernels();
    }

    static public void setLogFeatureAdding(boolean shouldLog){
        LOG_FEATURE_ADDING = shouldLog;
    }
}