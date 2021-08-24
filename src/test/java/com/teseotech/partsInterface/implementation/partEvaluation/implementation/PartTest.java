package com.teseotech.partsInterface.implementation.partEvaluation.implementation;

import com.teseotech.partsInterface.implementation.partEvaluation.core.Affinity;
import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseKernel;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.KernelPoint;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.KernelRange;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.KernelString;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.Range;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeatureTest;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLPartTest;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel.KernelPointTest.getKernelParams;
import static org.junit.jupiter.api.Assertions.*;

class PartTest {
    public static Set<BaseKernel<?,?>> getKernels() {  // Based on `OWLPartTest.getFeatures`
        // Define some features (shared to all parts for simplicity).
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelRange("hasFeature1", new Range(0,5)));
        kernels.add(new KernelPoint("hasFeature2", 2f, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature3", 3f, getKernelParams()));
        kernels.add(new KernelString("hasFeature4", "f4"));
        return kernels;
    }

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
        for(Part p: parts){
            Affinity affinity = p.queryAffinity(getKernels());
            System.out.println("Affinities found (partId, affinityDegree): " + affinity
                    + ". You should check manually that te results are correct!");
        }
    }
}