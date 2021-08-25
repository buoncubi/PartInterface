package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class KernelStringTest {
    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelString("hasFeature1", "f1 ", 5));
        kernels.add(new KernelString("hasFeature2", "???", 1));
        kernels.add(new KernelString("hasFeature3", "F3 ", 5));
        return kernels;
    }
    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontoRef) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", "F1 ", ontoRef));
        features.add(new OWLFeature<>("hasFeature2", "F2", ontoRef));
        features.add(new OWLFeature<>("hasFeature3", "F3", ontoRef));
        return features;
    }

    @Test
    public void testAffinity(){
        // Define some features and target kernels.
        OWLReferences ontology = Configurer.createOntology(KernelPointTest.class.getSimpleName(), Configurer.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> kernels = getKernels();

        // For all feature, test the kernel's evaluation.
        for(BaseKernel<?,?> k: kernels){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            if(affinity != null)
                System.out.println("The evaluation of " + k.toDescription() + " is: " + affinity + ". You should check manually if it is correct.");
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }
}