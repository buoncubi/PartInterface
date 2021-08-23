package com.teseotech.partsInterface.implementation.partEvaluation.implementation.affinity;

import com.teseotech.partsInterface.implementation.partEvaluation.core.Kernel;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeatureTest;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPartTest;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class PartTest {
    public static Set<Kernel<?,?>> getKernels(OWLReferences ontoRef) {  // Based on `OWLPartTest.getFeatures`
        // Define some features (shared to all parts for simplicity).
        Set<Kernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelPoint("hasFeature1", 1, new KernelPointParam()));
        kernels.add(new KernelPoint("hasFeature2", 2L, new KernelPointParam()));
        kernels.add(new KernelPoint("hasFeature3", 3f, new KernelPointParam()));
        return kernels;
    }

    @Test
    public void testAffinity(){
        String partType = "MOTOR";

        // Define some parts (all equal for simplicity) in a new ontology.
        OWLReferences ontology = OWLFeatureTest.setupTestOntology("AffinityTest");
        Set<OWLFeature<?>> kernels = OWLPartTest.getFeatures(ontology);
        new Part(partType, kernels, ontology).addInstance();
        new Part(partType, kernels, ontology).addInstance();
        new Part(partType, kernels, ontology).addInstance();

        // Retrieve some parts from the ontology.
        ontology.synchronizeReasoner();
        Set<Part> parts = OWLPart.readParts(partType, ontology);
        for(Part p: parts){
            p.queryAffinity(getKernels(ontology));
        }
    }
}