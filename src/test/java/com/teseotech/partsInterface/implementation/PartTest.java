package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.implementation.kernel.*;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPartTest;
import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.teseotech.partsInterface.implementation.kernel.KernelPointTest.getKernelParams;

class PartTest {
    public static Set<BaseKernel<?,?>> getKernels() {  // Based on `OWLPartTest.getFeatures`
        // Define some features (shared to all parts for simplicity).
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelRange("hasFeature1", new Range(0,5)));
        kernels.add(new KernelPoint("hasFeature2", 2L, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature3", 3f, getKernelParams()));
        kernels.add(new KernelString("hasFeature4", "f4"));
        kernels.add(new KernelBoolean("hasFeature5", true));
        return kernels;
    }

    @Test
    public void testAffinity(){
        String partType = "MOTOR";

        // Define some parts (all equal for simplicity) in a new ontology.
        OWLReferences ontology = Configurer.createOntology(PartTest.class.getSimpleName(), Configurer.ONTO_TEST_FILE_PATH);
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
                    + ". You should check manually that te results are correct!");  // Should all affinities be one?
        }
    }
}