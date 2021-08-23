package com.teseotech.partsInterface.implementation.partEvaluation.implementation;

import com.teseotech.partsInterface.implementation.partEvaluation.core.Affinity;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.KernelPointTest;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeatureTest;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPartTest;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PartTest {
    @Test
    public void testAffinity(){
        String partType = "MOTOR";

        // Define some parts (all equal for simplicity) in a new ontology.
        OWLReferences ontology = OWLFeatureTest.setupTestOntology(PartTest.class.getSimpleName());
        Set<OWLFeature<?>> kernels = OWLPartTest.getFeatures(ontology);
        new Part(partType, kernels, ontology).addInstance();
        new Part(partType, kernels, ontology).addInstance();
        new Part(partType, kernels, ontology).addInstance();

        // Retrieve some parts from the ontology.
        ontology.synchronizeReasoner();
        Set<Part> parts = OWLPart.readParts(partType, ontology);
        Set<Affinity> affinities = new HashSet<>();
        for(Part p: parts){
            Affinity affinity = p.queryAffinity(KernelPointTest.getKernels(ontology));
            affinities.add(affinity);
            assertEquals(affinity.getDegree(), 1.0f);
        }
        System.out.println("Affinities found (partId, affinityDegree): " + affinities
                + ". By the definition of this test, the affinity should always be 1.");
    }
}