package com.teseotech.partsInterface.implementation.partEvaluation;

import com.teseotech.partsInterface.coreInterface.Affinity;
import com.teseotech.partsInterface.coreInterface.KernelInterface;
import com.teseotech.partsInterface.coreInterface.Target;
import com.teseotech.partsInterface.implementation.owlInterface.PartOWL;
import it.emarolab.amor.owlInterface.OWLReferences;

import java.util.List;
import java.util.Set;

public class Part extends PartOWL {

    public Part(String identifier, String partType, Set<KernelInterface<?, ?>> kernels, OWLReferences ontology) {
        super(identifier, partType, kernels, ontology);
    }

    public Part(String partType, Set<KernelInterface<?, ?>> kernels, OWLReferences ontology) {
        super(partType, kernels, ontology);
    }

    @Override
    public List<Affinity> queryAffinity(Set<Target<?, ?>> targets) {
        return null;
    }
}
