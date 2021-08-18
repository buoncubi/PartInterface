package com.teseotech.partsInterface;

import com.teseotech.partsInterface.core.Kernel;
import com.teseotech.partsInterface.core.Part;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.utility.individualDescriptor.FullIndividualDesc;

import java.util.List;
import java.util.Set;

public class PartImplementation extends Part {
    static private long id_num = 0L;
    static private boolean LOG_FEATURE_ADDING = true;

    private final FullIndividualDesc indDescr;  // TODO use dedicated OWLOOP descriptors (see also elsewhere)

    public PartImplementation(String typeName, String instanceId, OWLReferences ontology, Set<Kernel> kernels) {
        super(typeName, kernels);
        if (instanceId == null){
            try {
                instanceId = "P" + id_num++;
            } catch (Exception e) {
                System.out.println("WARNING: resetting part identifier!");
                id_num = 0L;
                instanceId = "P" + id_num++;
            }
        }
        this.indDescr = new FullIndividualDesc(instanceId, ontology);
        this.indDescr.readAxioms();
        this.indDescr.addTypeIndividual(this.getTypeName());
        this.setInstanceID(instanceId);
    }

    @Override
    public boolean exists() {
        return !this.indDescr.getDataProperties().isEmpty();
    }

    @Override
    public void addInstance() {
        if(this.initialised() & this.shouldAdd(LOG_FEATURE_ADDING)) {
            for (Kernel k : this.getKernels()) {
                k.addFeature();
                this.indDescr.addData(k.getFeature().getKey(), k.getFeature().getValue());
            }
            this.indDescr.writeAxioms();
        }
    }

    @Override
    public void removeInstance() {
        if(this.initialised() & this.shouldRemove(LOG_FEATURE_ADDING))
            removeInstance(indDescr.getOntologyReference(), getInstanceID());
    }

    static public void removeInstance(OWLReferences ontoRef, String instanceName){
        ontoRef.removeIndividual(instanceName);  // added features are not removed.
    }

    @Override
    public List<?> queryAffinity(Set<Kernel> targets) {
        return null;
    }

    static public void setLogFeatureAdding(boolean shouldLog){
        LOG_FEATURE_ADDING = shouldLog;
    }
}
